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
            
            /*
             * Heuristique tj calculé après un don de pièce
             */
          
            // Nombre de pièces restantes pour chaque caractéristique
            int[] nb_piece = p.nb_piece_restante();
            
            // Vrai si il existe une position où le dépôt d'une pièce rendrait le plateau gagnant pour chaque
            int[] existe_position_gagnante = p.nb_position_gagnante();
            
             if(p.finDePartie()) {
            		 return Integer.MAX_VALUE - 1;	 
             }
             
             for(int i=1; i<9; i++) {
                 if(nb_piece[i] == nb_piece[0] && existe_position_gagnante[i] >= 1) {
                		 return Integer.MAX_VALUE - 1;
                 }
             }
            
            int val = 0;
            
            for(int i=1; i<9; i++) {
                if(nb_piece[0] > 0) {
                    if(nb_piece[i] > 0 && existe_position_gagnante[i] >= 1) {
                        if((nb_piece[0] - nb_piece[i])%2 == 0)
                            val += (nb_piece[i]*existe_position_gagnante[i]*20) - (nb_piece[0] - nb_piece[i])*10;
                        else
                            val -= (nb_piece[i]*existe_position_gagnante[i]*20) - (nb_piece[0] - nb_piece[i])*10;
                    }
                }
            }
            
            Random rand = new Random();
            
            if (val == 0) {
                val = rand.nextInt(50);
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
    
    public static Heuristique heuristique1 = new Heuristique() {
        public int eval(PlateauJeu plateau, Joueur j) {
            return 1;
        }
    };
}
