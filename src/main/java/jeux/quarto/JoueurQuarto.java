package jeux.quarto;
import iia.jeux.alg.*;
import iia.jeux.modele.*;
import iia.jeux.modele.joueur.Joueur;
import quartoplus.IJoueur;

/**
 * Classe implémentant l'interface IJoueur, pour le quarto
 **/
public class JoueurQuarto implements IJoueur {
    private int coulour;
    // BLANC commence
    private static final int NOIR  = IJoueur.BLANC;
    private static final int BLANC = IJoueur.NOIR;
    
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
	if(coulour == NOIR)
	    jh = new AlphaBeta(HeuristiqueQuarto.heuristique1_j1, j_noir, j_blanc, 1);	
    }

    /*
      algoJoueur[0] = 
    */
    
    public int getNumJoueur(){
	return coulour;
    }
    
    public String choixMouvement(){
	System.out.println(p.toString());      
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
	return "eluhjizdaugyutguhujiuytfrdtfgjhjiuytfhg";
    }
}
