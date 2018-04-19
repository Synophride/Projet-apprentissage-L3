package jeux.quarto;
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
    private PlateauQuarto p = new Plateau(j_blanc, j_noir);
    
    private AlgoJoueur algo = new JoueurHumainQuarto();
    
    public JoueurQuarto(){
	
    }
    
    public JoueurQuarto(AlgoJoueur aj){
	jh = aj; 
    }

    public void changementAlgoJeu(){
	
    }

    
    public void initJoueur(int mycolour){
	coulour = mycolour;
	
    }

    /*
      algoJoueur[0] = new AlphaBeta(HeuristiqueQuarto.heuristique1_j1, joueur_noir, joueur_blanc, prof_blanc);
      algoJoueur[1] = new Minimax(HeuristiqueQuarto.heuristique_aleatoire, joueur_blanc, joueur_noir, prof_noir);
    */
    
    public int getNumJoueur(){
	return coulour;
    }
    
    public String choixMouvement(){
	CoupJeu ret = s.meilleurCoup(p);
	p.joue( p.joueur_jouant(), ret);
	return ret.toString();
    }
    
    public void declareVainqueur( int colour ){
	if( colour != coulour)
	    System.out.println("il a triché");
    }
    
    public void mouvementEnnemi(String coup){
	DoubleCoupQuarto cj = new DoubleCoupQuarto(coup);
	plateau_de_jeu.joue(p.joueur_jouant(), cj);
    }
    
    /// ???
    public String bioname(){
	return "";
    }
}
