package jeux.awale;

import iia.jeux.modele.CoupJeu;

public class CoupAwale implements CoupJeu {
	private int numJoueur;
	private int numCase;

	// INDICES DES JOUEURS
	public CoupAwale(int numeroJoueur, int numeroCase) {
		numJoueur = numeroJoueur;
		numCase = numeroCase;
	}

	public int getNumJoueur() {
		return numJoueur;
	}

	public int getNumCase() {
		return numCase;
	}

	public String toString() {
		return ("(" + (numJoueur + 1) + "," + numCase + ")");
	}
}
