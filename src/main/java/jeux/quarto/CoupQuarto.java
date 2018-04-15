package jeux.quarto;

import iia.jeux.modele.CoupJeu;

public class CoupQuarto implements CoupJeu {
    private final byte idCoup;
    private boolean is_don;
    
    public CoupQuarto(byte id, boolean is_don){
	idCoup = id;
	this.is_don = is_don;
    }
    
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

    public byte get() {
        return idCoup;
    }

    public boolean get_type(){
	return is_don;
    }

    public String toString(boolean isDon) {
        if (isDon)
            return pieceToString(idCoup);
        else
            return coordToString(idCoup);
    }

    private static String coordToString(byte coordonnee_case) {
        byte chiffre = (byte) (0x03 & coordonnee_case >>> 2);
        byte lettre = (byte) ((0x0C & coordonnee_case));

        char char_lettre = '?';

        switch (lettre) {
        case 0:
            char_lettre = 'A';
            break;
        case 1:
            char_lettre = 'B';
            break;
        case 2:
            char_lettre = 'C';
            break;
        case 3:
            char_lettre = 'D';
            break;
        default:
            char_lettre = '?';
            break;
        }

        return char_lettre + Integer.toString(chiffre+1);
    }

    private static String pieceToString(byte idPiece) {
        // Bleu/rouge, Grand/petit, Plein/troué, Rond/carré
        // Bleu = blanc, Rouge = noir

        char[] str = new char[4];

        if (idPiece % 2 == 0) // 0 = rond
            str[3] = 'r';
        else
            str[3] = 'c';

        if ((idPiece >>> 1) % 2 == 0) // 0 = troué
            str[2] = 't';
        else
            str[2] = 'p';

        if ((idPiece >>> 2) % 2 == 0) // 0 = troué
            str[1] = 'p';
        else
            str[1] = 'g';

        if ((idPiece >>> 3) % 2 == 0) // 0 = troué
            str[0] = 'r';
        else
            str[0] = 'b';

        return new String(str);
    }
}
