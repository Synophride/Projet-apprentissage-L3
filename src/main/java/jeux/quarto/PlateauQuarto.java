package jeux.quarto;

import iia.jeux.modele.*;
import iia.jeux.modele.joueur.Joueur;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

public class PlateauQuarto implements PlateauJeu {

    /********** commentaires *********/

    /// Opérateurs logiques sur les entiers
    /// ^ -> XOR
    /// | -> OR
    /// & -> AND

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
    // Todo: modifier

    /*******************
     * 
     * Attributs
     *
     *******************/

    // au lieu de faire j1 et j2, on appelle les joueurs j0 et j1 : J1 devient
    // J0,
    // j2 devient j1
    public static Joueur j0;
    public static Joueur j1;

    // Plateau de jeu
    // ligne colonne
    private byte[][] plateau = new byte[4][4];

    byte piece_a_jouer = -1;
    int indice_pieces = 0;

    byte etat_du_tour = 0;

    /************* constructeurs ****************/

    /**
     * Constructeur de base de la classe. Rend un plateau vide.
     **/
    public PlateauQuarto() {
	for (byte i = 0; i < 16; i++) {
	    plateau[i / 4][i % 4] = (byte) -1;
	}
    }

    /**
     * Constructeur de PlateauQuarto initialisant le joueurs
     * 
     * @param joueurZero
     *            le premier joueur à jouer (qui va donner une pièce en premier
     * @param joueurUn
     *            le second joueur à jouer
     **/
    public PlateauQuarto(Joueur joueurZero, Joueur joueurUn) {
	j0 = joueurZero;
	j1 = joueurUn;
		
	for (byte i = 0; i < 16; i++) {
	    plateau[i / 4][i % 4] = -1;
	}
    }

    /**
     * Constructeur initialisant à partir d'un état du plateau,
     * 
     * @param plateau
     *            L'état du plateau. Une case contient un id de pièce si elle
     *            est occupée, -1 sinon
     * @param pieceAJouer
     *            La pièce qui doit être jouée dans le cas d'un dépôt de pièce.
     *            Dans le cas contraire (ou il faut donner une pièce), cet
     *            attribut n'est pas significatif
     * @param indicesPieces
     *            entier dénotant des pièces jouées
     * @param etatDuTour
     *            Dénote de l'état du tour.
     **/
    public PlateauQuarto(byte[][] plateau, byte pieceAJouer, int indicesPieces, byte etatDuTour) {
	this.plateau = plateau;
	this.piece_a_jouer = pieceAJouer;
	this.indice_pieces = indicesPieces;
	this.etat_du_tour = etatDuTour;
    }

    /************ Méthodes privées ****************/

    /**
     * Méthode déterminant si la pièce représentée par l'idPiece a déjà été
     * jouée
     * 
     * @param idPiece
     *            L'identifiant de la pièce dont on cherche à savoir si elle a
     *            déjà été jouée
     * @return true si la pièce a été jouée, false sinon
     **/
    private boolean has_been_played(byte idPiece) {
	return (indice_pieces >>> idPiece) % 2 == 1;
    }

    /**
     * Méthode permettant d'accéder à l'identifiant de la pièce présente aux
     * coordonnées (colonnes, lignes).
     * 
     * @param colonne
     *            la coordonnée "colonne" de la pièce à laquelle on veut accéder
     *            (entre 0 et 3 inclus)
     * @param ligne
     *            la coordonnée "ligne" de la pièce à laquelle on veut accéder
     *            (entre 0 et 3 inclus)
     * @return l'identifiant de la pièce accedée si cette pièce est posée sur le
     *         plateau -1 sinon
     * @see get_double_piece
     * @see points_communs
     ***/
    private byte get_piece(byte colonne, byte ligne) {
	return plateau[ligne][colonne];
    }

