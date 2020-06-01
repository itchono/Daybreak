public class Grid extends Component {
	// Used as frame for square cells, as well as controlling

	int gapSpace;

	Cell[][] squares;

	int gw, gh;

	Label playerInf;
	Label moneyIndicator;
	TurnIndicator t;

	OverallIndicator o;

	NextBtn b;

	// Used for timing terrain rng and text
	int oceanTimer, textTimer;

	class NextBtn extends Button {

		NextBtn(DayBreakMain parent, int x, int y, int w, int h, String msg) {
			super(parent, x, y, w, h, msg);
		}

		public void draw() {
			text = "Player " + (GC.activePlayer + 1) + "'s Turn...";
			super.draw();
		}

		public void execute() {
			GC.gameState = GC.turns > 0 ? 1 : 0;
			// QOL thing: if they're past turn 1, they probably want to move units, so make it convenient
			
		}

	}

	class OverallIndicator extends Label {

		OverallIndicator(DayBreakMain parent, int x, int y, int boxW, int boxH, String msg, int color, int size) {
			super(parent, x, y, boxW, boxH, msg, color, size);

		}

		public void draw() {
			text = "";
			p.stroke(0, 0);
			for (int i = 0; i < GC.players.length; i++) {
				if (i == 4)
					text += "\n";

				text += "Player " + (i + 1) + ": " + GC.players[i].getFOWNum() + " sq, units: "
						+ GC.players[i].activeUnits + "      ";
			}

			p.fill(250);
			p.rect(x, 0, w, p.height * 7 / 102);
			super.draw();
		}

	}

	class TurnIndicator extends Label {

		TurnIndicator(DayBreakMain parent, int x, int y, int color, int size) {
			super(parent, x, y, "", color, size);
		}

		public void draw() {
			text = "Actions remaining this turn: " + GC.players[GC.activePlayer].actionsRemaining + " | Player "
					+ (GC.activePlayer + 1) + (GC.gameState == 0 ? " | Placement Mode" : " | Action Mode") + " | Turn "
					+ (GC.turns + 1);
			super.draw();
		}

	}

	// Constructor
	Grid(DayBreakMain p, int x, int y, int w, int h, int gw, int gh, int gapSpace) {
		super(p, x, y, w, h);
		this.gapSpace = gapSpace;
		this.gw = gw;
		this.gh = gh;

		// Initialize squares

		squares = new Cell[gw][gh];

		for (int gridX = 0; gridX < gw; gridX++) {
			for (int gridY = 0; gridY < gh; gridY++) {
				squares[gridX][gridY] = new Cell(this, gridX, gridY);
				// Applies offset of the grid component itself to each square
			}
		}

		b = new NextBtn(p, x + w / 3, y + h / 3, w / 3, h / 3, "Next Player...");

		playerInf = new Label(p, x + w / 64, y + h / 64, w / 2, h / 10, "Prometheus Archiapelago", p.color(40), 24);
		moneyIndicator = new Label(p, x + w * 4 / 5, y + h / 100, w / 5, h / 10, "Funds: ", p.color(40), 30);

		t = new TurnIndicator(p, x + w / 64, y + h * 98 / 100, p.color(42), 24);

		o = new OverallIndicator(p, p.width / 64, p.height / 256, p.width * 53 / 64, p.height / 14, "", p.color(42),
				16);
	}

	public void draw() {
		p.fill(150);
		p.rect(x, y, w, h);

		p.stroke(0, 255);

		if (GC.gameState >= 0) {
			boolean drawUI = false;

			// set presets
			for (int gx = 0; gx < gw; gx++) {
				for (int gy = 0; gy < gh; gy++) {
					if (squares[gx][gy].occupant.isSelected()) {
						drawUI = true;
					}
					if (this.containsMouse() && squares[gx][gy].cameraContainsMouse()) {
						GC.tp.loadHover(squares[gx][gy]);
					}
					else if (!this.containsMouse()) {
						GC.tp.clearHover();
					}
				}
			}
			// CELL BG
			for (int gx = 0; gx < gw; gx++) {
				for (int gy = 0; gy < gh; gy++)
					if (squares[gx][gy].x() + GC.scale < x + w - GC.cameraX
							&& squares[gx][gy].y() + GC.scale < y + h - GC.cameraY
							&& squares[gx][gy].x() > x - GC.cameraX && squares[gx][gy].y() > y - GC.cameraY)
						squares[gx][gy].drawBG();
			}
			
			p.stroke(0, 0);
			// CELL UNIT
			for (int gx = 0; gx < gw; gx++) {
				for (int gy = 0; gy < gh; gy++) {
					if (squares[gx][gy].x() + GC.scale < x + w - GC.cameraX
							&& squares[gx][gy].y() + GC.scale < y + h - GC.cameraY
							&& squares[gx][gy].x() > x - GC.cameraX && squares[gx][gy].y() > y - GC.cameraY
							&& GC.players[GC.activePlayer].FOW[gx][gy])
						// check that it is in FOW
						squares[gx][gy].drawUnit();
				}
			}

			// CELL UI
			if (drawUI) {
				// if displaying UI, need to draw an additional layer
				for (int gx = 0; gx < gw; gx++) {
					for (int gy = 0; gy < gh; gy++)
						squares[gx][gy].drawUI();
				}
			}

			// BULLET
			if (GC.gameState == 3) {
				// bullet
				p.fill(0, 255);
				p.ellipse(GC.bulletP.pCell.x() + GC.bulletX + GC.cameraX + GC.scale * 2 / 5,
						GC.bulletP.pCell.y() + GC.bulletY + GC.cameraY + GC.scale * 2 / 5, GC.scale / 5, GC.scale / 5);
			}

			p.fill(GC.ownerColours[GC.activePlayer]);
			// MASK
			p.rect(x + 1, y + 1, w - 1, (GC.scale < 40 ? 40 : GC.scale) - 1); // top
			p.rect(x + 1, y + 1, (GC.scale < 40 ? 40 : GC.scale) - 1, h - 1); // left
			p.rect(x + w - (GC.scale < 40 ? 40 : GC.scale), y + 1, (GC.scale < 40 ? 40 : GC.scale), h - 1);
			p.rect(x + 1, y + h - (GC.scale < 40 ? 40 : GC.scale), w - 1, (GC.scale < 40 ? 40 : GC.scale)); // bottom
			t.draw();

			// TIMER ROUTINES
			if (p.millis() - oceanTimer >= 1500) {
				for (int gx = 0; gx < gw; gx++) {
					for (int gy = 0; gy < gh; gy++) {
						if (squares[gx][gy].terrainType == 1)
							squares[gx][gy].bgc = GC.terrainColours[squares[gx][gy].terrainType]
									+ p.color((int) (10 * Math.random()));
					}
				}
				oceanTimer = p.millis();
			}

			if (p.millis() - textTimer >= 1500) {
				playerInf.color = p.color(42);
				locationUpdate();
				textTimer = p.millis();
				o.draw();
			}

			moneyIndicator.text = "Funds: " + GC.players[GC.activePlayer].money;
			moneyIndicator.draw();
			playerInf.draw();
		} else if (GC.gameState == -1) {
			b.draw();
			o.draw();
		}

	}

	public void locationUpdate() {

		// Some interesting names for people who provided development (moral) support
		if (mouseGridX() > 36 && mouseGridX() < 43 && mouseGridY() > 2 && mouseGridY() < 13)
			playerInf.text = "Shaft Island";
		else if (mouseGridX() > 12 && mouseGridX() < 22 && mouseGridY() > 16 && mouseGridY() < 42)
			playerInf.text = "Zhang Atoll";
		else if (mouseGridX() > 30 && mouseGridX() < 40 && mouseGridY() > 36 && mouseGridY() < 48)
			playerInf.text = "Vuong Alcove";
		else if (mouseGridX() > 28 && mouseGridX() < 36 && mouseGridY() > 16 && mouseGridY() < 31)
			playerInf.text = "Seb Isle";
		else if (mouseGridX() > 38 && mouseGridX() < 50 && mouseGridY() > 17 && mouseGridY() < 28)
			playerInf.text = "Ju Island";
		else if (mouseGridX() > 17 && mouseGridX() < 25 && mouseGridY() > 44 && mouseGridY() < 49)
			playerInf.text = "Syed's Retreat";
		else if (mouseGridX() > 2 && mouseGridX() < 35 && mouseGridY() > 3 && mouseGridY() < 45)
			playerInf.text = "Prometheus Island";

		else
			playerInf.text = "Prometheus Archipelago";
	}

	public int mouseGridX() {
		return (int) Math.floor((p.mouseX - x - GC.cameraX) / (double) (GC.scale + gapSpace));
	}

	public int mouseGridY() {
		return (int) Math.floor((p.mouseY - y - GC.cameraY) / (double) (GC.scale + gapSpace));
	}

	public void deselectAll() {
		for (int gx = 0; gx < gw; gx++) {
			for (int gy = 0; gy < gh; gy++)
				if (squares[gx][gy].x() + GC.scale < x + w - GC.cameraX
						&& squares[gx][gy].y() + GC.scale < y + h - GC.cameraY && squares[gx][gy].x() > x - GC.cameraX
						&& squares[gx][gy].y() > y - GC.cameraY) {
					if (squares[gx][gy].occupant.isSelected()) {
						squares[gx][gy].occupant.deselect();
					}
				}
		}
		Shop s = (Shop) p.forms[2].components.get(0);
		s.loadDesc(-1);
		GC.shopID = -1;

	}

	public void click() {
		// Optimizing clicking process
		if (p.mouseX > x + GC.scale && p.mouseX < x + w - GC.scale && p.mouseY > y + GC.scale
				&& p.mouseY < y + h - GC.scale && mouseGridX() >= 0 && mouseGridX() < GC.mapData[0].length()
				&& mouseGridY() >= 0 && mouseGridY() < GC.mapData.length) {
			// Bounds Check - on the physical grid object and also within cell array bounds
			if (GC.players[GC.activePlayer].actionsRemaining > 0) {
				// SHOPPING MODE
				Cell tgtCell = squares[mouseGridX()][mouseGridY()];
				if (GC.gameState == 0) {
					// Buying mode

					// refund
					if (tgtCell.occupant.exists && tgtCell.occupant.owner == GC.activePlayer
							&& tgtCell.occupant.justPlaced) {

						GC.players[GC.activePlayer].money += Integer
								.parseInt(FileIO.readStat("cost")[tgtCell.occupant.id]) * 6 / 10;
						pushMSG("Refunded for 60%. Action NOT refunded.");
						tgtCell.occupant = new Occupant();
					}

					// buy
					else if (GC.shopID != -1) {

						if (GC.hasEnoughMoney(GC.shopID)) {
							if (GC.terrainisValid(tgtCell, GC.shopID)) {
								tgtCell.occupant = new Occupant(tgtCell, GC.shopID, GC.activePlayer);
								GC.players[GC.activePlayer].money -= Integer
										.parseInt(FileIO.readStat("cost")[GC.shopID]);
								GC.players[GC.activePlayer].actionsRemaining--;
								tgtCell.occupant.updateFOW();
							}

							else {
								pushMSG("Invalid Terrain!");
							}

						} else {
							pushMSG("Insufficient funds!");
						}

					} else {
						pushMSG("Select a shop item!");
					}

				}

				// ACTION MODE
				else if (GC.gameState == 1) {
					// Action mode - lockout input otherwise
					// Select or attack/move
					if (GC.selectedUnit.exists && GC.selectedUnit.id != -1
							&& GC.selectedUnit.owner == GC.activePlayer) {
						// handle click on target
						if (tgtCell.occupant.isSelected()) {
							tgtCell.occupant.deselect();
						} else
							GC.selectedUnit.handleClick(mouseGridX(), mouseGridY());
						// p.selectedUnit.deselect(); moved elsewhere
					} else if (tgtCell.occupant.id != -1 && tgtCell.occupant.owner == GC.activePlayer) {
						// handle selection
						GC.selectedUnit.deselect(); // moved elsewhere
						tgtCell.occupant.select();

					}

				}

				// TRANSITION
				else if (GC.gameState == -1) {
					b.click();
				}
			} else {
				playerInf.color = p.color(255, 0, 0);
				pushMSG("NO ACTIONS REMAINING!");
			}

		}

		// Make it the unit or resets the square
	}

	public void pushMSG(String text) {
		playerInf.text = text;
		textTimer = p.millis();
	}

}