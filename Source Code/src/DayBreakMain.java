// DAYBREAK

// A Game by Mingde Yin

// ICS3U - 02 Final Project
// Build 15 RC4 - built on June 12, 2018 (Final Version)
// Developed from Apr 20 to June 12
// approx 2200 lines
// A slightly polished version with more QOL stuff and a new minor game mechanic

// Most of the code is written by me, external code is noted as such.
// All of the components in each of the forms were painstakingly hand coded because external libraries were too hard to use

// All units and the map are read from files, so they can be freely modded (even without the source code)
// The dynamic nature of this is probably the coolest part of the project.

import ddf.minim.*; // Audio
import processing.core.*; // Processing framework
import processing.event.MouseEvent;

public class DayBreakMain extends PApplet {

	public Form[] forms = new Form[10];

	public Minim sound;

	public AudioPlayer player;
	AudioInput input;

	public TextBox txtIn;
	// used to relay directly to the textbox

	private int state;
	// state of the program
	// 9: loading 0: main menu 1: playerselection 2: game 3: end

	public static void main(String[] args) {
		PApplet.main("DayBreakMain");
	}

	public void settings() {
		setSize(1280, 720);
	}

	public void setup() {
		background(40);
		surface.setTitle("Daybreak");
		surface.setIcon(loadImage("MainIcon.png"));
		state = 9;
		textSize(22);
		fill(255);
		text("Loading, Please Wait (It may take a while)...", width / 3, height / 2);
		forms[9] = new Form(this); // Blank form for loading
	}

	public void init() {
		GC.selectedUnit = new Occupant();
		textSize(32);
		textAlign(PConstants.CENTER, PConstants.CENTER);
		this.strokeWeight(1);

		GC.ownerColours = new int[] { color(200, 102, 102), color(109, 158, 200), color(147, 196, 125),
				color(200, 133, 27), color(142, 124, 195), color(200, 200, 50), color(50, 200, 169),
				color(200, 50, 165) };
		GC.terrainColours = new int[] { color(92, 165, 39), color(34, 66, 145), color(232, 247, 246) };
		GC.terrainBorders = new int[] { color(62, 155, 19), color(14, 46, 135), color(232, 247, 246) };
		// earth, water, air

		GC.mapData = loadStrings("Prometheus Archipelago.tsv");

		for (int i = 0; i < GC.mapData.length; i++) {
			GC.mapData[i] = GC.mapData[i].replaceAll("\t", "");
		}
		// map setup
		System.out.println("Loading files");

		GC.unitImg = new PImage[FileIO.readStat("name").length];
		System.out.println("Loading Units - " + GC.numUnits() + " units found!\n");

		// UNITS LOADING
		for (int i = 0; i < GC.numUnits(); i++) {
			// load sprites VERY IMPORTNAT DO NOT DELETE
			GC.unitImg[i] = loadImage(FileIO.readStat("image")[i]); // this one actually loads stuff

			System.out.println("Name: " + FileIO.readStat("name")[i]);
			System.out.println("Attack: " + FileIO.readStat("attack")[i]);
			System.out.println("Move Range: " + FileIO.readStat("moveRange")[i]);
			System.out.println("Attack Range: " + FileIO.readStat("atkRange")[i]);
		}
		

		System.out.println("Loading forms");
		forms[2] = new GameForm(this, GC.mapData[0].length(), GC.mapData.length);
		forms[0] = new MainMenu(this);
		forms[1] = new NumPlayers(this);
		forms[3] = new EndForm(this);

		txtIn = new TextBox();

		ellipseMode(CORNER);
		GC.activePlayer = 0;
		GC.scale = 40;
		
		GC.winner = -1;

		// referencing to GC

		System.out.println("Init Audio...");
		sound = new Minim(this);
		player = sound.loadFile("data/Menu.mp3");
		input = sound.getLineIn();
		player.play();
		player.loop();

		GC.player = player;
		GC.sound = sound;
	}

	public void draw() {
		
		

		// Routines
		switch (state) {
		case 9:
			init();
			state = 0;
			break;
		case 1:
		case 2:

			if (GC.dcX != 0 || GC.dcY != 0) {
				GC.cameraX += GC.dcX;
				GC.cameraY += GC.dcY;
				// smoother camera action
			}

			if (GC.dScale != 1 && (GC.scale >= 20 || GC.dScale > 1) && (GC.scale <= 100 || GC.dScale < 1)) {

				GC.scale = (int) (GC.scale * GC.dScale);

				GC.cameraX -= mouseX;
				GC.cameraY -= mouseY;

				GC.cameraX = (int) (GC.cameraX * GC.dScale);
				GC.cameraY = (int) (GC.cameraY * GC.dScale);

				GC.cameraX += mouseX;
				GC.cameraY += mouseY;
				

			}
			
			break;
		}

		// Drawing
		forms[state].drawComponents();

	}

	public void mouseClicked() {
		// was formerly pressed, changed to released because of drag scroll
		// must now be a deliberate action, and much less random accidental movement
		forms[state].clickForm();
	}

	public void mouseDragged() {
		if (state == 2) {

			GC.cameraX += mouseX - pmouseX;
			GC.cameraY += mouseY - pmouseY;
		}
	}

	// Move to game
	public void gameSetup() {
		GC.players = new Player[txtIn.getInt() >= 2 && txtIn.getInt() <= 8 ? txtIn.getInt() : 2];
		for (int i = 0; i < GC.players.length; i++)
			GC.players[i] = new Player(6, 4500, GC.mapData[0].length(), GC.mapData.length, i);

		player.pause();
		player = sound.loadFile("data/Game.mp3");
		player.play();
		player.loop();
		stateTransition(2);
	}

	public void keyPressed() {
		switch (state) {
		case 1:
			if (txtIn.active) {
				switch (keyCode) {
				case BACKSPACE:
					txtIn.bksp();
					break;
				default:
					txtIn.receiveIn((char) keyCode);
				}

			}

			break;
		case 2:
			switch (keyCode) {
			case UP:
				GC.dcY = GC.scale / 4;
				break;
			case DOWN:
				GC.dcY = GC.scale / -4;
				break;
			case LEFT:
				GC.dcX = GC.scale / 4;
				break;
			case RIGHT:
				GC.dcX = GC.scale / -4;
				break;
			case 65:
				GC.dScale = 1.1;
				break;
			case 68:
				GC.dScale = 0.9;
				break;
			case 32:
				
				Button b = (Button) forms[2].components.get(3);
				b.execute();
				break;
			}

			break;
		}

	}

	public void keyReleased() {
		switch (state) {
		case 2:
			if ((keyCode == UP && GC.dcY > 0) || (keyCode == DOWN && GC.dcY < 0))
				GC.dcY = 0;

			if ((keyCode == LEFT && GC.dcX > 0) || (keyCode == RIGHT && GC.dcX < 0))
				GC.dcX = 0;
			if ((keyCode == 65 && GC.dScale > 1) || (keyCode == 68 && GC.dScale < 1))
				GC.dScale = 1;
			break;
		}
	}

	public void mouseWheel(MouseEvent e) {
		GC.dScale = e.getCount() > 1 ? 0.9 : (e.getCount() < -1 ? 1.1 : 1);
	}

	public void stateTransition(int newState) {
		state = newState;
		background(newState == 2 ? 250 : 42);
	}

}
