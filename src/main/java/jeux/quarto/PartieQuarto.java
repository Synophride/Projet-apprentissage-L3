package jeux.quarto;

import iia.jeux.alg.AlgoJeu;
import iia.jeux.alg.Minimax;
import iia.jeux.alg.AlphaBeta;
import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import jeux.dominos.PlateauDominos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PartieQuarto {
    public static Joueur noir = new Joueur("noir");
    public static Joueur blanc= new Joueur("blanc");
    
    public static void main(String[] args) throws IOException {
        PlateauQuarto p = new PlateauQuarto(noir, blanc);
	Scanner input = new Scanner(System.in);
        while(! p.finDePartie() ){
	    System.out.println( p.toString() );
	    String str = input.nextLine();
	    try{
		p.play(str, p.getStrCurrentPlayer());
	    } catch (Exception e ){
		System.out.println("le coup est invalide");
	    }
	}
	System.out.println("La partie est finie");
	input.close();
    }
    
}
