ackage jeux.quarto;
import iia.jeux.modele.CoupJeu;

pubic class CoupQuarto implements CoupJeu {

    private final byte idCoup;

    public CoupQuarto(byte id){
	idCoup = id;
    }
    
    public CoupQuarto(byte id, boolean is_piece){
	byte ind_type_coup;
	
	if(is_piece)
	    ind_type_coup = 0x80;
	else
	    inf_type_coup = 0;
	
	idCoup = (id | ind_byte_coup);

    }
    
    public byte get(){
	return idCoup;
    }
    
    public String toString(){
	""	    
    }
    
}
