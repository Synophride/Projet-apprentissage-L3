package iia.jeux.alg;

import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;

public interface Heuristique {

	public int eval(PlateauJeu p, Joueur j);

}
