
import processing.core.*;

public class SquareSpace extends PApplet {

	Form[] forms = new Form[10];

	PImage[] unitImg;
	// Images, all loaded at startup


	int activeForm = -1;
	// used in conjunction with array
	
	Occupant selectedUnit = new Occupant(this, -1, -1);

	boolean superDraw;

	int gameState;

	int cameraX, cameraY, scale;
	// TODO - make camera pan + zoom

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("SquareSpace");
	}

	public void settings() {
		setSize(1280, 720);
	}

	public void setup() {
		unitImg = new PImage[] { loadImage("ElementanTank.png"), loadImage("Equanos Howitzer.png"),
				loadImage("EquanosTank.png"), loadImage("InfernusTank.png"), loadImage("PrismaTank.png"),
				loadImage("Surface Missile Battery.png"), loadImage("UmbraTank.png") };
		// Load operations
		background(200, 200, 200);

		textAlign(PConstants.CENTER, PConstants.CENTER);
		forms[0] = new GameForm(this, color(255, 255, 250));
		forms[1] = new MainMenu(this, color(200, 200, 200));

		activeForm = 1;
		// Menu

		gameState = 0;
		fill(255, 255, 255);

		// fill in current form

		superDraw = true;
		forms[activeForm].fillDraw();
		superDraw = false;

	}

	int selectedUnitID = -1;
	// Selected unitID

	public void draw() {
		forms[activeForm].drawComponents();
	}

	public void mouseClicked() {

		forms[activeForm].clickForm();

	}

	public void keyPressed() {
		if (keyCode == UP) {
			cameraY += 5;
			superDraw();
		} else if (keyCode == DOWN) {
			cameraY -= 5;
			superDraw();
		}

		if (keyCode == LEFT) {
			cameraX += 5;
			superDraw();
		} else if (keyCode == RIGHT) {
			cameraX -= 5;
			superDraw();
		}
		
		if (keyCode == 119 || keyCode == 87 && gameState == 1) {
			selectedUnit.move(selectedUnit.gx, selectedUnit.gy - 1);
			System.out.println("MOVED");
			
		}
	}

	public void formTransition(int newForm) {
		activeForm = newForm;

		background(200, 200, 200);

		textAlign(LEFT);
		superDraw();

	}

	public void superDraw() {
		superDraw = true;
		forms[activeForm].fillDraw();
		draw();
		superDraw = false;
	}

}

class Occupant {
	// Base class for units and buildings
	int id, gx, gy;
	SquareSpace parent;

	int owner;

	public Occupant(SquareSpace parent, int id, int owner) {
		this.parent = parent;
		this.id = id;
		this.owner = owner;
	}
	
	public void setCoords(int gx, int gy) {
		this.gx = gx;
		this.gy = gy;
	}

	public void move(int newgx, int newgy) {
		for (Component c : parent.forms[parent.activeForm].components) {
			if (c.toString().contains("Grid")) {
				Grid newGrid = (Grid) c;
				newGrid.squares[newgx][newgy].occupant = this;
				newGrid.squares[gx][gy].occupant = new Occupant(parent, -1, -1);
				gx = newgx;
				gy = newgy;

			}
		}

	}
}