    /**
     * Méthode permettant d'accéder à l'identifiant de la pièce présente aux
     * coordonnées indiquées par le byte (celui de coupQuarto.get() )
     * 
     * @param coord
     *            l'identifiant de la pièce, de la forme 0b0000 coord_ligne
     *            (2b.) coord_colonne (2b.)
     * @return l'identifiant de la pièce accedée si cette pièce est posée sur le
     *         plateau -1 sinon
     * @see get_double_piece
     * @see points_communs
     ***/
    private byte get_piece(byte coord) {
	int id_colonne = 0b0011 & coord;
	int id_ligne = coord >>> 2;
	return plateau[id_ligne][id_colonne];
    }
    /**
     * Ajoute la pièce byte aux coordonnées en paramètre 
     * @param coord
     *          les coordonnées de là ou on veut mettre la pièce. On part du principe que la case est vide
     * @param piece 
     *          
     **/
    private void put_piece(byte coord, byte piece) {
	int id_colonne = 0b011 & coord;
	int id_ligne = coord >>> 2;
	plateau[id_ligne][id_colonne] = piece;
    }

    /**
     * Prend deux identifiants de pièce en paramètre, rend les points communs de
     * ces pièces
     * 
     * @param p1
     *            l'identifiant de la première pièce, sous la forme
     *            0x0[id_pièce].
     * @param p2
     *            l'identifiant de la seconde pièce, même forme que p1
     * @return les points commus de ces deux pièces, de manière à ce que chaque
     *         bit représentant une caractéristique de la pièce soit à 1 si les
     *         identifiants ont cette caractéristique en commun
     * @see get_piece
     * @see points_communs_double_piece
     * @see test_colonne
     * @see test_diagonale
     ***/
    public byte points_communs(byte p1, byte p2) {
	int pts_communs = p1 ^ p2;
	pts_communs = ~pts_communs;
	pts_communs = 0x0F & pts_communs;
	return (byte) pts_communs;
    }

    /// id_colonne id_ligne < 3
    /**
     * Méthode testant si un carré contient quatre pièces du plateau ont une
     * caractéristique en commun
     * 
     * @param id_colonne
     *            l'identifiant de la colonne du point le plus en haut à gauche
     *            du carré à tester (vers A1). Strictement inférieur à 3
     * @param id_ligne
     *            l'identifiant de la ligne du point le plus en haut à gauche du
     *            carré à tester (vers A1). Strictement inférieur à 3
     * @return true si toutes les pièces du carré possèdent une caractéristique
     *         commun, false si elles n'en ont aucune (ou ne sont pas toutes
     *         posées)
     * @see test_ligne
     * @see test_diagonales
     * @see finDePartie
     ***/
    private boolean test_carre(byte id_colonne, byte id_ligne) {
	byte p1, p2, p3, p4;
	p1 = get_piece(id_colonne, id_ligne);
	p2 = get_piece((byte) (id_colonne + 1), id_ligne);
	p3 = get_piece(id_colonne, (byte) (id_ligne + 1));
	p4 = get_piece((byte) (id_colonne + 1), (byte) (id_ligne + 1));

	boolean no_piece_vide = (p1 != -1 && p2 != -1 && p3 != -1 && p4 != -1),
	    points_communs = ((points_communs(p1, p3) & points_communs(p1, p2) & points_communs(p3, p4)) != 0);

	return no_piece_vide && points_communs;
    }

    /**
     * Méthode testant si les pièces d'une ligne possèdent un attribut en commun
     * 
     * @param id_ligne
     *            la ligne à tester
     * @return true si toutes les pièces posées sur la ligne en question
     *         possèdent un point commun, false si aucun point commun ou toutes
     *         les pièces ne sont pas posées
     * @see test_carre
     * @see test_diagonales
     * @see finDePartie
     * @see test_colonne
     ***/
    private boolean test_ligne(byte id_ligne) {
	byte p1, p2, p3, p4;
	p1 = plateau[id_ligne][0];
	p2 = plateau[id_ligne][1];
	p3 = plateau[id_ligne][2];
	p4 = plateau[id_ligne][3];

	return (p1 != -1 && p2 != -1 && p3 != -1 && p4 != -1)
	    && (points_communs(p1, p2) & points_communs(p3, p4) & points_communs(p1, p3)) != 0;
    }

