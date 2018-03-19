package jeux.quarto;
import iia.jeux.modele.CoupJeu;

public class CoupQuarto implements CoupJeu {

    private final byte idCoup;

    public CoupQuarto(byte id){
	idCoup = id;
    }
    
    public CoupQuarto(byte id, boolean is_piece){
	byte ind_type_coup;
	
	if(is_piece)
	    ind_type_coup = (byte) 0x80;
	else
	    ind_type_coup = 0x00;

	
	idCoup = (byte) (id | ind_type_coup);
    }
    
    public byte get(){
	return idCoup;
    }

    public String toString(boolean isDon){
	// Si c'est un don de pièce, on affiche une pièce.

	// Dans le cas contraire, on affiche une coordonnée.
      
    }
    
    public String toString(){
	return "";
    }
    
}
