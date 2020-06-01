import java.util.ArrayList;
import java.util.List;

import gifAnimation.*;
import processing.core.PImage;

class Form {
	// Base object for forms which everything should be layered upon
	DayBreakMain parent;

	List<Component> components = new ArrayList<Component>();

	Form(DayBreakMain p) {
		parent = p;
	}

	public void clickForm() {
		for (Component c : components)
			c.click();
	}

	public void drawComponents() {
		for (Component c : components) {
			c.draw();
			parent.stroke(0, 255);
			
		}
	}

}

class MainMenu extends Form {

	Animation anim;

	class Animation {
		// used to animate gifs, used in conjunction with GifAnimation lib

		// based on example code from processing site
		PImage[] images;
		int frame;
		DayBreakMain p;

		Animation(DayBreakMain p) {
			images = new PImage[468];
			this.p = p;
			images = Gif.getPImages(p, "BGANIM.gif");
		}

		void display(int x, int y, int w, int h) {
			frame = (frame + 1) % 468;
			p.image(images[frame], x, y, w, h);
		}

		int getWidth() {
			return images[0].width;
		}

	}

	class MenuButton extends Button {

		MainMenu m;

		MenuButton(DayBreakMain p, String s, int x, int y, int w, int h, MainMenu m) {
			super(p, x, y, w, h, s);
			this.m = m;
		}

		public void execute() {
			p.stateTransition(1);
		}

	}

	class ExitButton extends Button {
		ExitButton(DayBreakMain p, String s, int x, int y, int w, int h) {
			super(p, x, y, w, h, s);
		}

		public void execute() {
			p.exit();
		}

	}

	MainMenu(DayBreakMain p) {

		super(p);

		components.add(new MenuButton(parent, "Start", parent.width * 3 / 4, parent.height * 5 / 16, parent.width / 5,
				parent.height / 8, this));
		components.add(new Label(parent, parent.width * 47 / 64, parent.height / 7, "DAYBREAK", parent.color(255), 60));
		components.add(
				new Label(parent, parent.width * 47 / 64, parent.height / 5, "Version 15 RC4 FINAL", parent.color(255), 20));
		components.add(new ExitButton(parent, "Quit", parent.width * 3 / 4, parent.height * 12 / 24, parent.width / 5,
				parent.height / 8));
		components.add(new Label(parent, parent.width * 3 / 4, parent.height * 17 / 24, parent.width / 3,
				parent.height / 4,
				"A game by Mingde Yin\nMade for ICS3U-02\nJune 12, 2018\n\nMade using Processing 3\nWith Minim and GifAnimation libs\nMusic from Star Citizen",
				p.color(255), 16));

		anim = new Animation(parent);

	}

	public void drawComponents() {
		parent.stroke(0, 0);
		parent.fill(40);
		parent.rect(parent.width * 47 / 64, 0, parent.width / 3, parent.height / 4);
		parent.rect(components.get(4).x - 2, components.get(4).y, components.get(4).w + 2, components.get(4).h);
		anim.display(0, 0, parent.width * 45 / 64, parent.height);
		super.drawComponents();
	}

}

class NumPlayers extends Form {

	public class ContGameButton extends Button {

		ContGameButton(DayBreakMain parent, int x, int y, int w, int h, String msg) {
			super(parent, x, y, w, h, msg);
		}

		public void execute() {
			p.gameSetup();
		}

	}

	NumPlayers(DayBreakMain p) {
		super(p);

		components.add(
				new ContGameButton(p, p.width / 3, p.height * 5 / 6, p.width / 3, p.height / 10, "Proceed to game"));
		components.add(new Label(p, p.width * 5 / 12, p.height / 5, p.width / 3, p.height / 10, "Game Setup",
				p.color(255), 30));
		components.add(new TextBox(p, p.width * 5 / 12, p.height / 3, p.width / 5, p.height / 8, p.color(180),
				p.color(42), 32, TextBox.NUMERIC, "Number of Players? (2 to 8)"));
		components.add(new Label(p, p.width * 5 / 12, p.height / 2, p.width / 3, p.height / 4,
				"Arrow Keys/Mouse Drag to Pan Camera\nA/D to zoom\nSpacebar to switch mode\nMouse/clicks for everything else", p.color(255), 16));

	}