    /**
     * Méthode testant si les pièces des deux diagonales possèdent des attributs
     * en commun, pour chaque diagonale
     * 
     * @return true si toutes les pièces posées sur une des deux diagonales
     *         possèdent (au moins) un point commun, false si aucun point commun
     *         ou toutes les pièces ne sont pas posées
     * @see test_carre
     * @see test_lgine
     * @see finDePartie
     * @see test_colonne
     ***/
    private boolean test_diagonales() {
	byte[] g = new byte[4], d = new byte[4];

	boolean g_false = false, d_false = false;

	for (int i = 0; i < 4; i++) {
	    g[i] = get_piece((byte) i, (byte) i);
	    d[i] = get_piece((byte) (3 - i), (byte) (3 - i));

	    if (g[i] == -1)
		g_false = true;

	    if (d[i] == -1)
		d_false = true;

	    if (g_false && d_false)
		return false;

	}

	return (((!g_false)
		 && (points_communs(g[0], g[3]) & points_communs(g[0], g[1]) & points_communs(g[3], g[2])) != 0))
	    || ((!d_false) && ((points_communs(d[0], d[1]) & points_communs(d[0], d[3])
				& points_communs(d[2], d[3])) != 0));

    }

    /**
     * Méthode testant si les pièces d'une colonne possèdent un attribut en
     * commun
     * 
     * @param colonne
     *            la colonne à tester
     * @return true si toutes les pièces posées sur la colonne en question
     *         possèdent un point commun, false si aucun point commun ou toutes
     *         les pièces ne sont pas posées
     * @see test_carre
     * @see test_diagonales
     * @see finDePartie
     * @see test_ligne
     **/
    private boolean test_colonne(byte colonne) {
	byte p1, p2, p3, p4;
	p1 = get_piece(colonne, (byte) 0);
	p2 = get_piece(colonne, (byte) 1);
	p3 = get_piece(colonne, (byte) 2);
	p4 = get_piece(colonne, (byte) 3);

	return (p1 != -1 && p2 != -1 && p3 != -1 && p4 != -1)
	    && ((points_communs(p1, p2) & points_communs(p3, p4) & points_communs(p1, p3)) != 0);
    }

    /**
     * Fonction déterminant si c'est le joueur 0 qui jouer
     * 
     * @return true si le joueur 0 doit faire une action, false si c'est le j1
     **/
    private boolean j0plays() {
	return (etat_du_tour >>> 1) % 2 == 0;
    }

    /**
     * Fonction déterminant quel type de coup est joué
     * 
     * @return true si le coup qui doit être joué est un don
     **/
    public boolean is_don() {
	return etat_du_tour % 2 == 0;
    }

    /**
     * Fonction jouant un dépôt de pièce
     * 
     * @param coup
     *            un coup valide (!), sous la forme [coordonnée, piece]
     * @see play
     * @see jouers
     * @see unsafe_jouer_don
     ***/
    private void unsafe_jouer_coup_depot(byte cp) {
	// 1. Dépot de la pièce

	int id_ligne = cp >>> 2;
	int id_colonne = 3 & cp;

	plateau[id_ligne][id_colonne] = piece_a_jouer;

	// 3. Changement du statut du tour : Le joueur venant de poser un pion
	// donne une autre pièce à l'adversaire : On change le 2e bit
	// de tourEtPiece.
	etat_du_tour = (byte) (etat_du_tour ^ 1);
    }

    /*
     * Aucun test. On part aussi du principe que piece est de la forme
     * 0x0(pdpiece)
     */
    private void unsafe_jouer_coup_don(byte p) {
	if (p == -1)
	    return;
	piece_a_jouer = p;
	etat_du_tour = (byte) (etat_du_tour ^ 3);
	indice_pieces = indice_pieces | (1 << p);
    }

    /********************
     * Méthodes utiles pour les tests
     *******************/

