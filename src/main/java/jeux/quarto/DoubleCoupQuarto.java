package jeux.quarto;

import iia.jeux.modele.CoupJeu;

public class DoubleCoupQuarto implements CoupJeu {

    private final byte id_coord;
    private final byte id_piece;

    public DoubleCoupQuarto(byte idCoord, byte idPiece){
	id_piece = idPiece;
	id_coord = idCoord;
    }
    
    //  COORD - PIECE
    public DoubleCoupQuarto(String str){
	String[] separated_str = str.split("-");
	id_coord = PlateauQuarto.stringToCoord(separated_str[0]);
	id_piece = PlateauQuarto.stringToPiece(separated_str[1]);
    }
    
    public byte get_coord(){
	return id_coord;
    }

    public byte get_piece(){
	return id_piece;
    }

    public String toString(){
	return PlateauQuarto.coordToString(id_coord) + "-" + PlateauQuarto.pieceToString(id_piece);
    }
}
