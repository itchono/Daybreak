
import ddf.minim.*;
import gifAnimation.Gif;
import processing.core.*;

public class DayBreakMain extends PApplet {

	Form[] forms = new Form[10];

	Minim sound;

	AudioPlayer player;
	AudioInput input;

	PImage[] unitImg;
	// Images, all loaded at startup

	Gif loopingGif;

	private int activeForm = -1;
	// used in conjunction with array

	static int[] ownerColours;
	static int[] terrainColours;
	static int[] terrainBorders;
	static int[][] unitStats;

	final static int HP = 0;
	final static int ATK = 1;
	

	static String[] mapData;

	Player players[];

	int activePlayer;

	Occupant selectedUnit;

	String tempinput = "0";

	boolean superDraw;

	int gameState;
	// game state
	// 0 unit placing
	// 1 unit moving
	// 2 moving animation
	// 3 atk animation

	int state;
	// state of the program
	// 0: loading 1: main menu 2: game
	
	static int shopID = -1;
	// Selected unitID

	int cameraX, cameraY;

	int dcX, dcY;
	// for cam movement

	int timing;

	int scale, dScale;

	public static void main(String[] args) {
		PApplet.main("DayBreakMain");
	}

	public void settings() {
		setSize(1280, 720);
	}

	public void setup() {
		background(40);
		state = 0;
		surface.setTitle("Mingde Yin - Daybreak");
		surface.setIcon(loadImage("MainIcon.png"));

	}

	public void init() {
		selectedUnit = new Occupant();
		textSize(32);
		textAlign(PConstants.CENTER, PConstants.CENTER);
		this.strokeWeight(1);

		ownerColours = new int[] { color(205, 67, 54), color(156, 39, 176), color(63, 81, 181), color(76, 175, 80) };
		terrainColours = new int[] { color(92, 165, 39), color(34, 66, 145), color(232, 247, 246) };
		terrainBorders = new int[] { color(62, 155, 19), color(14, 46, 135), color(232, 247, 246) };

		// earth, water, air

		mapData = loadStrings("Map.tsv");

		for (int i = 0; i < mapData.length; i++) {
			mapData[i] = mapData[i].replaceAll("\t", "");
		}
		// map setup

		System.out.println("Init Audio...");
		sound = new Minim(this);
		player = sound.loadFile("data/Menu.mp3");
		input = sound.getLineIn();
		player.play(71000);

		System.out.println("Loading files");
		
		unitImg = new PImage[FileIO.readStat("name").length];
		System.out.println("Loading Units - " + numUnits() + " units found!\n");
		
		
		// UNITS LOADING
		for (int i = 0; i < numUnits(); i++) {
			// load sprites VERY IMPORTNAT DO NOT DELETE
			unitImg[i] = loadImage(FileIO.readStat("image")[i]); // this one acutally loads stuff
			
			System.out.println("Name: " + FileIO.readStat("name")[i]);
			System.out.println("Attack: " + FileIO.readStat("attack")[i]);
			System.out.println("Move Range: " + FileIO.readStat("moveRange")[i]);
			System.out.println("Attack Range: " + FileIO.readStat("attackRange")[i]);
		}

		// loading unit stats
		System.out.println("Loading Other Images");
		loopingGif = new Gif(this, "BGANIM.gif");
		// Load operations

		System.out.println("Loading forms");
		forms[0] = new GameForm(this, mapData[0].length(), mapData.length);
		forms[1] = new MainMenu(this);
		forms[2] = new NumPlayers(this);
		forms[3] = new EndForm(this);

		ellipseMode(CORNER);

		activeForm = 1;
		// Menu

		activePlayer = 0;
		scale = 40;
	}

	

	public void draw() {
		switch (state) {
		case 0:
			// splash screen
			textSize(22);
			fill(255);
			text("Loading, Please Wait (It may take a while)...", 400, 400);
			if (millis() > 1500) {
				init();
				state = 1;
			}
			break;

		case 1:
		case 2:
			if (dcX != 0 || dcY != 0) {
				cameraX += dcX;
				cameraY += dcY;
				// smoother camera action
			}

			if (dScale != 0 && (scale >= 20 || dScale > 0) && (scale <= 100 || dScale < 0)) {
				scale += dScale;
			}

			forms[activeForm].drawComponents();
			break;
		}

	}

	public void mouseClicked() {
		if (activeForm >= 0) {
			forms[activeForm].clickForm();
		}

	}

	public void gameSetup() {
		players = new Player[(tempinput.matches("-?\\d+(\\.\\d+)?") && Integer.parseInt(tempinput) > 0
				&& Integer.parseInt(tempinput) < 20) ? Integer.parseInt(tempinput) : 2];
		for (int i = 0; i < players.length; i++)
			players[i] = new Player();

		state = 2;
		formTransition(0);
	}
	
	public static int numUnits() {
		return FileIO.readStat("name").length;
	}

	public void keyPressed() {
		switch (state) {
		case 1:
			if (key == ENTER || key == RETURN) {
				gameSetup();
			} else if (key == BACKSPACE)
				tempinput = tempinput.substring(0, tempinput.length() - 2);
			else
				tempinput = tempinput + key;
			break;
		case 2:
			switch (keyCode) {
			case UP:
				dcY = scale/4;
				break;
			case DOWN:
				dcY = scale/-4;
				break;
			case LEFT:
				dcX = scale/4;
				break;
			case RIGHT:
				dcX = scale/-4;
				break;
			case 65:
				dScale = 2;
				break;
			case 68:
				dScale = -2;
				break;
			}

			break;
		}

	}

	public void keyReleased() {
		switch (state) {
		case 2:
			if ((keyCode == UP && dcY > 0)||(keyCode == DOWN && dcY < 0))
				dcY = 0;

			if ((keyCode == LEFT && dcX > 0)||(keyCode == RIGHT && dcX < 0))
				dcX = 0;
			if ((keyCode == 65 && dScale > 0) || (keyCode == 68 && dScale < 0))
				dScale = 0;

			superDraw();
			break;
		}
	}

	public void formTransition(int newForm) {
		activeForm = newForm;
		background(42);
		textAlign(LEFT);
		superDraw();

	}

	public void superDraw() {
		superDraw = true;
		draw();
		superDraw = false;
	}
	
	public static boolean terrainisValid(Cell tgt, int id) {
		return Integer.parseInt(FileIO.readStat("terrainType")[id]) == tgt.terrainType || Integer.parseInt(FileIO.readStat("terrainType")[id]) ==2;
	}

}
