package jeux.awale;

import iia.jeux.alg.AlgoJeu;
import iia.jeux.alg.Minimax;
import iia.jeux.alg.AlphaBeta;
import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import java.util.ArrayList;
import jeux.awale.*;

public class PartieAwale {
	private static int prof_blanc = 2;
	private static int prof_noir = 2;

	public static void main(String[] args) {

		Joueur j1 = new Joueur("Blanc");
		Joueur j2 = new Joueur("Noir");

		Joueur[] tab_joueurs = new Joueur[2];
		tab_joueurs[0] = j1;
		tab_joueurs[1] = j2;

		AlgoJeu AlgoJoueur[] = new AlgoJeu[2];

		// Note : remplacer les paramètres
		AlgoJoueur[0] = new AlphaBeta(HeuristiqueAwale.heuristique1_j1, j1, j2, prof_blanc);
		AlgoJoueur[1] = new Minimax(HeuristiqueAwale.heuristique1_j2, j2, j1, prof_noir);

		System.out.println("TD IIA n°4");

		boolean jeu_fini = false;
		CoupJeu meilleur_coup = null;
		int joueur_jouant;
		PlateauAwale plateau = new PlateauAwale();
		PlateauAwale.setJoueurs(j1, j2);
		joueur_jouant = 0;

		while (!jeu_fini) {
			System.out.println(plateau.toString());
			System.out.println("C'est au joueur " + tab_joueurs[joueur_jouant].toString() + "de jouer");

			ArrayList<CoupJeu> coups_possibles = plateau.coupsPossibles(tab_joueurs[joueur_jouant]);

			if (coups_possibles.size() > 0 && (!plateau.finDePartie())) {

				// Lancement de l'algo de recherche du meilleur coup
				System.out.println("Recherche du meilleur coup avec l'algo " + AlgoJoueur[joueur_jouant]);
				meilleur_coup = AlgoJoueur[joueur_jouant].meilleurCoup(plateau);

				System.out.println("Coup joué : " + meilleur_coup + " par le joueur " + tab_joueurs[joueur_jouant]);

				plateau.joue(tab_joueurs[joueur_jouant], meilleur_coup);

				// Le coup est effectivement joué
				joueur_jouant = 1 - joueur_jouant;

			} else {
				// Récupération des différentes valeurs
				if (coups_possibles.size() == 0)
					plateau.cantPlay(tab_joueurs[joueur_jouant]);

				System.out.println("Le joueur " + tab_joueurs[joueur_jouant] + " ne peut plus jouer et abandonne !");
				System.out.println("Le joueur " + plateau.getWinner() + " a gagné cette partie !");
				jeu_fini = true;
			}
		}
	}
}
