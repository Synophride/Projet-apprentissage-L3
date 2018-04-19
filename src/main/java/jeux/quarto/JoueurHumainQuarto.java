package quarto;

import iia.jeux.alg.AlgoJeu;

import java.util.Scanner;

public class JoueurHumainQuarto implements AlgoJeu {
    private Scanner input= new Scanner(System.in);
    
    public CoupJeu meilleurCoup(PlateauJeu p){
	PlateauQuarto pq = (PlateauQuarto) p;
	System.out.prinln( "Etat du plateau :\n" + pq.toString() );
	String s = input.nextLine();
	return DoubleCoupQuarto(s);
    }
    
}

