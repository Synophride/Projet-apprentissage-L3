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

    private static int prof_blanc = 1;
    private static int prof_noir = 2;

    private static AlgoJeu algoJoueur[] = new AlgoJeu[2];

    private static PlateauQuarto p = new PlateauQuarto(joueur_noir, joueur_blanc);

    public static void init() {
        algoJoueur[0] = new AlphaBeta(HeuristiqueQuarto.heuristique1_j1, joueur_noir, joueur_blanc, prof_noir);

        algoJoueur[1] = new Minimax(HeuristiqueQuarto.heuristique_aleatoire, joueur_blanc, joueur_noir, prof_blanc);
    }

    public static void main_ia_vs_ia(String[] args) throws IOException {
        init();
        // BLANC = 0
        // NOIR = 1

        Scanner input = new Scanner(System.in);
        
        int joueur_courant = 0;
        int nb_de_partie = 10;
        
        int victoire_noir = 0;
        int victoire_blanc = 0;

        for (int i = 0; i < nb_de_partie; i++) {
            
            PlateauQuarto p = new PlateauQuarto(joueur_noir, joueur_blanc);
            
            System.out.println(p.toString());
            
            while (!p.finDePartie()) {
		
                Joueur joueur_jouant = p.joueur_jouant();
		
                if (joueur_jouant.equals(joueur_noir))
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
		
                System.out.println(p.toString());
		
            }
            
            System.out.println("La partie est finie\nPlateau :\n" + p.toString());
	    
            if (p.joueur_jouant().equals(joueur_noir)) {
                System.out.println("Le joueur blanc gagne !");
                victoire_blanc++;
            } else {
                System.out.println("Le joueur noir gagne !");
                victoire_noir++;
            }
        }
	
        System.out.println("\nNoir : " + victoire_noir + "\nBlanc : " + victoire_blanc );
        input.close();
    }

    public static void main(String[] args) throws IOException {
        main_ia_vs_ia(args);
    }

}
