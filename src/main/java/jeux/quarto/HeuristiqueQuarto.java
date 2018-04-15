package jeux.quarto;

import iia.jeux.alg.Heuristique;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import jeux.awale.PlateauAwale;

import java.util.Random;

public class HeuristiqueQuarto {
    public static Heuristique heuristique1_j1 = new Heuristique() {
        public int eval(PlateauJeu plateau, Joueur j) {
            PlateauQuarto p = (PlateauQuarto) plateau;
            
            // Valeur d'heuristique
            int val = 0;
            
            // Nombre de pièces restantes pour chaque caractéristique
            int[] nb_piece = p.nb_piece_restante();
            
            // Vrai si il existe une position où le dépôt d'une pièce rendrait le plateau gagnant pour chaque
            int[] existe_position_gagnante = p.nb_position_gagnante();
            
            // Comme l'algo joue puis évalue l'heuristique, cela veut dire que le coup était un dépôt
            if(p.is_don()) {
                if(p.finDePartie())
                    return Integer.MAX_VALUE;
                
                for(int i=1; i<9; i++) {
                    if(nb_piece[i] == nb_piece[0] && existe_position_gagnante[i] >= 1)
                        return Integer.MIN_VALUE;
                }
            }
            // Le coup qui a été joué est un don
            else {
                byte piece = p.piece_a_jouer;
                
                if(p.existe_position_gagnante(piece))
                    return Integer.MIN_VALUE;
                
                for(int i=1; i<9; i++) {
                    if(nb_piece[i] == nb_piece[0] && existe_position_gagnante[i] >= 1)
                        return Integer.MAX_VALUE;
                }
            }
            
            for(int i=1; i<9; i++) {
                if(nb_piece[0] > 0)
                    val += ((nb_piece[i]/nb_piece[0])*existe_position_gagnante[i]);
            }
            
            return val;
        }
    };

    public static Heuristique heuristique1_j2 = new Heuristique() {
        public int eval(PlateauJeu plateau, Joueur j) {
            return -heuristique1_j1.eval(plateau, j);
        }
    };
    
    public static Heuristique heuristique_aleatoire = new Heuristique() {
        public int eval(PlateauJeu plateau, Joueur j) {
            Random rand = new Random();
            return rand.nextInt();
        }
    };
}
