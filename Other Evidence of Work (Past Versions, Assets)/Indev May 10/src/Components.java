import processing.core.PConstants;

// Components

// Superclass

class Component {
	int x, y, w, h;

	SquareSpace parent;

	int[][] colours;
	// 2D array; colours for each state, and a highlight layer
	// i.e. [0][0] is base colour for state 0, while [1][0] is highlight colour for
	// state 0

	// Same infrastructure will be used in image textures for actual game

	Component(SquareSpace parent, int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.parent = parent;

	}

	public boolean containsMouse() {
		return (parent.mouseX > x && parent.mouseX < x + w && parent.mouseY > y && parent.mouseY < y + h);
	}

	public boolean cameraContainsMouse() {
		return (parent.mouseX - parent.cameraX > x && parent.mouseX - parent.cameraX < x + w
				&& parent.mouseY - parent.cameraY > y && parent.mouseY - parent.cameraY < y + h);
	}

	public boolean mouseProximity(int range) {
		return (parent.mouseX > x - range && parent.mouseX < x + range + w && parent.mouseY > y - range
				&& parent.mouseY < y + range + h);
	}

	public boolean cameraMouseProximity(int range) {
		return (parent.mouseX + parent.cameraX > x - range && parent.mouseX + parent.cameraX < x + range + w
				&& parent.mouseY + parent.cameraY > y - range && parent.mouseY + parent.cameraY < y + range + h);
	}

	public void draw() {

		parent.rect(x, y, w, h);
	}

	public void click() {

	}

	public void fillRect() {

	}
}

class Grid extends Component {
	// Invisible object with no set dimensions; used as frame for square cells

	int cellSize, gapSpace;

	Cell[][] squares;

	int gw, gh;

	// Constructor
	Grid(SquareSpace p, int x, int y, int w, int h, int gw, int gh, int cellSize, int gapSpace) {
		super(p, x, y, w, h);
		this.cellSize = cellSize;
		this.gapSpace = gapSpace;
		this.gw = gw;
		this.gh = gh;

		// Initialize squares

		squares = new Cell[gw][gh];

		for (int gridX = 0; gridX < gw; gridX++) {
			for (int gridY = 0; gridY < gh; gridY++) {
				squares[gridX][gridY] = new Cell(parent, cellSize * (gridX + gapSpace) + x,
						cellSize * (gridY + gapSpace) + y, gridX, gridY, cellSize, cellSize, this);
				// Applies offset of the grid component itself to each square
			}
		}

	}

	public void fillRect() {
		System.out.println("YO");
		parent.fill(150);
		parent.rect(x, y, w, h);
	}

	public void draw() {

		// mirrors draw command to all squares within a certain mouse proximity

		if (parent.superDraw) {
			// Draw all
			for (int gx = 0; gx < gw; gx++) {
				for (int gy = 0; gy < gh; gy++)
					drawSquare(gx, gy);
			}
		}
		// NEW EFFICIENT DRAWING ALGORITHM
		// Draws within 2 square radius
		else {
			int proximity = 2;
			for (int gx = (xValue() - proximity < 0 ? 0 : xValue() - proximity); gx < (xValue() + proximity > w ? w
					: xValue() + proximity); gx++) {
				for (int gy = (yValue() - proximity < 0 ? 0 : yValue() - proximity); gy < (yValue() + proximity > h ? h
						: yValue() + proximity); gy++)
					drawSquare(gx, gy);
			}
		}

	}

	public int xValue() {
		return (int) Math.floor((parent.mouseX - x - parent.cameraX) / (double) (cellSize + gapSpace));
	}

	public int yValue() {
		return (int) Math.floor((parent.mouseY - y - parent.cameraY) / (double) (cellSize + gapSpace));
	}

	public void drawSquare(int gx, int gy) {
		if (squares[gx][gy].x + cellSize < x + w - parent.cameraX
				&& squares[gx][gy].y + cellSize < y + h - parent.cameraY && squares[gx][gy].x > x - parent.cameraX
				&& squares[gx][gy].y > y - parent.cameraY)
			squares[gx][gy].draw();
	}

	public void click() {

		// Optimizing clicking process
		if (xValue() < w && xValue() >= 0 && yValue() < h && yValue() >= 0) {

			if (parent.gameState == 0) {
				squares[xValue()][yValue()].occupant.id = squares[xValue()][yValue()].occupant.id == -1
						? parent.selectedUnitID
						: -1;
			} else {
				parent.selectedUnit = squares[xValue()][yValue()].occupant;
				parent.selectedUnit.owner = 1;
				// temp
			}

		}
		// TODO - add shop implementation
		// Make it the unit or resets the square
	}

	public void fillOccupants() {
		// fills with empty occupants by default for now, add players later
		for (int gx = 0; gx < gw; gx++) {
			for (int gy = 0; gy < gh; gy++) {
				squares[gx][gy].occupant = new Occupant(parent, -1, -1);
			}
		}
	}

}

class Cell extends Component {
	// Square objects drawn on screen, can contain occupants, and are the basis of
	// interaction
	int gridX, gridY;

	Grid gridP;

