package jeux.quarto;

import iia.jeux.modele.joueur.Joueur;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlateauQuartoTest {
    PlateauQuarto plateau_depart = new PlateauQuarto(new Joueur("blanc"), new Joueur("noir"));
    PlateauQuarto plateau_final;
    // Syntaxe des pièces ("en commençant par les bits de poids fort="
    // b/r -> bleu , rouge. resp. 1 et 0 en notation binaire.
    // g/p -> grand, petit. resp. 1 et 0
    // p/t -> plein, troué. Resp. 1 et 0
    // c/r -> carré, rond . Resp. 1 et 0
    
    @Before
    public void init() {
	
    }
    
    @Test
    public void testPlateauQuarto() {
    }
    
    @Test
    public void testCoupValide() {
    }

    @Test
    public void testPieceToString() {
    }

    @Test
    public void testEstMoveValide() {
    }
    
    @Test
    public void testEstChoixValide() {
    }
    
    @Test
    public void testFinDePartie() {
    }

    @Test
    public void testPointsCommuns() {
    	Assert.assertEquals(plateau_depart.points_communs((byte) 0x01, (byte) 0x0E), 0);
    	Assert.assertEquals(plateau_depart.points_communs((byte) 0x02, (byte) 0x0E), 3);
    	Assert.assertEquals(plateau_depart.points_communs((byte) 0x0F, (byte) 0x0F), 0x0F);
    }
}
