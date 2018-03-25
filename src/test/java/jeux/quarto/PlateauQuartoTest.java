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
        // je me permets de changer cette ligne, étant donné que le constructeur avec le
        // booléen sera a priori pas utile étant donné qu'on part du principe que l'on
        // donne sagement les
        CoupQuarto c1 = new CoupQuarto((byte) 0x0F);
        Assert.assertTrue(plateau.coupValide(plateau.getJ0(), c1));
    }

    @Test
    public void testPieceToString() {
        Assert.assertEquals(PlateauQuarto.pieceToString((byte) 0x00), "rptr");
        Assert.assertEquals(PlateauQuarto.pieceToString((byte) 0x01), "rptc");
        Assert.assertEquals(PlateauQuarto.pieceToString((byte) 0x08), "bptr");
    }

    @Test
    public void testEstMoveValide() {
        PlateauQuarto p = new PlateauQuarto(new Joueur("noir"), new Joueur("blanc"));

        Assert.assertTrue(p.estmoveValide("A1", "noir"));

        plateau.play("A1", "noir");

        Assert.assertFalse(p.estmoveValide("A1", "blanc"));
    }

    @Test
    public void testEstChoixValide() {
        PlateauQuarto p = new PlateauQuarto(new Joueur("noir"), new Joueur("blanc"));

        Assert.assertTrue(p.estchoixValide("bgpr", "noir"));

        plateau.play("bgpr", "noir");
        plateau.play("A2", "blanc");

        Assert.assertFalse(p.estchoixValide("bgpr", "blanc"));
    }

    @Test
    public void testFinDePartie() {
        PlateauQuarto p = new PlateauQuarto(new Joueur("noir"), new Joueur("blanc"));

        String[] t = new String[4];
        t[0] = "bgpcbgprrgpcrgpr";
        t[1] = "++++";
        t[2] = "++++";
        t[3] = "++++";

        p.setFromStringTab(t);

        Assert.assertTrue(p.finDePartie());
    }

    @Test
    public void testJoue() throws Exception {
        PlateauQuarto p = new PlateauQuarto(new Joueur("noir"), new Joueur("blanc"));

        p.play("bgpr", "noir");
        p.play("A1", "blanc");

        String test = "1 bgpr+++ 1\n2 ++++ 2\n3 ++++ 3\n4 ++++ 4\n";

        System.out.print(p.toString());
        Assert.assertEquals(p.toString(), test);
    }

}