    /**
     * Renvoie la représentation sous un octet de la pièce mentionnée en
     * paramètre
     * 
     * @param strPiece
     *            la chaine de caractère associée à la pièce, ou la première
     *            lettre correspond à la couleur (r rouge/b bleu), la seconde
     *            FIXME
     * @return l'identifiant de la pièce associée à la str associée en paramètre
     ***/
    public static byte stringToPiece(String strPiece) {
	char[] idPiece = strPiece.toCharArray();

	// Pê il faudrait faire genre plutôt String.get(i)
	byte id_krq = 0x00;
	if (idPiece[0] == 'b') // b = bleu = blanc = 1
	    id_krq = 0x08; // 0b1000
	if (idPiece[1] == 'g') // g = grand = 1
	    id_krq = (byte) (0x04 ^ id_krq); // 0b0100
	if (idPiece[2] == 'p') // plèce pleine
	    id_krq = (byte) (0x02 ^ id_krq);
	if (idPiece[3] == 'c') // pièce carrée
	    id_krq = (byte) (0x01 ^ id_krq);

	return id_krq;
    }

    /**
     * Renvoie la représentation sous forme de chaine de caractères de la pièce
     * en paramètre
     * 
     * @param idPiece
     *            l'identifiant de la pièce.
     * @return la chaine de caractères associée à l'identifiant
     ***/
    public static String pieceToString(byte idPiece) {
	// Bleu/rouge, Grand/petit, Plein/troué, Rond/carré
	// Bleu = blanc, Rouge = noir
	if (idPiece == -1)
	    return "XXXX";

	char[] str = new char[4];

	if (idPiece % 2 == 0) // 0 = rond
	    str[3] = 'r';
	else
	    str[3] = 'c';

	if ((idPiece >>> 1) % 2 == 0) // 0 = troué
	    str[2] = 't';
	else
	    str[2] = 'p';

	if ((idPiece >>> 2) % 2 == 0) // 0 = petit
	    str[1] = 'p';
	else
	    str[1] = 'g';

	if ((idPiece >>> 3) % 2 == 0) // 0 = rouge
	    str[0] = 'r';
	else
	    str[0] = 'b';

	return new String(str);
    }

    /********************************************************
     *
     * Méthodes de PlateauJeu
     *
     ********************************************************/

    public ArrayList<CoupJeu> coupsPossibles(Joueur j) throws IllegalArgumentException {
	ArrayList<CoupJeu> ret = new ArrayList<CoupJeu>();

	// Si c'est pas le bon joueur
	if (j0plays() && j.equals(j1) || (!j0plays() && j.equals(j0)))
	    throw new IllegalArgumentException("CoupsPossibles : Mauvais joueur demandé (Joueur demandé = "
					       + j.toString() + ", Joueur jouant = " + joueur_jouant().toString() + ")\n");
		
	// 1. Liste des cases ou il est possible de poser la pièce donnée
	ArrayList<Integer> cases_libres = new ArrayList<>();
	for (int i = 0; i < 16; i++)
	    if (plateau[i / 4][i % 4] == -1)
		cases_libres.add(i);
		
	// 2. Liste des pieces restantes
	ArrayList<Integer> pieces_restantes = new ArrayList<>();
	for (int i = 0; i < 16; i++)
	    if ((indice_pieces >>> i) % 2 == 0)
		pieces_restantes.add(i);

	// 3. Combinaison de tout ça

	for (int i = 0; i < cases_libres.size(); i++)
	    for (int k = 0; k < pieces_restantes.size(); k++)
		ret.add(new DoubleCoupQuarto(cases_libres.get(i).byteValue(), pieces_restantes.get(k).byteValue()));

	return ret;
    }

