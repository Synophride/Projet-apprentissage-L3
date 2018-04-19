package jeux.quarto;
import quartoplus.IJoueur;

public class JoueurQuarto implements IJoueur {
    private int coulour;
    
    AlgoJeu jh = new JoueurHumainQuarto();
    
    private Joueur
	j_blanc = new Joueur("blanc"),
	j_noir = new Joueur("noir");
    private PlateauQuarto plateau_de_jeu = new Plateau(j_blanc, j_noir);

    private AlgoJoueur = new JoueurHumain();
    public JoueurQuarto(){
    }
    
    public JoueurQuarto(AlgoJoueur aj){
	jh = aj;
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
	return s.meilleurCoup(p).toString()
    }
    
    public void declareVainqueur( int colour ){
	if( colour != coulour)
	    System.out.println("il a triché");
    }
    
    public void mouvementEnnemi( String coup){
	DoubleCoupQuarto cj = new DoubleCoupQuarto(coup);
	plateau_de_jeu.joue(cj);
    }

    /// ???
    public String bioname(){
	return "" 
    }
}
