package jeux.quarto;

import iia.jeux.modele.*;
import iia.jeux.modele.joueur.Joueur;

import java.io.PrintStream;
import java.util.ArrayList;


public class PlateauQuarto implements PlateauJeu, Partie1 {
    private unsigned long plateau;
    private unsigned short indCases;
    private unsigned short indPiece;
    private unsigned byte tourEtPiece;

    /* AUCUN TEST. On part du principe que le coup est valide */
    private void unsafe_jouerCoup(byte coup){
	byte piece = (coup & 0x0F), coord = coup >> 4;
	plateau = plateau | (piece << (4*coord));
    }

    
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


    
