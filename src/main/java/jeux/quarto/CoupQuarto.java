package jeux.quarto;

import iia.jeux.modele.CoupJeu;

public class CoupQuarto implements CoupJeu {
    private final byte idCoup;

    public CoupQuarto(byte id){
	idCoup = id;
    }
    
    
    public CoupQuarto(String s){
	byte coup=0;
	if(s.length() == 2){ // C'est une coordonnée. 

	    // 1. recherche de la colonne 
	    char first_kr = s.charAt(0),
		second_kr = s.charAt(1);
	    
	    switch(first_kr){
	    case('A'):
		coup = 0x00;
		break;
	    case('B'): 
		coup = 0x04;
		break;
	    case('C'): 
		coup = 0x08;
		break; 
	    case('D'):
		coup = 0x0C;
		break;
	    default :
		throw new IllegalArgumentException("Construction de coupQuarto : La chaîne n'est pas valide");
	    }

	    switch(second_kr){
	    case('1'):
		break;
	    case('2'):
		coup = (byte) (coup ^ 0x01);
		break;
	    case('3'):
		coup = (byte) (coup ^ 0x02);
		break;
	    case('4'):
		coup = (byte) (coup ^ 0x03);
		break;
	    default:
		throw new IllegalArgumentException("Construction de coupQuarto : La chaîne n'est pas valide");		
	    }

	    this.idCoup = coup;

	} else {

	    char color  = s.charAt(0),
		taille  = s.charAt(1),
	        pleinure= s.charAt(2), // dénote de si c'est troué ou pas
		carrure = s.charAt(3); // --- carré --- 
	    
	    if(color == 'b')
		coup = 0x08;
	    if(taille == 'g')
		coup = (byte) (coup ^ 0x04);
	    if(pleinure == 'p')
		coup = (byte)  (coup ^ 0x02);
	    if(carrure == 'c')
		coup = (byte) (coup ^ 0x01);

	    this.idCoup = coup;
	}
    }
    
    public byte get(){
	return idCoup;
    }
    
    public String toString(boolean isDon){
	if(isDon) return coordToString(idCoup);
	else return pieceToString(idCoup);
    }

    private static String coordToString(byte coordonnee_case){
	byte chiffre =(byte) (0x03 & coordonnee_case);
	byte lettre = (byte) ((0x0C & coordonnee_case) >>> 2);
	
	char char_lettre = '?';
	
	
	switch(lettre){
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
	
	return char_lettre + Integer.toString(chiffre);
    }
    
    private static String pieceToString(byte idPiece){
	// Bleu/rouge, Grand/petit, Plein/troué, Rond/carré
	// Bleu = blanc, Rouge = noir
	
	char[] str = new char[4];
	
	if(idPiece % 2 ==  0) // 0 = rond
	    str[3] = 'r';
	else str[3] = 'c';

	if((idPiece >>> 1) % 2 == 0) // 0 = troué
	    str[2] = 't';
	else str[2] = 'p';
	
	if((idPiece>>> 2) % 2 == 0) // 0 = troué
	    str[1] = 'p';
	else str[1] = 'g';
	
	if((idPiece>>> 3) % 2 == 0) // 0 = troué
	    str[0] = 'r';
	else str[0] = 'b';

	
	return new String(str);
    }
}
