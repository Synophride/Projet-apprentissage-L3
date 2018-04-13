package iia.jeux.alg;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;

public interface AlgoJeu {

	/**
	 * Renvoie le meilleur coup
	 * 
	 * @param p plateau de jeu
	 * @return le meilleur coup
	 */
	public CoupJeu meilleurCoup(PlateauJeu p);

}