	public void drawComponents() {
		parent.stroke(0, 0);
		parent.fill(42);
		parent.rect(parent.width * 5 / 12, 0, parent.width / 3, parent.height);
		super.drawComponents();
	}

}

class GameForm extends Form {

	String msg;

	public class StatIndicator extends Label {

		// used for debugging

		StatIndicator(DayBreakMain parent, int x, int y, int w, int h, int color, int size) {
			super(parent, x, y, w, h, "", color, size);
		}

		public void draw() {
			p.fill(250);
			p.rect(x, y, w, h);
			text = "cX: " + GC.cameraX + "  cY: " + GC.cameraY + "  \nPlayers: " + GC.players.length + "   FPS: "
					+ (int) p.frameRate + "\nScale: " + GC.scale;
			super.draw();

		}

	}

	class MovePlaceSwitch extends Button {

		Grid g;

		MovePlaceSwitch(DayBreakMain p, int x, int y, int w, int h, Grid g) {
			super(p, x, y, w, h, "Switch Mode (Placing)");
			this.g = g;
		}

		public void execute() {
		 if (GC.gameState != -1) {
			GC.gameState = (GC.gameState == 0 ? 1 : (GC.gameState == 1 ? 0 : GC.gameState));
			// safe for the movement and bullet scenarios
			text = (GC.gameState == 0 ? "Switch Mode (Placing)" : "Switch Mode (Action)");
			g.deselectAll();
			}
		}
		
		public void draw() {
			if (GC.gameState != -1) {
				// try to only change on update
				p.textAlign(DayBreakMain.CENTER, DayBreakMain.CENTER);
				p.textSize(24);
				p.stroke(0, 0);
				p.fill(this.containsMouse() ? p.color(220) : p.color(200));
				p.rect(x, y, w, h);
				p.fill(p.color(42));
				p.text(text, x + w / 2, y + h / 2);
			}
				
		}

	}

	class PlayerSwitchBtn extends Button {

		int bgc;
		Grid g;

		PlayerSwitchBtn(DayBreakMain parent, int x, int y, int w, int h, int colour, Grid g) {
			super(parent, x, y, w, h, "END TURN");
			bgc = colour;
			this.g = g;

		}

		public void execute() {

			if (GC.gameState != -1) {

				GC.gameState = -1;
				parent.background(250);
				g.deselectAll();

				int cont = 0;
				for (int i = 0; i < GC.players.length; i++) {
					if (GC.players[i].active())
						cont++;

					// play until all but one are elimiated

				}

				incrementPlayer();

				for (int gx = 0; gx < g.gw; gx++) {
					for (int gy = 0; gy < g.gh; gy++) {
						g.squares[gx][gy].occupant.justPlaced = false;

					}
				}

				if (cont <= 1 && GC.turns > 0) {

					GC.winner = -1;
					for (int i = 0; i < GC.players.length; i++) {
						if (GC.players[i].active())
							GC.winner = i;

					}

					p.stateTransition(3);
					GC.turns = 0;
					// and reset grid
					p.forms[2].components.set(0, new Grid(p, p.width / 64, p.height / 14, p.width * 53 / 64,
							p.height * 5 / 6, GC.mapData[0].length(), GC.mapData.length, 0));
					GC.gameState = 1;

				} else {
					GameForm.MovePlaceSwitch b = (MovePlaceSwitch) p.forms[2].components.get(3);
					b.text = GC.turns > 0 ? "Switch Mode (Action)" : "Switch Mode (Placing)";
					// See Grid.NextBtn for rationale

					// GAME CHANGE LOGIC

					GC.shopID = -1;
				}
				
				
				// camera setting
				GC.cameraX = -1 * (Player.offsetX[GC.activePlayer] - 5) * GC.scale;
				GC.cameraY = -1 * (Player.offsetY[GC.activePlayer] - 3) * GC.scale;

				bgc = GC.ownerColours[GC.activePlayer];
			}

		}

