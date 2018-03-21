package jeux.quarto;

import iia.jeux.modele.*;
import iia.jeux.modele.joueur.Joueur;

import java.io.PrintStream;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

public class PlateauQuarto implements PlateauJeu{
    /********** commentaires *********/ 

    /// Opérateurs logiques sur les entiers
    ///    ^ -> XOR
    ///    | -> OR
    ///    & -> AND

    /// << -> Shift G
    /// >>> -> shift D logique (non signé)
    /// !! NE PAS UTILISER '>>' !! ///
    /// Il faut caster le résultat en byte/short pour les opérateurs au dessus
    
    /// Apparemment, pas d'entiers non-signés en java.

    // Syntaxe des pièces ("en commençant par les bits de poids fort="
    // b/r -> bleu , rouge. resp. 1 et 0 en notation binaire.
    // g/p -> grand, petit. resp. 1 et 0
    // p/t -> plein, troué. Resp. 1 et 0
    // c/r -> carré, rond . Resp. 1 et 0

    /************* Attributs *******************/
    
    // au lieu de faire j1 et j2, on appelle les joueurs j0 et j1.
    public static Joueur j0; 
    public static Joueur j1; 
    
    // Plateau de jeu
    private long plateau = 0;

    // Montre quelles pièces sont jouées.
    private short indCases = 0; 
    private short indPiece = 0; // On marque la pièce comme jouée lors du don de la pièce
    
    // id_joueur id_action 00 id_piexe
    private byte tourEtPiece = 0;


    /*************  constructeurs  ****************/
    public PlateauQuarto(){}
    
    public PlateauQuarto(Joueur joueurZero, Joueur joueurUn){
	j0 = joueurZero;
	j1 = joueurUn;
    }
    
    public PlateauQuarto(long plateau, short indcases, short indpiece, byte tourEtPiece){
	this.plateau = plateau;
	this.indCases = indcases;
	this.indPiece = indpiece;
	this.tourEtPiece = tourEtPiece;
	// Pas besoin d'initialiser j1 et j2 puisque ce sont des attributs statiques 
    }
    
    /************ Méthodes privées ****************/

    private boolean j0plays(){
	return (tourEtPiece >>> 7) % 2  == 0;
    }
    
    private boolean is_don(){
	return (tourEtPiece >>> 6) % 2  == 0;
    }
    
    
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
    private void unsafe_jouer_coup_don(byte piece){
	// 1 : montrer la pièce qu'il faut jouer 
	tourEtPiece = (byte) (piece | (tourEtPiece & 0xF0));
	
	// 2 : Marquer la pièce comme jouée (même principe qu'au dessus)
	indPiece =(short) (indPiece | ((0x0001) << piece));
	
	// 3 : Changer l'état du tour. L'autre joueur va jouer un autre type de tour.
	tourEtPiece = (byte) (0xC0 ^ tourEtPiece);
	// 0xC = 0b1100
    }
    
    /********** Méthodes utiles pour les tests *****/

    /**
     * @brief renvoie la représentation sous un octet de la pièce mentionnée en paramètre
     * @param idpiece l'identifiant "string" de la pièce
     * @return l'identifiant de la pièce associée à la str associée en paramètre
     ***/
    public byte stringToPiece(String strPiece){
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
    public String pieceToString(byte idPiece){
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

    
    public void joue(Joueur j, CoupJeu cj){
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
    
    
    ///TODO
    public boolean finDePartie(){
	/// Test des lignes
	
	/// Test des colonnes
	
	/// Test des diagonales 
	
	/// Test des carrés
	
	return indCases == 0xFFFF;
    }

    public PlateauJeu copy(){
	return new PlateauQuarto(plateau, indCases, indPiece, tourEtPiece);
    }

    public boolean coupValide(Joueur j, CoupJeu cj){
	CoupQuarto cq = (CoupQuarto) cj;
	byte id_coup = cq.get();
	
	return
	    // 1: vérification que c'est le bon joueur qui joue
	    ((j0plays() && j.equals(j0)) || (!j0plays() && j.equals(j1))) 
	    &&
	    
	    // 2 : Vérification de la validité du coup
	    (  is_don() && ((indPiece >>> id_coup) %2 == 0 )
	       || (!is_don()) && (indCases >>> id_coup) % 2 == 0) 
	    ;
    }
    
    /*********** Méthodes de Partiel **************/
    
    /// TODO

    public static String coord_toString(byte coordonnee_case){
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
	
    /// TODO
    public void setFromFile(String fileName){
	// On peut calculer le joueur qui doit jouer en fonction du nombre de pièces posées. 
	return;
    }
    
    /// TODO
    public void saveToFile(String fileName){
	return;
    }

    public boolean estmoveValide(String move, String player){
	return false;
    }

    public String[] mouvementsPossibles(String player){
	Joueur j;
	if(player.equals("noir"))
	    j = j0;
	else j = j1;
	ArrayList<CoupJeu> cj;
	
	// Tester à quel moment du jeu on est, puis faire appel à 
	try {
	    cj_arr = this.coupsPossibles(j);
	} catch( IllegalArgumentException e ) {
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
    public void play(String move, String player){ 
	int id_joueur = 0;
	if(player.equals("blanc"))
	    id_joueur = 1;
	return;
    }

}