    /**
    * Joue un doublecoupquarto, par le joueur j
    * @param j 
    *      le joueur voulant jouer un double coup quarto
    * @param dcq
    *      le doubleCoupQuarto joué
    ***/ 
    private void joue_double(Joueur j, DoubleCoupQuarto dcq) {
	joue(j, new CoupQuarto(dcq.get_coord(), false));

	joue(j, new CoupQuarto(dcq.get_piece(), true));
    }

    
    public void joue(Joueur j, CoupJeu cj) throws IllegalArgumentException {
	if (cj instanceof DoubleCoupQuarto) {
	    joue_double(j, (DoubleCoupQuarto) cj);
	    return;
	}

	// 1. vérification que c'est le bon joueur qui joue
	if (!coupValide(j, cj))
	    throw new IllegalArgumentException("joue() : Coup invalide");

	// On peut jouer le coup pusqu'il est valide
	CoupQuarto c = (CoupQuarto) cj;

	// 2. vérification du type du coup
	if (c.get_type()) {
	    unsafe_jouer_coup_don(c.get());
	} else { // C'est un dépôt
	    unsafe_jouer_coup_depot(c.get());
	}
    }

    /**
     * Retourne un nouveau plateau de jeu, qui est une copie du plateau présent
     * 
     * @return un plateau équivalent au plateau présent
     ***/
    public PlateauJeu copy() {
	byte[][] p = new byte[4][4];

	for (int i = 0; i < 4; i++)
	    for (int j = 0; j < 4; j++)
		p[i][j] = plateau[i][j];

	return new PlateauQuarto(p, piece_a_jouer, indice_pieces, etat_du_tour);
    }

    /**
     * Détermine si un doublecoupquarto est bien valide, id est les deux actions correspondants à 
     * ce coup sont elles-mêmes valides
     *
     **/
    private boolean coupValide_d(Joueur j, DoubleCoupQuarto dcq) {
	CoupQuarto cq1 = new CoupQuarto(dcq.get_coord(), false), cq2 = new CoupQuarto(dcq.get_piece(), true);

	PlateauQuarto pq = (PlateauQuarto) this.copy();
	pq.joue(j, cq1);
	return coupValide(j, cq1) && pq.coupValide(j, cq2);

    }

    public boolean coupValide(Joueur j, CoupJeu cj) {
	if (cj instanceof DoubleCoupQuarto)
	    return coupValide_d(j, (DoubleCoupQuarto) cj);

	CoupQuarto c = (CoupQuarto) cj;
	boolean c_type = c.get_type();

	int c_val = c.get();

	// vérification que le ccoup est moralement valide (bon type, etc)
	if (!j.equals(joueur_jouant()) && (c_type && (!is_don()) || (!c_type) && is_don()))
	    return false;

	return (c_type && (indice_pieces >>> c_val) % 2 == 0) || ((!c_type) && (get_piece((byte) c_val)) == -1);
    }


    public boolean finDePartie() {
	for (byte i = 0; i < 4; i++) {
	    /// Test des lignes
	    if (test_ligne(i) || test_colonne(i))
		return true;
	}

	/// Test des diagonales
	if (test_diagonales())
	    return true;

	/// Test des carrés
	for (byte i = 0; i < 3; i++)
	    for (byte j = 0; j < 3; j++)
		if (test_carre(i, j))
		    return true;

	/// Cas ou toutes les pièces ont été posées
	return indice_pieces == 0x0000FFFF;

    }

    /*********************
     *
     * Méthodes de Partiel
     *
     *********************/

