package jeux.quarto;

import iia.jeux.alg.AlgoJeu;
import iia.jeux.alg.Minimax;
import iia.jeux.alg.AlphaBeta;
import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import jeux.awale.HeuristiqueAwale;
import jeux.dominos.PlateauDominos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PartieQuarto {
    public static Joueur joueur_noir = new Joueur("noir");
    public static Joueur joueur_blanc = new Joueur("blanc");
    
    private static int prof_blanc = 2;
    private static int prof_noir = 2;

    private static AlgoJeu algoJoueur[] = new AlgoJeu[2];
        
    private static PlateauQuarto p = new PlateauQuarto(joueur_noir, joueur_blanc);
    
    
    public static void init(){
	algoJoueur[0] =
	    new Minimax(HeuristiqueQuarto.heuristique1_j1,
			joueur_noir,
			joueur_blanc,
			prof_blanc);

	algoJoueur[1] =
	    new Minimax(HeuristiqueQuarto.heuristique_aleatoire,
			joueur_blanc,
			joueur_noir,
			prof_noir);
            }
    
    public static void main_ia_vs_ia(String[] args) throws IOException {
	init();
	// BLANC = 0
	// NOIR  = 1
	
        Scanner input = new Scanner(System.in);
	int joueur_courant = 0;
	
        while (!p.finDePartie()) {
            System.out.println(p.toString());
	    
	    Joueur joueur_jouant = p.joueur_jouant();
	    
	    if(joueur_jouant.equals(joueur_noir) )
		joueur_courant = 0;
	    else
		joueur_courant = 1;
	    
            CoupJeu meilleur_coup = null;
	    
	    meilleur_coup = algoJoueur[joueur_courant].meilleurCoup(p);
	    
            System.out.println("choix : " + meilleur_coup.toString() + "\n");
            
            // String str = input.nextLine();
            
            try {
                p.joue(p.joueur_jouant(), meilleur_coup);
            } catch (Exception e) {
                System.out.println("le coup est invalide");
            }
        }
        
        System.out.println("La partie est finie");
        input.close();
    }

    public static void main(String [] args) throws IOException {
	main_ia_vs_ia(args);
    }
    
}
