package jeux.quarto;

import iia.jeux.modele.joueur.Joueur;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlateauQuartoTest {
	PlateauQuarto plateau;
	
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
		CoupQuarto c1 = new CoupQuarto((byte) 1, false);
		
		Assert.assertTrue(plateau.coupValide(plateau.getJ0(), c1));
	}
	
	@Test
	public void testPieceToString() {
		Assert.assertEquals(plateau.pieceToString((byte) 0), "rptr");
		Assert.assertEquals(plateau.pieceToString((byte) 1), "rptc");
		Assert.assertEquals(plateau.pieceToString((byte) 2), "rppr");
	}
}
