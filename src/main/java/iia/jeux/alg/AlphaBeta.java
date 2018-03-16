package iia.jeux.alg;

import java.util.ArrayList;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;

public class AlphaBeta implements AlgoJeu {

    /** La profondeur de recherche par défaut
     */
    private final static int PROFMAXDEFAUT = 5;

   
    // -------------------------------------------
    // Attributs
    // -------------------------------------------
 
    /**  La profondeur de recherche utilisée pour l'algorithme
     */
    private int profMax = PROFMAXDEFAUT;

     /**  L'heuristique utilisée par l'algorithme
      */
   private Heuristique h;

    /** Le joueur Min
     *  (l'adversaire) */
    private Joueur joueurMin;

    /** Le joueur Max
     * (celui dont l'algorithme de recherche adopte le point de vue) */
    private Joueur joueurMax;

    /**  Le nombre de noeuds développé par l'algorithme
     * (intéressant pour se faire une idée du nombre de noeuds développés) */
    private int nbnoeuds;

    /** Le nombre de feuilles évaluées par l'algorithme
     */
    private int nbfeuilles;


  // -------------------------------------------
  // Constructeurs
  // -------------------------------------------
    public AlphaBeta(Heuristique h, Joueur joueurMax, Joueur joueurMin) {
        this(h,joueurMax,joueurMin,PROFMAXDEFAUT);
    }

    public AlphaBeta(Heuristique h, Joueur joueurMax, Joueur joueurMin, int profMaxi) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        profMax = profMaxi;
	System.out.println("Initialisation d'un alphaBeta de profondeur " + profMax);
    }

    // -------------------------------------------
    // Méthodes de l'interface AlgoJeu
    // -------------------------------------------
    public CoupJeu meilleurCoup(PlateauJeu p) {
	nbfeuilles = 0;
	nbnoeuds = 0;
	
	ArrayList<CoupJeu> coups_possibles = p.coupsPossibles(joueurMax);

	int max = Integer.MIN_VALUE;
	CoupJeu meilleur_coup = null;
	
	for( CoupJeu coup : coups_possibles ){
	    
	    PlateauJeu pbis = p.copy();
	    pbis.joue(joueurMax, coup);
	    int nmax = negAlphaBeta(pbis,  Integer.MIN_VALUE+1, Integer.MAX_VALUE-1, 1, joueurMin); 
	    if ( nmax  > max ){
		max = nmax;
		meilleur_coup = coup;
	    }
	}
	System.out.println("nb noeuds développés :" + nbnoeuds + "\n nb feuilles développés :" + nbfeuilles);
	
	return meilleur_coup;
    }
  // -------------------------------------------
  // Méthodes publiques
  // -------------------------------------------
    public String toString() {
        return "AlphaBeta(ProfMax="+profMax+")";
    }

  // -------------------------------------------
  // Méthodes internes
  // -------------------------------------------
    private Joueur autreJoueur(Joueur j)
    {
	if (j==joueurMin) return joueurMax;
	else
	    return joueurMin;
    }

    private int negAlphaBeta(PlateauJeu p, int alpha, int beta, int profondeur, Joueur j) {
	if( profondeur >= profMax || p.finDePartie() ){
	    nbfeuilles++;
	    return h.eval(p, j);
	}else{
	    nbnoeuds ++;
	    int alpha_bis_max = alpha;
	    ArrayList<CoupJeu> coups_possibles = p.coupsPossibles(j);
	    for ( CoupJeu i : coups_possibles ){

		PlateauJeu p_bis = p.copy();
		p_bis.joue(j, i);
		int alpha_bis = -negAlphaBeta(p_bis, -beta, -alpha_bis_max, profondeur+1, autreJoueur(j)); 

		if( alpha_bis > alpha_bis_max){
		    alpha_bis_max = alpha_bis;
		}
		if( alpha_bis_max >= beta)
		    return beta;
			
	    }
	    return
		alpha_bis_max;
	}
    }
}

