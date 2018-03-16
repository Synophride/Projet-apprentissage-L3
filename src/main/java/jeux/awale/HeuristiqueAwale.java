package jeux.awale;

import iia.jeux.alg.Heuristique;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import java.util.Random;

public class HeuristiqueAwale{
    public static Heuristique heuristique1_j1 = new Heuristique(){
	    public int eval(PlateauJeu plateau, Joueur j){
		PlateauAwale p = (PlateauAwale) plateau;
		int nbj1 = 0, nbj2=0;
		for(int i=0; i < 6; i++){
		    nbj1 += p.nbGraineCase(j, i);
		    nbj2 += p.nbGraineCase( p.autreJoueur(j), i);
		}
		
		return nbj1 - nbj2;
	    }		
	};
	
    public static Heuristique heuristique1_j2 = new Heuristique(){
	    public int eval(PlateauJeu plateau, Joueur j){
		return -heuristique1_j1.eval(plateau, j);
	    }
	};
	
    public static Heuristique heuristique2_j1 = new Heuristique(){
	    public int eval(PlateauJeu plateau, Joueur j){
		PlateauAwale p = (PlateauAwale) plateau;
			
		int nbCase1Ou2GraineJ1 = 0;
		int nbCase1Ou2GraineJ2 = 0;
		int nbCoupAvantageux = 0;
			
		for(int i=0; i<p.NB_CASES_LARGEUR; i++) {
		    /// 
		    if(p.nbGraineCase(j, i) == 1 || p.nbGraineCase(j, i) == 2)
			nbCase1Ou2GraineJ1++;
		}
			
		for(int i=0; i<p.NB_CASES_LARGEUR; i++) {
		    if(p.nbGraineCase(p.autreJoueur(j), i) == 1 || p.nbGraineCase( p.autreJoueur(j), i) == 2 )
			nbCase1Ou2GraineJ2++;
		}
			
		for(int i=0; i<p.NB_CASES_LARGEUR; i++) {
		    if(p.nbGraineCase(j, i) > p.NB_CASES_LARGEUR - i)
			nbCoupAvantageux++;
		}
		
		int nbj1 = 0, nbj2 = 0;
		for(int i=0; i < 6; i++){
		    nbj1 += p.nbGraineCase(j, i);
		    nbj2 += p.nbGraineCase( p.autreJoueur(j), i);
		}
	        
		return (nbj1 - nbj2) - (nbCase1Ou2GraineJ1/2) + (nbCase1Ou2GraineJ2/2) + nbCoupAvantageux;
	    }		
	};

    
    public static Heuristique heuristique2_j2 = new Heuristique(){
	    public int eval(PlateauJeu plateau, Joueur j){
		return -heuristique2_j1.eval(plateau, j);
	    }
	};
	
    public static Heuristique heuristiqueAlea_j1 = new Heuristique(){
	    public int eval(PlateauJeu plateau, Joueur j){
		PlateauAwale p = (PlateauAwale) plateau;
		Random rand = new Random();
			
		return rand.nextInt();
	    }		
	};
	
    public static Heuristique heuristiqueAlea_j2 = new Heuristique(){
	    public int eval(PlateauJeu plateau, Joueur j){
		return -heuristiqueAlea_j1.eval(plateau, j);
	    }
	};
}
