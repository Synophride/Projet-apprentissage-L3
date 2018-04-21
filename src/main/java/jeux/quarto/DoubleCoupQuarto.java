package jeux.quarto;

import iia.jeux.modele.CoupJeu;

public class DoubleCoupQuarto implements CoupJeu {
    private final byte id_coord;
    private final byte id_piece;

    /***
     * Construit un nouveau plateau de quarto
     * @param idCoord
     *          L'identifiant des coordonnées de là ou on veut poser la pièce
     * @param idPiece
     *          L'identifiant de la pièce qu'on donnera à l'adversaire
     ***/
    public DoubleCoupQuarto(byte idCoord, byte idPiece){
	id_piece = idPiece;
	id_coord = idCoord;
    }
    
    /**
     * Constructeur à partir d'une chaîne de caractères
     * @param str 
     *          l'identifiant du double coup de la pièce.
     * Ce double-coup est de la forme [id coordonnées] + '-' + [id piece], ou l'identifiant
     * des coordonnées est de la forme [A-D][1-4]
     *
     **/
    public DoubleCoupQuarto(String str){
	String[] separated_str = str.split("-");
	id_coord = PlateauQuarto.stringToCoord(separated_str[0]);
	id_piece = PlateauQuarto.stringToPiece(separated_str[1]);
    }

    /** 
     * Rend l'identifiant des coordonnées d'une case
     */
    public byte get_coord(){
	return id_coord;
    }
    /**
     * Rend l'id de la pièce qu'on veut donner à l'adversayre
     **/
    public byte get_piece(){
	return id_piece;
    }

    public String toString(){
	return PlateauQuarto.coordToString(id_coord) + "-" + PlateauQuarto.pieceToString(id_piece);
    }
}
