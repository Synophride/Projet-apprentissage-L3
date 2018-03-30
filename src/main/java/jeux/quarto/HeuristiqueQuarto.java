package jeux.quarto;

import iia.jeux.alg.Heuristique;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import jeux.awale.PlateauAwale;

import java.util.Random;

public class HeuristiqueQuarto {
    public static Heuristique heuristique_aleatoire = new Heuristique() {
        public int eval(PlateauJeu plateau, Joueur j) {
            Random rand = new Random();
            return rand.nextInt();
        }
    };
}
