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

    public static void main(String[] args) throws IOException {
        
        AlgoJeu AlgoJoueurChoixPiece[] = new AlgoJeu[2];
        AlgoJeu AlgoJoueurChoixPosition[] = new AlgoJeu[2];
        
        AlgoJoueurChoixPiece[0] = new Minimax(HeuristiqueQuarto.heuristique_aleatoire, joueur_noir, joueur_blanc, prof_blanc);
        AlgoJoueurChoixPiece[1] = new Minimax(HeuristiqueQuarto.heuristique_aleatoire, joueur_blanc, joueur_noir, prof_noir);
        
        AlgoJoueurChoixPosition[0] = new Minimax(HeuristiqueQuarto.heuristique_aleatoire, joueur_noir, joueur_blanc, prof_blanc);
        AlgoJoueurChoixPosition[1] = new Minimax(HeuristiqueQuarto.heuristique_aleatoire, joueur_blanc, joueur_noir, prof_noir);
        
        PlateauQuarto p = new PlateauQuarto(joueur_noir, joueur_blanc);
        
        Scanner input = new Scanner(System.in);
        while (!p.finDePartie()) {
            System.out.println(p.toString());
            String str = input.nextLine();
            try {
                p.play(str, p.getStrCurrentPlayer());
            } catch (Exception e) {
                System.out.println("le coup est invalide");
            }
        }
        System.out.println("La partie est finie");
        input.close();
    }
}
