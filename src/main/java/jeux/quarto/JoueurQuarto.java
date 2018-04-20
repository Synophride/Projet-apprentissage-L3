package jeux.quarto;
import iia.jeux.alg.*;
import iia.jeux.modele.*;
import iia.jeux.modele.joueur.Joueur;
import quartoplus.IJoueur;

public class JoueurQuarto implements IJoueur {
    private int coulour;
    // BLANC commence
    private static final int BLANC = IJoueur.BLANC;
    private static final int NOIR = IJoueur.NOIR;
    
    AlgoJeu jh = new JoueurHumainQuarto();
    
    private Joueur
	j_blanc = new Joueur("blanc"),
	j_noir = new Joueur("noir");
    private PlateauQuarto p = new PlateauQuarto(j_blanc, j_noir);
    
    private AlgoJeu algo = new JoueurHumainQuarto();

    /**
     * Initialise un nouveau joueur humain
     *
     **/
    public JoueurQuarto(){
	
    }
    /**
     * Initialise un nouveau joueur avec l'algo de jeu aj
     **/
    public JoueurQuarto(AlgoJeu aj){
	jh = aj; 
    }

    /**
     * Changement de l'algorithme de jeu (par exemple parce qu'on change de "partie de la partie"
     **/ 
    public void changementAlgoJeu(AlgoJeu aj){
	jh = aj;
    }

    /**
     * Initialise le joueur à mycolour. 
     */
    public void initJoueur(int mycolour){
	coulour = mycolour;
	
    }

    /*
      algoJoueur[0] = new AlphaBeta(HeurcistiqueQuarto.heuristique1_j1, joueur_noir, joueur_blanc, prof_blanc);
      algoJoueur[1] = new Minimax(HeuristiqueQuarto.heuristique_aleatoire, joueur_blanc, joueur_noir, prof_noir);
    */
    
    public int getNumJoueur(){
	return coulour;
    }
    
    public String choixMouvement(){
	CoupJeu ret = jh.meilleurCoup(p);
	p.joue( p.joueur_jouant(), ret);
	return ret.toString();
    }
    
    public void declareLeVainqueur( int colour ){
	if( colour != coulour)
	    System.out.println("il a triché");
    }
    
    public void mouvementEnnemi(String coup){
	DoubleCoupQuarto cj = new DoubleCoupQuarto(coup);
	p.joue(p.joueur_jouant(), cj);
    }
    
    /// Pê le nom du binôme
    public String binoName(){
	return "";
    }
}