	Occupant occupant;

	// x pos, y pos, side length, state variable

	Cell(SquareSpace p, int px, int py, int gx, int gy, int w, int h, Grid gridP) {
		super(p, px, py, w, h);
		colours = new int[][] { { p.color(100, 100, 200), p.color(84, 168, 53) },
				{ p.color(150, 150, 250), p.color(102, 206, 64) } };
		gridX = gx;
		gridY = gy;
		occupant = new Occupant(parent, -1, -1);
		occupant.setCoords(gx, gy);
		// TODO NEVER EVER EVER EVER EVER DRAW WITH AN IMAGE IN SETUP

	}

	public void draw() {
		parent.fill(occupant.owner == -1 ? (this.cameraContainsMouse() ? parent.color(190) : parent.color(150))
				: colours[this.cameraContainsMouse() ? 1 : 0][occupant.owner]);

		parent.rect(x + parent.cameraX, y + parent.cameraY, w, h);

		if (occupant.id != -1) {
			parent.image(parent.unitImg[occupant.id], x + 5 + parent.cameraX, y + 5 + parent.cameraY, w - 10, h - 10);
		}

	}

}

// TODO - generic button
class MenuButton extends Component {
	String text;

	MenuButton(SquareSpace p, String s, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		text = s;
	}

	public void draw() {
		// try to only change on update

		parent.textSize(32);

		if (this.mouseProximity(50) || parent.superDraw) {
			parent.fill(this.containsMouse() ? parent.color(220) : parent.color(200));
			parent.rect(x, y, w, h);

			parent.textAlign(PConstants.CENTER, PConstants.CENTER);
			parent.fill(parent.color(0));
			parent.text(text, x + w / 2, y + h / 2);
		}

	}

	public void click() {
		// TODO make button specific stuff
		if (this.containsMouse())
			parent.formTransition(0);
	}

}

class GameButton extends Component {
	String text;

	GameButton(SquareSpace p, String s, int x, int y, int w, int h) {
		super(p, x, y, w, h);
		text = s;
	}

	public void draw() {
		// try to only change on update

		parent.textSize(32);

		if (this.mouseProximity(50) || parent.superDraw) {
			parent.fill(this.containsMouse() ? parent.color(220) : parent.color(200));
			parent.rect(x, y, w, h);

			parent.textAlign(PConstants.CENTER, PConstants.CENTER);
			parent.fill(parent.color(0));
			parent.text(text, x + w / 2, y + h / 2);
		}

	}

	public void click() {
		// TODO make button specific stuff
		if (this.containsMouse())
			parent.gameState = (parent.gameState == 0 ? 1 : 0);
	}

}

class TextField extends Component {

	String text;
	int color;
	int size;

	TextField(SquareSpace parent, int x, int y, int w, int h, String msg, int color) {
		super(parent, x, y, w, h);
		text = msg;
		this.color = color;
		size = 32;
	}

	public void draw() {
		parent.textAlign(PConstants.CENTER, PConstants.CENTER);
		parent.fill(parent.color(color));
		parent.textSize(32);
		parent.text(text, x + w / 2, y + h / 2);
	}
}

class Shop extends Component {

	ShopItem[] shopSquares;
	TextField[] shopTexts;

	Shop(SquareSpace parent, int x, int y, int w, int h) {
		super(parent, x, y, w, h);

		shopSquares = new ShopItem[7];
		shopTexts = new TextField[7];

		int initialGap = 60;

		for (int i = 0; i < 7; i++) {
			shopSquares[i] = new ShopItem(parent, x + initialGap + (i % 3) * 40, y + initialGap + (i / 3) * 40, 40, 40,
					i);
			shopTexts[i] = new TextField(parent, x + initialGap + (i % 3) * 40, y + initialGap + (i / 3) * 40, 40, 40,
					"U " + i, parent.color(255));
		}

	}

	public void draw() {
		parent.fill(240);
		parent.rect(x, y, w, h);

		for (int i = 0; i < 7; i++) {
			shopSquares[i].draw();
			shopTexts[i].draw();
		}
	}

	public void click() {
		if (this.containsMouse()) {
			for (int i = 0; i < 7; i++) {
				shopSquares[i].click();
			}
		}

	}

}

class ShopItem extends Component {

	int unitID;

	int state;

	ShopItem(SquareSpace parent, int x, int y, int w, int h, int unitID) {
		super(parent, x, y, w, h);
		this.unitID = unitID;

		colours = new int[][] { { parent.color(100, 100, 200), parent.color(65, 242, 183) },
				{ parent.color(150, 150, 250), parent.color(132, 237, 202) } };

	}

	public void draw() {
		// only updates near button
		parent.fill(colours[this.containsMouse() ? 1 : 0][state]);

		parent.rect(x, y, w, h);

		parent.image(parent.unitImg[unitID], x + 5, y + 5, w - 10, h - 10);

		parent.fill(255);
		parent.textSize(12);

	}

	public void click() {
		parent.selectedUnitID = this.containsMouse() ? unitID : parent.selectedUnitID;
		state = this.containsMouse() ? 1 : 0;
	}

}
