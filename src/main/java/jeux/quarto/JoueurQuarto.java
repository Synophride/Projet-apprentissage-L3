import quartoplus.IJoueur;

public class JoueurQuarto implements IJoueur {
    private int coulour;

    AlgoJeu s;
    
    Joueur
	j_blanc = new Joueur("blanc"),
	j_noir = new Joueur("noir");
    private PlateauQuarto plateau_de_jeu;
    
    public JoueurQuarto(){
	plateau_de_jeu = new PlateauQuarto(j_blanc, j_noir);
    }
    
    public void initJoueur(int mycolour){
	coulour = mycolour;
	// initialiser algoJeu
    }
    
    public int getNumJoueur(){
	return coulour;
    }
    
    public String choixMouvement(){
	// meilleur_mouvement.toString()
	return s.meilleurCoup(p).toString()
    }
    
    public void declareVainqueur( int colour ){
	if( colour != coulour)
	    System.out.println("il a trichÃ©");
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
