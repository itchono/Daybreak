
import java.util.ArrayList;
import java.util.List;

import processing.core.*;

public class SquareSpace extends PApplet {

	Form[] forms = new Form[10];

	static int activeForm = -1;
	// used in conjunction with array

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("SquareSpace");
	}

	public void settings() {
		setSize(1280, 720);
	}

	public void setup() {
		background(200, 200, 200);

		forms[0] = new Form(this, color(255, 255, 250), 10, 10, 900, 700);
		forms[0].createGrid();
		forms[0].createShop();

		forms[1] = new Form(this, color(200, 200, 200), 0, 0, width, height);
		forms[1].createButton();

		activeForm = 1;
		// Menu

		fill(255, 255, 255);

		// fill in current form

		forms[activeForm].fillDraw();
	}

	public void draw() {
		forms[activeForm].drawComponents();
	}

	public void mouseClicked() {

		forms[activeForm].clickForm();

	}

	public void formTransition() {
		background(200, 200, 200);

		textAlign(LEFT);

		forms[activeForm].fillDraw();

	}

	public static void setActiveForm(int f) {
		activeForm = f;
	}

}

class Form {
	// Base object which everything should be layered upon

	PApplet parent;

	int dispC, x, y, width, height;

	String msg;

	// Can contain:

	List components = new ArrayList();

	Form(PApplet p, int c, int x, int y, int w, int h) {
		parent = p;
		dispC = c;
		this.x = x;
		this.y = y;
		width = w;
		height = h;
	}

	public void createGrid() {
		components.add(new Grid(parent, 20, 10, 40, 0, 20 + x, 100 + y));
	}

	public void createButton() {
		components.add(new MenuButton(parent, "Start", 500, 200, 100, 50));
	}

	public void createShop() {
		components.add(new Shop(parent, 920, 10, 200, 700));
	}

	public void fillDraw() {
		// Onetime setup for BG
		parent.fill(dispC);
		parent.rect(x, y, width, height);
		parent.fill(parent.color(0));
		parent.textSize(32);
		parent.text("Battle Squares. Click to Play.", 20, 60);
		// Draw itself
	}

	public void clickForm() {
		for (int i = 0; i < components.size(); i++) {
			Component c = (Component) components.get(i);
			c.click();
		}
	}

	public void drawComponents() {
		for (int i = 0; i < components.size(); i++) {
			Component c = (Component) components.get(i);
			c.draw();
		}
	}

}

// Components

// Superclass

class Component {
	int x, y, w, h;

	PApplet parent;

	int[][] colours;
	// 2D array; colours for each state, and a highlight layer
	// i.e. [0][0] is base colour for state 0, while [1][0] is highlight colour for
	// state 0

	// Same infrastructure will be used in image textures for actual game

	Component(PApplet parent, int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.parent = parent;

	}

	public boolean containsMouse() {
		return (parent.mouseX > x && parent.mouseX < x + w && parent.mouseY > y && parent.mouseY < y + h);
	}

	public void draw() {

		parent.rect(x, y, w, h);
	}

	public void click() {

	}
}

class Grid extends Component {
	// Invisible object with no set dimensions; used as frame for square cells

	int cellSize, gapSpace;

	Cell[][] squares;

	// Constructor
	Grid(PApplet p, int w, int h, int cellSize, int gapSpace, int x, int y) {
		super(p, x, y, w, h);
		this.cellSize = cellSize;
		this.gapSpace = gapSpace;

		// Initialize squares

		squares = new Cell[w][h];

		for (int gridX = 0; gridX < w; gridX++) {
			for (int gridY = 0; gridY < h; gridY++) {
				squares[gridX][gridY] = new Cell(parent, cellSize * (gridX + gapSpace) + x,
						cellSize * (gridY + gapSpace) + y, gridX, gridY, cellSize, cellSize);
				// Applies offset of the grid component itself to each square
			}
		}
	}

	public void draw() {
		// mirrors draw command to all squares
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				squares[x][y].draw();
			}
		}
	}

	public void click() {

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				// State logic
				if (squares[x][y].containsMouse() && squares[x][y].state == 0)
					squares[x][y].state++;
				else if (squares[x][y].containsMouse())
					squares[x][y].state--;
			}
		}
	}

}

class Cell extends Component {
	// Square objects drawn on screen, can contain occupants, and are the basis of
	// interaction
	int state, gridX, gridY, unitID;

	PImage[] unitImg;

	// x pos, y pos, side length, state variable

	Cell(PApplet p, int px, int py, int gx, int gy, int w, int h) {
		super(p, px, py, w, h);
		colours = new int[][] { { p.color(100, 100, 200), p.color(65, 242, 183) },
				{ p.color(150, 150, 250), p.color(132, 237, 202) } };
		gridX = gx;
		gridY = gy;
		unitImg = new PImage[] { parent.loadImage("ElementanTank.png"), parent.loadImage("EquanosTank.png") };
		// TODO NEVER EVER EVER EVER EVER DRAW WITH AN IMAGE IN SETUP
		state = (gx + gy) % 2;
	}

	public void draw() {
		
		// try to only change on update


		parent.fill(colours[this.containsMouse() ? 1 : 0][state]);
		// Learned the ? operator - feels nice to use
		// Essentially, uses ? operator to determine whether it's highlighted, then
		// changes colour using AN ARRAY - faster decision

		parent.rect(x, y, w, h);

		parent.image(unitImg[state], x + 5, y + 5, w - 10, h - 10);

		parent.fill(255);
		parent.textSize(12);
		parent.text((String) (gridX + "," + gridY), x + 20, y + 20);
	}

}

class MenuButton extends Component {
String text;

	MenuButton(PApplet p, String s, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		text = s;
	}

	public void draw() {
		// try to only change on update

		if(this.containsMouse()) {
			parent.fill(parent.color(200, 200, 200));
			parent.rect(x, y, w, h);

			parent.textAlign(parent.CENTER, parent.CENTER);
			parent.fill(parent.color(0));
			parent.text(text, x + w / 2, y + h / 2);
		}
		

	}

	public void click() {
		if (this.containsMouse())
		SquareSpace.setActiveForm(0);
	}

}

class Shop extends Component {

	ShopItem[] shops;

	Shop(PApplet parent, int x, int y, int w, int h) {
		super(parent, x, y, w, h);

	}

}

class ShopItem extends Component {

	ShopItem(PApplet parent, int x, int y, int w, int h) {
		super(parent, x, y, w, h);

	}

}

class Occupant {
	// Base class for units and buildings

}