    /**
     * Renvoie la chaine de caractères associée à l'identifiant d'une coordonnée
     * passée en paramètre
     * 
     * @param coordonnee_case
     *            l'identifiant de la case passé en paramètre
     * @return une chaine de caractères représentant une case, de la forme
     *         [A-D][1-4], ou la lettre représente la colonne, le chiffre la
     *         ligne
     */
    public static String coordToString(byte coordonnee_case) {
	// Ligne colonne
	int ind_ligne = coordonnee_case >>> 2;
	int ind_colonne = 0b011 & coordonnee_case;
	char[] c = new char[2];
	switch (ind_ligne) {
	case 0:
	    c[1] = '1';
	    break;
	case 1:
	    c[1] = '2';
	    break;
	case 2:
	    c[1] = '3';
	    break;
	case 3:
	    c[1] = '4';
	    break;
	}

	switch (ind_colonne) {
	case 0:
	    c[0] = 'A';
	    break;
	case 1:
	    c[0] = 'B';
	    break;
	case 2:
	    c[0] = 'C';
	    break;
	case 3:
	    c[0] = 'D';
	    break;
	}

	return new String(c);
    }

    
    /**
     * Donne si le dépôt "move" par le joueur représenté par la chaine "player"
     * est valide, ou non
     * 
     * @param move
     *            la chaine de caractères représentant une position du plateau
     * @param player
     *            chaîne représentant le joueur ("blanc" ou "noir")
     **/
    public boolean estmoveValide(String move, String player) {
	CoupQuarto cj = new CoupQuarto(move);
	Joueur j;

	if (player.equals("noir"))
	    j = j0;
	else
	    j = j1;

	return coupValide(j, cj);
    }


    /** 
    * Joue un double coup quarto, représenté par une string
    ***/
    public void play_dbstr(String str) {
	String[] splitted_str = str.split("-");
	// Str 0 -> la case
	// Str 1 -> la pièce donnée
	String j;
	if (j0plays())
	    j = j0.toString();
	else
	    j = j1.toString();
	play_double_coup(splitted_str[1], splitted_str[0], j);
    }

    // Joue a) le dépôt de la pièce à jouer sur la case move
    // b) le don de la pièce
    public void play_double_coup(String choose, String move, String player) {
	Joueur j;
	if (player.equals("blanc"))
	    j = j1;
	else
	    j = j0;

	/// A - dépôt
	byte coord = stringToCoord(move);
	CoupQuarto cq = new CoupQuarto(move);
	joue(j, cq);
	// B : Don
	byte id_piece = stringToPiece(choose);
	CoupQuarto cq_bis = new CoupQuarto(choose);
	joue(j, cq);
    }

    /**
     * Transfome une chaîne de caractères représentant une coordonnée en un identifiant de coordonneés
     * @param str 
     *          la chaîne de caractères représentant une coordonnée, de la forme [A-D][1-4]
     * @return un identifiant de coordonnée
     **/
    public static byte stringToCoord(String str) {
	char c1 = str.charAt(0), c2 = str.charAt(1);
	int ret = 0;

	// ligne
	switch (c2) {

	case '1':
	    break;
	case '2':
	    ret = 0b0000_01_00;
	    break;
	case '3':
	    ret = 0b0000_10_00;
	    break;
	case '4':
	    ret = 0b0000_11_00;
	    break;
	}

	switch (c1) {
	case 'A':
	    break;
	case 'B':
	    ret = ret ^ 0b0000_00_01;
	    break;
	case 'C':
	    ret = ret ^ 0b0000_00_10;
	    break;
	case 'D':
	    ret = ret ^ 0b0000_00_11;
	    break;
	}

	return (byte) ret;
    }

    /** Getter de j0 **/
    public Joueur getJ0() {
	return j0;
    }

    /** Getter de j1 **/
    public Joueur getJ1() {
	return j1;
    }

    /*********** Méthodes de Partiel **************/

    /**
     * Prend une chaine de caractères correspondant à un don de pièce.
     *
     * @param choix
     *            une string représentant une pièce, donnée à l'autre joueur
     * @param joueur
     *            le joueur donnant la pièce
     * @return true si la pièce peut être donnée/n'a pas encore été posée,
     **/
    public boolean estchoixValide(String choix, String joueur) {
	if (choix.length() != 4)
	    return false;
	boolean b; // FIXME
	return estmoveValide(choix, joueur);
    }

    private String str_pieces_non_jouees() {
	String ret = "";
	int idp = indice_pieces;
	for (byte i = 0; i < 16; i++) {
	    if (idp % 2 == 0)
		ret = ret + " " + pieceToString(i) + " , ";
	    idp = idp >>> 1;
	}
	return ret;
    }

