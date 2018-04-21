package jeux.quarto;

import iia.jeux.modele.CoupJeu;

public class CoupQuarto implements CoupJeu {
    private final byte idCoup;
    private boolean is_don;
    /**
     * Constructeur de CoupQuarto
     * @param id
     *    L'identifiant du coup joué
     * @param is_don true si le coup est un don, donc l'identifiant représente une pièce, false dans le cas contraire
     **/
    public CoupQuarto(byte id, boolean is_don){
	idCoup = id;
	this.is_don = is_don;
    }

    /**
     * Constructeur de CoupQuarto
     * @param s
     *   une chaine de caractères pouvant représenter soit une coordonnées, ou la chaine est de forme [A-D][1-4]
     *   ou une pièce, donc de la forme ([b|r][g|p][p|t][r|c])
     **/ 
    public CoupQuarto(String s){
        int coup = 0;
        
        if (s.length() == 2) { // C'est une coordonnée.
	    is_don = false;
	    // Ligne colonne
	    // 1. recherche de la colonne
            char first_kr = s.charAt(0), second_kr = s.charAt(1);
	    
            switch (second_kr) {
            case ('1'):
                break;
            case ('2'):
                coup = 0b0000_01_00;
                break;
            case ('3'):
                coup = 0b0000_10_00;
                break;
            case ('4'):
                coup = 0b0000_11_00;
                break;
            default:
                throw new IllegalArgumentException("Construction de coupQuarto : La chaîne n'est pas valide");
            }
	    
            switch(first_kr){
            case ('A'):
		break;
            case ('B'):
                coup = coup ^ 1;
                break;
            case ('C'):
                coup = coup ^ 0x02;
                break;
            case ('D'):
                coup = coup ^ 0x03;
                break;
            default:
                throw new IllegalArgumentException("Construction de coupQuarto : La chaîne n'est pas valide");
            }
	    
            this.idCoup = (byte) coup;
	    
        } else if (s.length() == 4 ){
	    is_don = true;
            char color = s.charAt(0),
		taille = s.charAt(1),
		pleinure = s.charAt(2),
		carrure = s.charAt(3);

            if (color == 'b')
                coup = 0x08;
            if (taille == 'g')
                coup = (coup ^ 0x04);
            if (pleinure == 'p')
                coup = (coup ^ 0x02);
            if (carrure == 'c')
                coup = (coup ^ 0x01);
	    
            this.idCoup = (byte) coup;
	    
        } else 
	    throw new IllegalArgumentException("Construction de coupQuarto : La chaîne n'est pas valide");
    }

    /** 
     * @return l'identifiant du coup
     */
    public byte get() {
        return idCoup;
    }
    /**
     * @return true si le coup est un don de pièce, false si c'est un dépôt
     */
    public boolean get_type(){
	return is_don;
    }

    public String toString(boolean isDon) {
        if (isDon)
            return PlateauQuarto.pieceToString(idCoup);
        else
            return PlateauQuarto.coordToString(idCoup);
    }
}
