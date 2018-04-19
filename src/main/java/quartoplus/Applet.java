package quartoplus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JApplet;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Applet extends JApplet

{
    final static int BLANC = -1;
    final static int NOIR = 1;
    final static int VIDE = 0;
    
  
    final private static int LARGEUR = 4;
    final private static int HAUTEUR = 4;
   
    final private static int TAILLEPIONS = 40;

    private static final long serialVersionUID = 1L;
    private JList brdList;
    private Board displayBoard;
    private JScrollPane scrollPane;
    private DefaultListModel listModel;
    private Frame myFrame;

    static int cpt = 0;

    public void init() {

	System.out.println("Initialisation BoardApplet" + cpt++);
	buildUI(getContentPane());
    }

    public void buildUI(Container container) {
	setBackground(Color.white);

	Piece[][] temp = new Piece[HAUTEUR][LARGEUR];

	for (int i = 0; i < HAUTEUR; i++)
	    for (int j = 0; j < LARGEUR; j++)
		temp[i][j] = Piece.zzzz;

	displayBoard = new Board("Coups", temp);

	listModel = new DefaultListModel();
	listModel.addElement(displayBoard);

	brdList = new JList(listModel);
	brdList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	brdList.setSelectedIndex(0);
	scrollPane = new JScrollPane(brdList);
	Dimension d = scrollPane.getSize();
	scrollPane.setPreferredSize(new Dimension(200, d.height));

	brdList.addKeyListener(new java.awt.event.KeyAdapter() {
	    public void keyPressed(KeyEvent e) {
		brdList_keyPressed(e);
	    }
	});
	brdList.addMouseListener(new java.awt.event.MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		brdList_mouseClicked(e);
	    }
	});
	container.add(displayBoard, BorderLayout.CENTER);
	container.add(scrollPane, BorderLayout.EAST);
    }

    public void update(Graphics g, Insets in) {
	Insets tempIn = in;
	g.translate(tempIn.left, tempIn.top);
	paint(g);
    }

    public void paint(Graphics g) {
	displayBoard.paint(g);
    }

    public void addBoard(String move, Piece[][] pieces) {
	Board tempEntrop = new Board(move, pieces);
	listModel.addElement(new Board(move, pieces));
	brdList.setSelectedIndex(listModel.getSize() - 1);
	brdList.ensureIndexIsVisible(listModel.getSize() - 1);
	displayBoard = tempEntrop;
	update(myFrame.getGraphics(), myFrame.getInsets());
    }

    public void setMyFrame(Frame f) {
	myFrame = f;
    }

    void brdList_keyPressed(KeyEvent e) {
	int index = brdList.getSelectedIndex();
	if (e.getKeyCode() == KeyEvent.VK_UP && index > 0)
	    displayBoard = (Board) listModel.getElementAt(index - 1);

	if (e.getKeyCode() == KeyEvent.VK_DOWN && index < (listModel.getSize() - 1))
	    displayBoard = (Board) listModel.getElementAt(index + 1);

	update(myFrame.getGraphics(), myFrame.getInsets());

    }

    void brdList_mouseClicked(MouseEvent e) {
	displayBoard = (Board) listModel.getElementAt(brdList.getSelectedIndex());
	update(myFrame.getGraphics(), myFrame.getInsets());
    }

    // Sous classe qui dessine le plateau de jeu
    class Board extends JPanel {

	private static final long serialVersionUID = 1L;
	Piece[][] boardState;
	String move;

	// The string will be the move details
	// and the array the details of the board after the move has been applied.
	public Board(String mv, Piece[][] pieces) {
	    boardState = pieces;
	    move = mv;
	}

	public void drawBoard(Graphics g) {
	    // First draw the lines
	    // Board
	    int bx = 30;
	    int by = 30;
	    boolean alt = true;

	    // axis labels
	    g.setColor(new Color(0, 0, 0));
	    for (int i = 1; i <= LARGEUR; i++) {
		g.drawString("" + (char) ('A' + i - 1), i * TAILLEPIONS + 10, 20);
	    }
	    for (int i = 1; i <= HAUTEUR; i++) {
		g.drawString("" + i, 5, i * TAILLEPIONS + 10);
	    }

	    // draw the squares
	    Color c1 = new Color(210, 210, 210);
	    Color c2 = new Color(190, 190, 190);
	    for (int i = 0; i < TAILLEPIONS * LARGEUR; i += TAILLEPIONS) {
		for (int j = 0; j < TAILLEPIONS * HAUTEUR; j += TAILLEPIONS) {
		    g.setColor(alt ? c1 : c2);
		    alt = !alt;
		    g.fillRect(bx + i, by + j, TAILLEPIONS, TAILLEPIONS);
		}
		alt = !alt;
	    }

	    // Draw the pieces by referencing boardState array
	    for (int i = 0; i < LARGEUR; i++) {
		for (int j = 0; j < HAUTEUR; j++) { 
		    switch (boardState[j][i]) {	    
		    case bgpc:
		    	drawingpion("bgpc",g,bx,by,i,j);
		    	break;		    
		    case bgpr:
		    	drawingpion("bgpr",g,bx,by,i,j);
		    	break;		    
		    case bgtc:  
		    	drawingpion("bgtc",g,bx,by,i,j);
		    	break;	
		    case bgtr:
		    	drawingpion("bgtr",g,bx,by,i,j);
		    	break;	
		    case bppc:
		        drawingpion("bppc",g,bx,by,i,j);
		        break;	
		    case bppr:
		    	 drawingpion("bppr",g,bx,by,i,j);
			     break;	
		    case bptc:
		    	 drawingpion("bptc",g,bx,by,i,j);
			     break;	
		    case bptr:
		    	 drawingpion("bptr",g,bx,by,i,j);
			     break;	
		    case rgpc:
		    	 drawingpion("rgpc",g,bx,by,i,j);
			     break;	
		    case rgpr:
		    	 drawingpion("rgpr",g,bx,by,i,j);
			     break;
		    case rgtc:
		    	 drawingpion("rgtc",g,bx,by,i,j);
			     break;
		    case rgtr:
		    	 drawingpion("rgtr",g,bx,by,i,j);
			     break;
		    case rppc:
		    	 drawingpion("rppc",g,bx,by,i,j);
			     break;
		    case rppr:
		    	 drawingpion("rppr",g,bx,by,i,j);
			     break;
		    case rptc:
		    	 drawingpion("rptc",g,bx,by,i,j);
			     break;
		    case rptr:
		    	 drawingpion("rptr",g,bx,by,i,j);
			     break;
		    }
		}
	    }
	}
	
	public void drawingpion(String text, Graphics g, int bx, int by, int i, int j){
    	FontMetrics fm = g.getFontMetrics();
    	double textWidth = fm.getStringBounds(text, g).getWidth();
    	g.setColor(Color.orange);
    	g.fillOval(bx + TAILLEPIONS * i + 6, by + TAILLEPIONS * j + 6, TAILLEPIONS - 8,
    			TAILLEPIONS - 8);
    	// Put text into circle
    	// What is the job of getstringbounds 
    	g.setColor(Color.white);
    	g.drawString(text, (int) (bx+ TAILLEPIONS * i + 23 - textWidth/2),(int) (by+ TAILLEPIONS * j + 19 + fm.getMaxAscent() / 2));
	}

	public void paint(Graphics g) {
	    drawBoard(g);
	}

	public void update(Graphics g) {
	    drawBoard(g);
	}

	public String toString() {
	    return move;
	}
    }
}
