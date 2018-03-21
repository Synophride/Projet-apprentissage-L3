package jeux.x;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import java.util.ArrayList;

public class PlateauX implements PlateauJeu {

	/*
	 * Pour coder un nouveau jeu... il faut au minimum coder - Une classe PlateauX
	 * pour représenter l'état du "plateau" de ce jeu. Cette classe doit fournir le
	 * code des méthodes de l'interface PlateauJeu qui permettent de caractériser
	 * les règles du jeu Une classe CoupX qui
	 */

	public PlateauX() {
		/* A Faire */ }

	/* A Faire */

	public ArrayList<CoupJeu> coupsPossibles(Joueur j) {
		throw new UnsupportedOperationException("Il vous faut coder cette méthode");
	}

	public void joue(Joueur j, CoupJeu c) {
		throw new UnsupportedOperationException("Il vous faut coder cette méthode");
	}

	public boolean finDePartie() {
		throw new UnsupportedOperationException("Il vous faut coder cette méthode");
	}

	public PlateauJeu copy() {
		throw new UnsupportedOperationException("Il vous faut coder cette méthode");
	}

	public boolean coupValide(Joueur j, CoupJeu c) {
		throw new UnsupportedOperationException("Il vous faut coder cette méthode");
	}
}
