package jeux.quarto;

import iia.jeux.modele.*;
import iia.jeux.modele.joueur.Joueur;

import java.io.PrintStream;
import java.util.ArrayList;
public class PlateauQuarto implements PlateauJeu, Partie1 {
 
    /// Note opérateurs logiques sur les entiers
    ///    ^ -> XOR
    ///    | -> OR
    ///    & -> AND

    /// << -> Shift G
    /// >>> -> shift D logique.  !! NE PAS UTILISER '>>' !! 




    /************* Attributs *******************/

    public static Joueur j1;
    public static Joueur j2;

    /// Apparemment, pas d'entiers non-signés en java
    
    // Plateau de jeu
    private long plateau;

    // Montre quelles pièces sont jouées.
    private short indCases;
    private short indPiece;
    
    // id_joueur id_actio 00 id_piexe
    private byte tourEtPiece;

    /************ Méthodes privées ****************/
    
    /* AUCUN TEST. On part du principe que le coup est valide */
    private void unsafe_jouer_coup_depot(byte coup){
	// 1. Dépot de la pièce
	byte piece = (coup & 0x0F), coord = coup >>> 4;
	plateau = plateau | (piece << (4*coord));
	
	// 2. Changement du statut du tour : Le joueur venant de poser un pion
	//    donne une autre pièce à l'adversaire : On change le 2e bit
	//    de tourEtPiece.
	tourEtPiece = 0b01000000^tourEtPiece;
    }

    
    /**********  Méthodes de PlateauJeu *********/

    public ArrayList<CoupJeu> coupsPossibles(Joueur j){
	
    }

    public void joue(Joueur j, CoupJeu cj){

    }

    public boolean finDePartie(){

    }

    public  PlateauJeu copy(){

    }

    public  boolean coupValide(Joueur j, CoupJeu cj){
	
    }

    
    /********** Méthodes de Partiel **************/

    public void setFromFile(String fileName){
	
    }
    
    public void saveToFile(String fileName){
	
    }

    public boolean estmoveValide(String move, String player){
	
    }

    public String[] mouvementsPossibles(String player){

    }

    public void play(String move, String player){

    }

    public boolean finDePartie(){

    }

}


    
