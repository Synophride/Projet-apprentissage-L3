package iia.jeux.alg;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;

public interface AlgoJeu {

	/**
	 * Renvoie le meilleur
	 * 
	 * @param p
	 * @return
	 */
	public CoupJeu meilleurCoup(PlateauJeu p);

}
