package jeux.dominos;

import iia.jeux.alg.Heuristique;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;

public class HeuristiquesDominos {
	public static Heuristique hblanc = new Heuristique() {
		public int eval(PlateauJeu pa, Joueur j) {
			PlateauDominos p = (PlateauDominos) pa;
			if (p.isJoueurBlanc(j)) {

				int nb_coups_blancs = p.nbCoupsBlanc();
				int nb_coups_noirs = p.nbCoupsNoir();

				if (nb_coups_blancs == 0)
					return (Integer.MIN_VALUE + 1);
				if (nb_coups_noirs == 0)
					return (Integer.MAX_VALUE - 1);

				return (nb_coups_blancs - (nb_coups_noirs));
			} else {
				int nb_coups_blancs = p.nbCoupsBlanc();
				int nb_coups_noirs = p.nbCoupsNoir();

				if (nb_coups_noirs == 0)
					return (Integer.MAX_VALUE - 1);

				if (nb_coups_blancs == 0)
					return (Integer.MIN_VALUE + 1);
				return (nb_coups_blancs - (nb_coups_noirs));
			}
		}
	};

	public static Heuristique hnoir = new Heuristique() {
		public int eval(PlateauJeu p, Joueur j) {
			return -hblanc.eval(p, j);
		}
	};
}
