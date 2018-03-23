package jeux.quarto;

import iia.jeux.modele.*;
import iia.jeux.modele.joueur.Joueur;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.IllegalArgumentException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlateauQuarto implements PlateauJeu{
    /********** commentaires *********/ 
    /// Lignes = chiffres
    /// Colonne = lettres
    ///0x0000 4
    ///  0000 3
    ///  0000 2
    ///  0000 1
    ///  DCBA
    /// En bas à droite, on a la case A1 
    
    /// Opérateurs logiques sur les entiers
    ///    ^ -> XOR
    ///    | -> OR
    ///    & -> AND

    /// << -> Shift G
    /// >>> -> shift D logique (non signé)
    /// !! NE PAS UTILISER '>>' !! ///
    /// Il faut caster le résultat en byte/short pour les opérateurs au dessus
    
    /// Apparemment, pas d'entiers non-signés en java.
    // Don = 0, dépôt = 1
    // Syntaxe des pièces ("en commençant par les bits de poids fort="
    // b/r -> bleu , rouge. resp. 1 et 0 en notation binaire.
    // g/p -> grand, petit. resp. 1 et 0
    // p/t -> plein, troué. Resp. 1 et 0
    // c/r -> carré, rond . Resp. 1 et 0
    
    // Le joueur noir commence à donner une pièce
    
    // Note : il faut définir ce qui est ligne et ce qui est colonne
    /*******************
     *	
     * Attributs 
     *
     *******************/	
    
    // au lieu de faire j1 et j2, on appelle les joueurs j0 et j1.
    public static Joueur j0; 
    public static Joueur j1; 
    public static String str_j0 = "noir";
    public static String str_j1 = "blanc";
    
    // Plateau de jeu
    private long plateau = 0;

    // Montre quelles pièces sont jouées.
    private short indCases = 0; 
    private short indPiece = 0; // On marque la pièce comme jouée lors du don de la pièce
    
    // id_joueur id_action 00 id_piexe
    private byte tourEtPiece = 0;


    /*************  constructeurs  ****************/
    
    public PlateauQuarto(){
    }
    
    public PlateauQuarto(Joueur joueurZero, Joueur joueurUn){
	j0 = joueurZero;
	j1 = joueurUn;
    }
    
    // ok
    public PlateauQuarto(long plateau, short indcases, short indpiece, byte tourEtPiece){
	this.plateau = plateau;
	this.indCases = indcases;
	this.indPiece = indpiece;
	this.tourEtPiece = tourEtPiece;
	// Pas besoin d'initialiser j1 et j2 puisque ce sont des attributs statiques 
    }
    

    /************ Méthodes privées ****************/
    
    public byte get_piece (byte colonne, byte ligne) throws Exception {
	if ( (indCases >>> (ligne * 4 + colonne) ) % 2 == 0 )
	    throw new Exception();
       
	// Ligne et colonne sont comris entre 0 et 3
	return 
	    (byte) ((plateau >>> ((ligne*4 + colonne)*4)) & (0x0F));
    }

    // Utile pour les carrés
    // Précondition : 0 <= ligne/colonne < 3
    public byte get_double_piece(byte colonne, byte ligne) throws Exception {
	if ( (indCases >>> (ligne * 4 + colonne) ) % 2 == 0  || ( indCases >>> (ligne * 4 + colonne + 1) ) % 2 == 0 )
	    throw new Exception();
	
	// Ligne et colonne sont comris entre 0 et 3
	return (byte) (plateau >>> ((ligne*4 + colonne)*4));
    }

    public byte points_communs(byte p1, byte p2){
	return (byte) (0x0F & ( ~ (p1 ^ p2) ));
    }

    // Teste les points communs entre deux "double pièce", ou chaque byte correspond à deux ids de pièces
    // A utiliser avec get_double_piece
    // db1 = [id_pièce 1a; id_piece2a]
    // db2 = [id_piece 1b; id_piece2b]
    public byte points_communs_double_pieces(byte db1, byte db2){
	// de la forme [points communs entre 1a et 1b ; points communs entre 2a et 2b]	
	byte points_communs = (byte) ~ (db1 ^ db2 );	
	return (byte) ((points_communs >>> 4 ) & (0x0F & points_communs));
    }
    
    /// id_colonne id_ligne < 3
    private boolean test_carre(byte id_colonne, byte id_ligne ){
	byte dp1, dp2;
	try {
	    dp1 = get_double_piece(id_colonne, id_ligne);
	    dp2 = get_double_piece(id_colonne, (byte) (id_ligne + 1));
	} catch( Exception e) {
	    return false;
	}
	
	return (points_communs_double_piece(dp1, dp2) != 0x00);
    }
    
    private boolean test_ligne(byte num_ligne){
	byte b1, b2;
	try {
	    b1 = get_double_piece(0, id_ligne);
	    b2 = get_double_ligne(2, id_ligne);
	} catch (Exception e) {
	    return false;
	}
	
	return (points_communs_double_piece(b1, b2) != 0);
    }
    
    // Les deux à la fois 
    private boolean test_diagonales(){
	byte [] g = new byte[4],
	        d = new byte[4];
	boolean g_false = false,
	    d_false = false;
	
	for(int i = 0; i<4; i++){
	    try{
		g[i] =  get_piece(i, i);
	    } catch (Exception e) {
		g_false = true;
		if(d_false)
		    return false;
	    }

	    try{
		D[i] =  get_piece(3-i, 3-i);
	    } catch (Exception e) {
		d_false = true;
		if(g_false)
		    return false;
	    }    
	}
	
	return true;
    }
    
    private boolean test_colonne(byte colonne){
	byte c1, c2, c3, c4;
	try{
	    c1 = get_piece(colonne, (byte) 0);
	    c2 = get_piece(colonne, (byte) 1);
	    c3 = get_piece(colonne, (byte) 2);
	    c4 = get_piece(colonne, (byte) 3);
	} catch (Exception e) {return false;}
	
	return (points_communs (c1, c2) & points_communs(c3, c4) != 0);
    }
    
    // ok
    private boolean j0plays(){
	return (tourEtPiece >>> 7) % 2  == 0;
    }
    
    // ok
    private boolean is_don(){
	return (tourEtPiece >>> 6) % 2  == 0;
    }
    
    // ok
    /* AUCUN TEST. On part du principe que le coup est valide */
    private void unsafe_jouer_coup_depot(byte coup){
	// 1. Dépot de la pièce
	byte piece = (byte) (coup & 0x0F),
	    coord = (byte) (coup >>> 4);
	
	plateau = plateau | (piece << (4*coord));
	
	// 2. Marquer la coordonnée comme jouée 
	indCases = (short) (indCases | ((0x0001) << ((short) coord))); // -> on met un '1' au coord-ème bit (à partir de la droite) du short - qu'on présume à 0. 
	
	
	// 3. Changement du statut du tour : Le joueur venant de poser un pion
	//    donne une autre pièce à l'adversaire : On change le 2e bit
	//    de tourEtPiece.
	tourEtPiece = (byte) (0x40^tourEtPiece);
    }

    
    /* Aucun test. On part aussi du principe que piece est de la forme 0x0(pdpiece) */
    // ok    
    private void unsafe_jouer_coup_don(byte piece){
	// 1 : montrer la pièce qu'il faut jouer 
	tourEtPiece = (byte) (piece | (tourEtPiece & 0xF0));
	
	// 2 : Marquer la pièce comme jouée (même principe qu'au dessus)
	indPiece =(short) (indPiece | ((0x0001) << piece));
	
	// 3 : Changer l'état du tour. L'autre joueur va jouer un autre type de tour.
	tourEtPiece = (byte) (0xC0 ^ tourEtPiece);
	// 0xC = 0b1100
    }
    
    /********** Méthodes utiles pour les tests *********/

    /**
     * @brief renvoie la représentation sous un octet de la pièce mentionnée en paramètre
     * @param idpiece l'identifiant "string" de la pièce
     * @return l'identifiant de la pièce associée à la str associée en paramètre
     ***/
    public static byte stringToPiece(String strPiece){
	byte ret = 0x00;
	
	char [] idPiece = strPiece.toCharArray();
	
	// Pê il faudrait faire genre plutôt String.get(i)
	byte id_krq = 0x00;
	if( idPiece[0] == 'b' ) // b = bleu = blanc = 1
	    id_krq = 0x08; // 0b1000
	if( idPiece[1] == 'g' ) // g = grand = 1
	    id_krq = (byte) (0x04 ^ id_krq); // 0b0100
	if( idPiece[2] == 'p' ) // plèce pleine
	    id_krq = (byte) (0x02 ^ id_krq);
	if( idPiece[3] == 'c' ) // pièce carrée
	    id_krq = (byte) (0x01 ^ id_krq);
	
	return id_krq; 
    }
    

    /**
     * @brief renvoie la représentation sous forme de chaine de caractères de la pièce en paramètre
     * @param idPiece l'identifiant de la pièce.
     * @return la chaine de caractères associée à l'identifiant
     ***/
    public static String pieceToString(byte idPiece){
	// Bleu/rouge, Grand/petit, Plein/troué, Rond/carré
	// Bleu = blanc, Rouge = noir
	
	char[] str = new char[4];
	
	if(idPiece % 2 ==  0) // 0 = rond
	    str[3] = 'r';
	else str[3] = 'c';
	
	if((idPiece >>> 1) % 2 == 0) // 0 = troué
	    str[2] = 't';
	else str[2] = 'p';
	
	if((idPiece>>> 2) % 2 == 0) // 0 = troué
	    str[1] = 'p';
	else str[1] = 'g';
	
	if((idPiece>>> 3) % 2 == 0) // 0 = troué
	    str[0] = 'r';
	else str[0] = 'b';

	
	return new String(str);
    }

    
    /********************************************************
     *
     * Méthodes de PlateauJeu 
     *
     ********************************************************/
    
    
    // Apparemment pas besoin de vérifier que c'est le bon joueur qui demande. On devrait pê faire une fonction genre "joueur jouant" ou quelque chose comme ça
    // ok    
    public ArrayList<CoupJeu> coupsPossibles(Joueur j) {	
	ArrayList<CoupJeu> ret = new ArrayList<CoupJeu>();
	
	// Si c'est pas le bon joueur
	if (j0plays() && j.equals(j1) || (!j0plays() && j.equals(j0)))
	    throw new IllegalArgumentException("CoupsPossibles : Mauvais joueur demandé");
	
	if( is_don() ){ // Il faut donner la pièce
	    for(byte  i = 0; i<16; i++){
		if ( (indPiece >>> i) %2  == 0)
		    ret.add(new CoupQuarto( i ));
	    }
	    
	} else { // Sinon, dépôt de la pièce

	    for(byte  i = 0; i<16; i++){
		if ( (indCases >>> i) %2  == 0)
		    ret.add(new CoupQuarto( i ));
	    }
	}	
	return ret;
    }

    
    public void joue(Joueur j, CoupJeu cj) {
	
	// 1. vérification que c'est le bon joueur qui joue
	if( ! coupValide(j, cj) ) 
	    throw new IllegalArgumentException( "joue() : Coup invalide" );

	// On peut jouer le coup s'il est valide	
	CoupQuarto c = (CoupQuarto) cj;
	
	// 2. vérification du type du coup
	if( is_don() ){
	    byte idpiece = c.get();
	    unsafe_jouer_coup_don(idpiece);
	    
	} else { // C'est un dépôt
	    byte id_piece = c.get();
	    unsafe_jouer_coup_don( id_piece );
	}
    }
    
    
    public PlateauJeu copy() {
	return new PlateauQuarto(plateau, indCases, indPiece, tourEtPiece);
    }
    
    public boolean coupValide(Joueur j, CoupJeu cj) {
	CoupQuarto cq = (CoupQuarto) cj;
	byte id_coup = cq.get();
	
	return
	    // 1: vérification que c'est le bon joueur qui joue
	    ((j0plays() && j.equals(j0)) || (!j0plays() && j.equals(j1))) 
	    &&
	    
	    // 2 : Vérification de la validité du coup
	    (  is_don() && ((indPiece >>> id_coup) %2 == 0 )
	       || (!is_don()) && (indCases >>> id_coup) % 2 == 0) 	    ;
    }
   
    ///TODO
    public boolean finDePartie(){
	for(byte i = 0; i<4; i++){
	/// Test des lignes
	    if(test_ligne(i) || test_colonne(i) ) return true;
	}
	/// Test des diagonales 
	if (test_diagonales()) return true;
	/// Test des carrés
	for(byte i = 0; i<3; i++)
	    for(byte j = 0; j<3; j++)
		if(test_carre(i, j)) return true;
		   
		   
	/// Cas ou toutes les pièces ont été posées
	return indCases == 0xFFFF;
    }
    
    
    /*********** Méthodes de Partiel **************/
    
    /// TODO
    public static String coordToString(byte coordonnee_case){
	byte chiffre =(byte) (0x03 & coordonnee_case);
	byte lettre = (byte) ((0x0C & coordonnee_case) >>> 2);
	
	char char_lettre = '?';
	
	switch(lettre){
	case 0:
	    char_lettre = 'A';
	    break;
	case 1:
	    char_lettre = 'B';
	    break;
	case 2:
	    char_lettre = 'C';
	    break;
	case 3:
	    char_lettre = 'D';
	    break;
	default:
	    char_lettre = '?';
	    break;
	}
	
	return char_lettre + Integer.toString(chiffre);
    }

    /// TODO FIRST
    public String toString(){
	return null;
    }

    // Modifié le 22/03
    public boolean estmoveValide(String move, String player){
	CoupQuarto cj = new CoupQuarto(move);
	Joueur j;
	if (player.equals(str_j0) ) 
	    j = j0;
	else  j = j1;
	
	return estValide(cj, j);
    }

    // impléemnté 
    public String[] mouvementsPossibles(String player){
	Joueur j;
	if(player.equals(str_j0))
	    j = j0;
	else j = j1;
	ArrayList<CoupJeu> cj_arr;
		
	// Tester à quel moment du jeu on est, puis faire appel à 
	try {
	    cj_arr =  this.coupsPossibles(j) ;
	} catch ( IllegalArgumentException e ) {
	    return null;
	}
		
	String[] ret = new String[cj_arr.size()];
	boolean is_don = this.is_don();
		
	for(int i = 0; i<cj_arr.size(); i++){
	    CoupJeu cj = cj_arr.get(i);
	    CoupQuarto cq = ( CoupQuarto ) cj;
	
	    ret[i] = cq.toString(is_don);
	}
	
	return ret;	
    }
       

    // player : noir = j0;
    // blanc = j1.
    /**
     * @brief joue le coup, matérialisé par une chaine de caractères.
     * Ce coup peut être sous deux formes différentes : deux lettres matérialisant une coordonnées (par 
     * ex. "A4", ou quatre lettres matérialisant une pièce ("rppc"), ou la première lettre représente la couleur, la deuxième représente 
     *  la taille, la troisième représente si la pièce est pleine ou vide, la quatrième représente
     * si ka mièce est carrée ou ronde.
     *
     * @param move le coup joué
     * @param player le joueur jouant le coup
     * @return rien si le coup est joué, sinon lance une exception 
     *
     * @version 
     **/
    public void play(String move, String player){ 
	Joueur j;
	if( j0.toString().equals(player) )   
	    j = j0;
	else j = j1;
	CoupQuarto cq = new CoupQuarto(move);
	joue(j, cq);
	
    }
    
    
    public Joueur getJ0() {
	return j0;
    }
	
    public Joueur getJ1() {
	return j1;
    }

     
    /*********** Méthodes de Partiel **************/
    
    
    /// TODO
    public void setFromFile(String fileName) throws FileNotFoundException, IOException {
	// On peut calculer le joueur qui doit jouer en fonction du nombre de pièces posées.

	/// Note: étant donné qu'on utilise java préhistorique, ce code donne une erreur (car le bufferedReader doit pas être déclaré dans un try, je crois... Ou il faut un bloc "finally")
	/// Je mets ça en commentaire pour l'instant (cc flemme masterrace) 
	
	  try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	  String line;
	  byte [] pieces = new byte[16];
	  int cpt = 0;
	  while ((line = br.readLine()) != null) {
	  if (line.charAt(0) != '%') {
	  String[] s = line.split(" ");
		    
	  // TODO : création du plateau
	  for(int i=1; i<5; i++) {
		  pieces[cpt] = stringToPiece(s[i]);
		  cpt++;
	  }
	  }
	  }
	  this.plateau = ByteBuffer.wrap(pieces).getLong();
	  }
    }

    // Modifié le 22/03
    public boolean estmoveValide(String move, String player){
	CoupQuarto cj = new CoupQuarto(move);
	Joueur j;
	if (player.equals(str_j0) ) 
	    j = j0;
	else  j = j1;

	return estValide(cj, j);
    }

    public void saveToFile(String fileName) throws Exception {
	// TODO : Convertir le plateau en lignes de String -> on codera tout ça dans toString()
	ArrayList<String> text = new ArrayList<String>();
	Path file = Paths.get(fileName);
	
	text.add("% Etat initial du plateau de jeu:");
	text.add("% ABCD");
	
	for(int i=0; i<4; i++) {
		String line = (i+1) +" ";
		
		for(int j=0; j<4; j++) {
			byte piece = get_piece((byte) j, (byte) i);
			line = line + pieceToString(piece) + " ";
			
		}
		
		line +=  (i+1);
		text.add(line);
	}
	
	text.add("% ABCD");
	
	Files.write(file, text, Charset.forName("UTF-8"));
    }

}
