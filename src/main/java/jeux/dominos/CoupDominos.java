package jeux.dominos;

import iia.jeux.modele.CoupJeu;

public class CoupDominos implements CoupJeu {

	/****** Attributs *******/
	private int ligne;

	private int colonne;

	/****** Constructeur *******/

	public CoupDominos(int l, int c) {
		ligne = l;
		colonne = c;
	}

	/****** Accesseurs *******/

	public int getLigne() {
		return ligne;
	}

	public int getColonne() {
		return colonne;
	}

	/****** Accesseurs *******/

	public String toString() {
		return "(" + ligne + "," + colonne + ")";
	}
}
