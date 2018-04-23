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
    
    AlgoJeu jh;
    
    private Joueur
	j_blanc = new Joueur("blanc"),
	j_noir = new Joueur("noir");
    
    private PlateauQuarto p =
	new PlateauQuarto(j_blanc, j_noir);

    /**
     * Initialise un nouveau joueur humain
     *
     **/
    public JoueurQuarto(){
	
    }
    
    /**
     * Initialise le joueur à mycolour. 
     */
    public void initJoueur(int mycolour){
	coulour = mycolour;

	if( IJoueur.BLANC == coulour )
	    jh = new AlphaBeta(HeuristiqueQuarto.heuristique1_j1, j_blanc, j_noir, 1);

	else
	    jh = new Minimax(HeuristiqueQuarto.heuristique_aleatoire, j_noir, j_blanc, 3);
    }

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
	return "Devatine  --  Guyot ";
    }
}