		public void incrementPlayer() {

			if (GC.activePlayer < GC.players.length - 1) {

				GC.activePlayer++;

				// if the player is not active, move to next
				while (!GC.players[GC.activePlayer].active() && GC.turns > 0) {
					incrementPlayer(); // recursion ensures that it works well
				}
			} else {
				GC.activePlayer = 0;
				// replenish
				while (!GC.players[GC.activePlayer].active() && GC.turns > 0) {
					incrementPlayer();
				}
				for (int i = 0; i < GC.players.length; i++) {
					if (GC.players[i].active()) {
						GC.players[i].actionsRemaining = 6 + GC.turns / 2;
						// add increasingly more moves per turn
						GC.players[i].money += 500 + 400 * (GC.turns / 2);
					}

				}
				GC.turns++;
			}
		}

		public void draw() {

			if (GC.gameState != -1) {
				p.stroke(0, 0);
				p.textSize(24);

				p.fill(this.containsMouse() ? bgc + p.color(20) : bgc);
				p.rect(x, y, w, h);

				p.fill(p.color(42));
				p.text(text, x + w / 2, y + h / 2);
			}
		}

	}

	GameForm(DayBreakMain p, int mapw, int maph) {
		super(p);

		// Grid and shop/targeting
	
		components.add(new Shop(p, p.width * 54 / 64, p.height / 14, p.width * 9 / 64, p.height * 5 / 6));
		components.add(new TargetingPane(p, p.width * 54 / 64, p.height / 14, p.width * 9 / 64, p.height * 5 / 6));
		components.add(new Grid(p, p.width / 64, p.height / 14, p.width * 53 / 64, p.height * 5 / 6,
				GC.mapData[0].length(), GC.mapData.length, 0));
		components.add(new MovePlaceSwitch(p, p.width * 7 / 32, p.height * 11 / 12, p.width / 5, p.height / 14,
				(Grid) components.get(2)));

		components.add(new PlayerSwitchBtn(p, p.width / 64, p.height * 11 / 12, p.width / 5, p.height / 14,
				GC.ownerColours[GC.activePlayer], (Grid) components.get(2)));
		components.add(new StatIndicator(p, p.width * 17 / 20, 0, p.width / 10, p.height / 15, p.color(42), 10));
		components.add(new Label(p, p.width * 7 / 16, p.height * 11 / 12, p.width * 8 / 16, p.height / 14,
				"Place units from the shop. Move by clicking. Have at least one unit on the map to survive. Use the buttons to end your turn or toggle between placing and moving.",
				p.color(42), 16));

		GC.tp = (TargetingPane) components.get(1);
		// Assigne tgt pane

	}

	public void drawComponents() {
		parent.stroke(0,255);
		parent.fill(250);
		parent.rect(parent.width * 7 / 16, parent.height * 11 / 12, parent.width * 8 / 16, parent.height / 14);
		super.drawComponents();

	}

}

class EndForm extends Form {

	class MenuBtn extends Button {

		MenuBtn(DayBreakMain parent, int x, int y, int w, int h, String msg) {
			super(parent, x, y, w, h, msg);
		}

		public void execute() {
			p.stateTransition(0);
		}

	}

	EndForm(DayBreakMain p) {
		super(p);

		components.add(new Label(parent, p.width / 2, p.height / 3, "", parent.color(255), 24));
		components.add(new MenuBtn(parent, p.width / 2, p.height * 2 / 3, p.width / 5, p.height / 14, "Back to Menu"));
	}

	public void drawComponents() {
		Label l = (Label) components.get(0);
		l.text = GC.winner != -1 ? "Player " + (GC.winner + 1) + " wins!" : "No winner - no units placed!!";
		parent.background(42);
		super.drawComponents();
		parent.stroke(0, 255);
	}

}
