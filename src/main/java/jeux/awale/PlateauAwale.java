package jeux.awale;
//mvn install && java -cp target/iia.td3-4_Devatine_Guyot-1.0.jar jeux.awale.PartieAwale

import iia.jeux.modele.*;
import iia.jeux.modele.joueur.Joueur;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class PlateauAwale implements PlateauJeu {
	/*
	 * Note : Pour l'heuristique, on pourrait stocker "l'avancement" de la partie,
	 * pour lancer des heuristiques différentes suivant si on est au début, au
	 * milieu, à la fin de la partie
	 */

	/*
	 * 0 1 2 3 4 5 6 J2 1 J1 0
	 */
	public static final int NB_CASES_LARGEUR = 6;
	private int[][] plateau;
	private static Joueur j1;
	private static Joueur j2;
	private int nbGrainesJ1 = 0;
	private int nbGrainesJ2 = 0;
	private static final int idJ1 = 0, idJ2 = 1;

	public PlateauAwale() {
		plateau = new int[2][NB_CASES_LARGEUR];
		for (int i = 0; i < plateau.length; i++)
			for (int j = 0; j < plateau[i].length; j++)
				plateau[i][j] = 4;
	}

	public PlateauAwale(int[][] terrain, int grainesJ1, int grainesJ2) {
		plateau = new int[terrain.length][terrain[0].length];
		for (int i = 0; i < terrain.length; i++)
			for (int j = 0; j < terrain[i].length; j++)
				plateau[i][j] = terrain[i][j];

		nbGrainesJ1 = grainesJ1;
		nbGrainesJ2 = grainesJ2;
	}

	public static void setJoueurs(Joueur jblanc, Joueur jnoir) {
		j1 = jblanc;
		j2 = jnoir;
	}

	/***************** Implémentation de PlateauJeu *******************/

	public int nbGraineCase(Joueur j, int idCase) {
		int nb_j = 1;

		if (j.equals(j1))
			nb_j = 0;

		return plateau[nb_j][idCase];
	}

	public Joueur autreJoueur(Joueur j) {
		if (j.equals(j1))
			return j2;
		else
			return j1;
	}

	public PlateauJeu copy() {
		return new PlateauAwale(plateau, nbGrainesJ1, nbGrainesJ2);
	}

	public void joue(Joueur j, CoupJeu c) {
		CoupAwale coup = (CoupAwale) c;
		// 0 : Vérification que le coup est légal
		// ? Lancer une exception ou ne rien faire ?
		/////////////

		///////////
		if (!coupValide(j, coup))
			return;

		// 1 : Dépôt des graines dans les cases
		int nb_j = coup.getNumJoueur(), case_ = coup.getNumCase(), nb_graines = plateau[nb_j][case_], cpt_case = case_,
				cpt_joueur = nb_j;

		plateau[nb_j][case_] = 0;

		while (nb_graines > 0) {
			int[] coord_case_pose = nextCase(cpt_joueur, cpt_case);
			cpt_joueur = coord_case_pose[0];
			cpt_case = coord_case_pose[1];

			if (cpt_joueur == nb_j && case_ == cpt_case)
				continue;

			plateau[cpt_joueur][cpt_case]++;
			nb_graines--;
		}

		int[] prevC = this.prevCase(cpt_joueur, cpt_case);
		cpt_joueur = prevC[0];
		cpt_case = prevC[1];

		// 2 : Enlèvement des graines si on est dans le camp qui vient pas de jouer +
		// qu'il y en a deux ou trois

		PlateauAwale etat_plateau = (PlateauAwale) this.copy();
		while (cpt_joueur != nb_j && (plateau[cpt_joueur][cpt_case] == 2 || plateau[cpt_joueur][cpt_case] == 3)) {

			System.out.println(this.toString());
			if (nb_j == 0) {
				nbGrainesJ1 = nbGrainesJ1 + plateau[cpt_joueur][cpt_case];
			} else {
				nbGrainesJ2 = nbGrainesJ2 + plateau[cpt_joueur][cpt_case];
			}

			plateau[cpt_joueur][cpt_case] = 0;

			// Puis on regarde la case d'avant
			int[] case_precedente = this.prevCase(cpt_joueur, cpt_case);
			cpt_joueur = case_precedente[0];
			cpt_case = case_precedente[1];
		}
		if (hasNothing((nb_j + 1) % 2)) {
			this.plateau = etat_plateau.plateau;
			this.nbGrainesJ1 = etat_plateau.nbGrainesJ1;
			this.nbGrainesJ2 = etat_plateau.nbGrainesJ2;
		}

	}

	public boolean finDePartie() {
		return ((coupsPossibles(j1).size() == 0 && coupsPossibles(j2).size() == 0) || nbGrainesJ1 > 24
				|| nbGrainesJ2 > 24 || this.nb_graines_restantes() <= 6);
	}

	public int nb_graines_restantes() {
		int res = 0;
		for (int[] i : plateau)
			for (int j : i)
				res += j;
		return res;
	}

	public Joueur getWinner() {
		if (nbGrainesJ1 > nbGrainesJ2) {
			return j1;
		} else
			return j2;
	}

	public ArrayList<CoupJeu> coupsPossibles(Joueur j) {
		ArrayList<CoupJeu> coups_possibles = new ArrayList<CoupJeu>();
		int id;
		if (j.equals(j1)) {
			id = idJ1;
		} else
			id = idJ2;

		for (int i = 0; i < NB_CASES_LARGEUR; i++) {
			CoupAwale cp = new CoupAwale(id, i);
			if (this.coupValide(j, cp))
				coups_possibles.add(cp);
		}
		return coups_possibles;
	}

	public int[] getNbGraines() {
		int[] ret = new int[2];
		ret[0] = nbGrainesJ1;
		ret[1] = nbGrainesJ2;
		return ret;
	}

	/* ok */
	public boolean coupValide(Joueur j, CoupJeu c) {
		CoupAwale coup = (CoupAwale) c;
		int num_joueur = coup.getNumJoueur();
		int id_case = coup.getNumCase();
		int ind_joueur;

		if (j1.equals(j)) {
			ind_joueur = 0;
		} else
			ind_joueur = 1;

		// Si le joueur tente de jouer à la place de l'autre j.
		if (ind_joueur != num_joueur)
			return false;

		// Le joueur tente de jouer une case vide
		int nb_graines_sur_case = plateau[ind_joueur][id_case];
		if (nb_graines_sur_case == 0)
			return false;

		int autre_joueur;
		if (ind_joueur == 1)
			autre_joueur = 0;
		else
			autre_joueur = 1;

		if (!hasNothing(autre_joueur))
			return true;

		if (hasNothing(autre_joueur) && (ind_joueur == 0 && (id_case + nb_graines_sur_case > NB_CASES_LARGEUR))
				|| (ind_joueur == 1 && (id_case - nb_graines_sur_case < 0)))
			return true;
		return false;
	}

	/*************** Fonctions internes ******************************/
	private boolean hasNothing(int idJoueur) {
		for (int i = 0; i < 6; i++)
			if (plateau[idJoueur][i] != 0)
				return false;
		return true;
	}

	public void cantPlay(Joueur j) {
		for (int[] i : plateau)
			for (int k : i)
				if (j.equals(j1))
					nbGrainesJ1 += k;
				else
					nbGrainesJ2 += k;
	}

	private int[] nextCase(int idJoueur, int idCase) {
		int[] ret = new int[2];
		if (idJoueur == 0) {
			// TODO : Voir comment définir le tableau
			if (idCase < NB_CASES_LARGEUR - 1) {
				ret[0] = idJoueur;
				ret[1] = idCase + 1;
			} else {
				ret[0] = idJ2;
				ret[1] = idCase;
			}
		} else {
			if (idCase > 0) {
				ret[0] = idJoueur;
				ret[1] = idCase - 1;
			} else {
				ret[0] = idJ1;
				ret[1] = idCase;
			}
		}
		return ret;
	}

	/** pas sûr **/
	private int[] prevCase(int idJoueur, int idCase) {
		int[] ret = new int[2];
		if (idJoueur == 1) {
			// TODO : Voir comment définir le tableau
			if (idCase < NB_CASES_LARGEUR - 1) {
				ret[0] = idJoueur;
				ret[1] = idCase + 1;
			} else {
				ret[0] = (idJoueur + 1) % 2;
				ret[1] = idCase;
			}
		} else {
			if (idCase > 0) {
				ret[0] = idJoueur;
				ret[1] = idCase - 1;
			} else {
				ret[0] = (idJoueur + 1) % 2;
				ret[1] = idCase;
			}
		}
		return ret;
	}

	private String str_of_terrain() {
		String ret = "J2 -";
		for (int i : plateau[1])
			ret = ret + "\t" + i;
		ret = ret + "\n J1-";
		for (int i : plateau[0])
			ret = ret + "\t" + i;
		return ret;
	}

	public String toString() {
		return ("J1 : " + nbGrainesJ1 + " graines \n" + "J2 : " + nbGrainesJ2 + " graines \n" + this.str_of_terrain());
	}
}
