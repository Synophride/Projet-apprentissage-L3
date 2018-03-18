package jeux.quarto;

import iia.jeux.modele.*;
import iia.jeux.modele.joueur.Joueur;

import java.io.PrintStream;
import java.util.ArrayList;

public class PlateauQuarto implements PlateauJeu{
 
    /// Note opérateurs logiques sur les entiers
    ///    ^ -> XOR
    ///    | -> OR
    ///    & -> AND

    /// << -> Shift G
    /// >>> -> shift D logique.  !! NE PAS UTILISER '>>' !! 

    /************* Attributs *******************/
    
    // au lieu de faire j1 et j2, on appelle les joueurs j0 et j1.
    public static Joueur j0; 
    public static Joueur j1; 
    
    /// Apparemment, pas d'entiers non-signés en java
    
    // Plateau de jeu
    private long plateau;

    // Montre quelles pièces sont jouées.
    private short indCases; 
    private short indPiece; // On marque la pièce comme jouée lors du don de la
    
    // id_joueur id_actio 00 id_piexe
    private byte tourEtPiece;

    /************ Méthodes privées ****************/
    
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
	tourEtPiece = (byte) (0xC0^ tourEtPiece);
    }

    /**********  Méthodes de PlateauJeu *********/
    
    // Apparemment pas besoin de vérifier que c'est le bon joueur qui demande. On devrait pê faire une fonction genre "joueur jouant" ou quelque chose comme ça
    public ArrayList<CoupJeu> coupsPossibles(Joueur j) {
	
	byte etat_jeu = (byte) (tourEtPiece >>> 6); 
	// traduction : c'est le tour du joueur 0 (étant donné que le truc est de la forme 0b0X, donc <2) 
	if(etat_jeu < 2){
	    if(j.equals(j1))
		return null;
	    // Sinon, le joueur 2 joue.
	    if(etat_jeu == 0){ // Il faut donner la pièce
		for(byte  i = 0; i<16; i++){
		    if ( (indPiece >>> i) %2  == 0)
			;
		}
	    }
	    
	    


	} else {
	    
	}
	return null;
    }

    public void joue(Joueur j, CoupJeu cj){

    }

    public boolean finDePartie(){
	return false;
    }

    public  PlateauJeu copy(){
	return null;
    }

    public  boolean coupValide(Joueur j, CoupJeu cj){
	return false;
    }

    
    /********** Méthodes de Partiel **************/

    public void setFromFile(String fileName){
	
    }
    
    public void saveToFile(String fileName){
	return;
    }

    public boolean estmoveValide(String move, String player){
	return false;
    }

    public String[] mouvementsPossibles(String player){
	return null;
    }

    public void play(String move, String player){

    }


}


    