    /**
     * Retourne le nombre de pièce restante pour chaque caractéristique
     * 
     * globale : indice 0 troue : indice 1 non troue : indice 2 grand : indice 3
     * petit : indice 4 rond : indice 5 carre : indice 6 rouge : indice 7 bleu :
     * indice 8
     * 
     * @return nombre de pièce restante par caractéristique
     **/
    public int[] nb_piece_restante() {
	int[] res = new int[9];

	int idp = indice_pieces;

	for (byte i = 0; i < 16; i++) {
	    if (idp % 2 == 0) {
		if ((i >>> 1) % 2 == 0)
		    res[1]++;

		if ((i >>> 1) % 2 != 0)
		    res[2]++;

		if ((i >>> 2) % 2 != 0)
		    res[3]++;

		if ((i >>> 2) % 2 == 0)
		    res[4]++;

		if ((i >>> 3) % 2 == 0)
		    res[5]++;

		if ((i >>> 3) % 2 != 0)
		    res[6]++;

		if (i % 2 == 0)
		    res[7]++;

		if (i % 2 != 0)
		    res[8]++;
	    }

	    idp = idp >>> 1;
	}
		
	for (int i=1; i<9; i++)
            res[0] += res[i];

	return res;
    }

    /**
     * Retourne un tableau avec vrai si il existe une position de dépôt gagnante
     * sur le plateau pour chaque caractéristique
     * 
     * globale : indice 0 troue : indice 1 non troue : indice 2 grand : indice 3
     * petit : indice 4 rond : indice 5 carre : indice 6 rouge : indice 7 bleu :
     * indice 8
     * 
     * @return nombre de pièce restante par caractéristique
     **/
    public int[] nb_position_gagnante() {
	int[] res = new int[9];

		for (int j = 1; j < 9; j++) {
		    byte pieceTemp = 0;
		    
		    switch (j) {
		    case 1:
		        pieceTemp = PlateauQuarto.stringToPiece("rgtc");
			break;

		    case 2:
		        pieceTemp = PlateauQuarto.stringToPiece("rgpc");
			break;

		    case 3:
		        pieceTemp = PlateauQuarto.stringToPiece("rgtc");
			break;

		    case 4:
		        pieceTemp = PlateauQuarto.stringToPiece("bptr");
			break;

		    case 5:
		        pieceTemp = PlateauQuarto.stringToPiece("rptr");
			break;

		    case 6:
		        pieceTemp = PlateauQuarto.stringToPiece("rgtc");
			break;

		    case 7:
		        pieceTemp = PlateauQuarto.stringToPiece("rgtc");
			break;

		    case 8:
		        pieceTemp = PlateauQuarto.stringToPiece("bptr");
			break;
		    }

		    if (existe_position_gagnante(pieceTemp)) {
			res[0]++;
			res[j]++;
		    }
	}
	return res;
    }
    public String toString() {
	String joueur;
	if (j0plays())
	    joueur = "noir";
	else
	    joueur = "blanc";
	String ret = "\t A \t B \t C \t D \n";
	for (int l = 0; l < 4; l++) { // ligne
	    ret = ret + (l + 1) + "\t";
	    for (int c = 0; c < 4; c++) { // colonne

		ret = ret + pieceToString(plateau[l][c]) + "\t";

	    }
	    ret = ret + (l + 1) + "\n";
	}
	ret = ret + "Le joueur " + joueur + " doit ";
	if (!is_don())
	    ret = ret + "poser la pièce " + pieceToString(piece_a_jouer) + " \n";
	else
	    ret = ret + "donner une pièce, comprise dans la liste suivante :" + str_pieces_non_jouees() + "\n";
	return ret;
    }

    /**
     * @return le joueur qui doit accomplir une action
     */
    public Joueur joueur_jouant() {
	if (j0plays())
	    return j0;
	else
	    return j1;
    }

    /**
    * Rend la chaîne de caractères associée au joueur jouant
    **/ 
    public String getStrCurrentPlayer() {
	if (j0plays())
	    return "Blanc";
	else
	    return "Noir";
    }
}
