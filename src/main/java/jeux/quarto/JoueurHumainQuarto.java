package jeux.quarto;

import iia.jeux.alg.AlgoJeu;
import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;

import java.util.Scanner;

public class JoueurHumainQuarto implements AlgoJeu {
    private Scanner input= new Scanner(System.in);
    
    public CoupJeu meilleurCoup(PlateauJeu p){
	System.out.println( "Etat du plateau :\n" + p.toString() );
	String s = input.nextLine();
	return new DoubleCoupQuarto(s);
    }
}

