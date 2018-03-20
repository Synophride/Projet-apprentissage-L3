/**
 * 
 */

package iia.jeux.alg;

import java.util.ArrayList;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;

public class Minimax implements AlgoJeu {

	/**
	 * La profondeur de recherche par défaut
	 */
	private final static int PROFMAXDEFAUT = 4;

	// -------------------------------------------
	// Attributs
	// -------------------------------------------

	/**
	 * La profondeur de recherche utilisée pour l'algorithme
	 */
	private int profMax = PROFMAXDEFAUT;

	/**
	 * L'heuristique utilisée par l'algorithme
	 */
	private Heuristique h;

	/**
	 * Le joueur Min (l'adversaire)
	 */
	private Joueur joueurMin;

	/**
	 * Le joueur Max (celui dont l'algorithme de recherche adopte le point de vue)
	 */
	private Joueur joueurMax;

	/**
	 * Le nombre de noeuds développé par l'algorithme (intéressant pour se faire une
	 * idée du nombre de noeuds développés)
	 */
	private int nbnoeuds = 0;

	/**
	 * Le nombre de feuilles évaluées par l'algorithme
	 */
	private int nbfeuilles = 0;

	// -------------------------------------------
	// Constructeurs
	// -------------------------------------------
	public Minimax(Heuristique h, Joueur joueurMax, Joueur joueurMin) {
		this(h, joueurMax, joueurMin, PROFMAXDEFAUT);
	}

	public Minimax(Heuristique h, Joueur joueurMax, Joueur joueurMin, int profMaxi) {
		this.h = h;
		this.joueurMin = joueurMin;
		this.joueurMax = joueurMax;
		profMax = profMaxi;
		// System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
	}

	// -------------------------------------------
	// Méthodes de l'interface AlgoJeu
	// -------------------------------------------
	public CoupJeu meilleurCoup(PlateauJeu p) {
		nbfeuilles = 0;
		nbnoeuds = 0;
		ArrayList<CoupJeu> coupsPossibles = p.coupsPossibles(joueurMax);
		int max = Integer.MIN_VALUE;
		CoupJeu meilleurCoup = null;

		for (CoupJeu coup : coupsPossibles) {
			PlateauJeu pbis = p.copy();
			pbis.joue(joueurMax, coup);
			int nmax = -negaMax(1, joueurMin, pbis);
			if (nmax > max) {
				max = nmax;
				meilleurCoup = coup;
			}
		}
		System.out.println("nb noeuds développés :" + nbnoeuds + "\n nb feuilles développés :" + nbfeuilles);

		return meilleurCoup;
	}

	// -------------------------------------------
	// Méthodes publiques
	// -------------------------------------------
	public String toString() {
		return "MiniMax(ProfMax=" + profMax + ")";
	}

	// -------------------------------------------
	// Méthodes internes
	// -------------------------------------------
	private Joueur autreJoueur(Joueur j) {
		if (j == joueurMin)
			return joueurMax;
		else
			return joueurMin;
	}

	private int negaMax(int profondeur, Joueur joueurQuiJoue, PlateauJeu etatDuPlateau) {

		if (etatDuPlateau.finDePartie() || profondeur >= profMax) {
			nbfeuilles++;
			return h.eval(etatDuPlateau, joueurQuiJoue);
		} else {
			nbnoeuds++;
			int max = Integer.MIN_VALUE + 1;
			ArrayList<CoupJeu> coupsPossibles = etatDuPlateau.coupsPossibles(joueurQuiJoue);
			for (CoupJeu i : coupsPossibles) {
				PlateauJeu p = etatDuPlateau.copy();
				p.joue(joueurQuiJoue, i);
				int n = -negaMax(profondeur + 1, this.autreJoueur(joueurQuiJoue), p);
				if (n > max)
					max = n;
			}
			return max;
		}
	}
}
