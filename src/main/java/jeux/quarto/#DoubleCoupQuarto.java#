package jeux.quarto;

import iia.jeux.modele.CoupJeu;

public class DoubleCoupQuarto implements CoupJeu {

    private final byte id_coord;
    private final byte id_piece;

    public DoubleCoupQuarto(byte idCoord, byte idPiece){
	id_piece = idPiece;
	id_coord = idCoord;
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
