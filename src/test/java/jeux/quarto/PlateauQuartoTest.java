package jeux.quarto;

import iia.jeux.modele.joueur.Joueur;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlateauQuartoTest {
	PlateauQuarto plateau;
	
    // Syntaxe des pièces ("en commençant par les bits de poids fort="
    // b/r -> bleu , rouge. resp. 1 et 0 en notation binaire.
    // g/p -> grand, petit. resp. 1 et 0
    // p/t -> plein, troué. Resp. 1 et 0
    // c/r -> carré, rond . Resp. 1 et 0
   
    @Before
	public void init() {
		plateau = new PlateauQuarto(new Joueur("Blanc"), new Joueur("Noir"));
	}

	@Test
	public void testPlateauQuarto() {
	    Assert.assertTrue(true);
	}
	
	@Test
	public void testCoupValide() {
	    // je me permets de changer cette ligne, étant donné que le constructeur avec le booléen sera a priori pas utile étant donné qu'on part du principe que l'on donne sagement les 
	    CoupQuarto c1 = new CoupQuarto((byte) 0x0F);
	    Assert.assertTrue(plateau.coupValide(plateau.getJ0(), c1));
	}
	
	@Test
	public void testPieceToString() {
	    Assert.assertEquals(plateau.pieceToString( (byte) 0x00), "rptr");
	    Assert.assertEquals(plateau.pieceToString( (byte) 0x01), "rptc");
	    Assert.assertEquals(plateau.pieceToString( (byte) 0x08), "bptr");
	}
}
