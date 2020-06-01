
class Grid extends Component {
	// Invisible object with no set dimensions; used as frame for square cells

	int cellSize, gapSpace;

	Cell[][] squares;

	int gw, gh;

	Label unitIndicator;

	
	// Used for timing terrain rng
	int timer;

	// Used for bullets
	int bulletX, bulletY;
	Occupant bulletP;

	// Constructor
	Grid(DayBreakMain p, int x, int y, int w, int h, int gw, int gh, int cellSize, int gapSpace) {
		super(p, x, y, w, h);
		this.cellSize = cellSize;
		this.gapSpace = gapSpace;
		this.gw = gw;
		this.gh = gh;

		// Initialize squares

		squares = new Cell[gw][gh];

		for (int gridX = 0; gridX < gw; gridX++) {
			for (int gridY = 0; gridY < gh; gridY++) {
				squares[gridX][gridY] = new Cell(this, cellSize * (gridX + gapSpace) + x,
						cellSize * (gridY + gapSpace) + y, gridX, gridY, cellSize, cellSize);
				// Applies offset of the grid component itself to each square
			}
		}

		unitIndicator = new Label(p, x + 500, y + 30, "Select a unit for more info", p.color(0), 14);

	}

	public void draw() {

		p.stroke(0, 255);
		p.fill(150);
		p.rect(x, y, w, h);

		p.stroke(0, 255);
		boolean drawUI = false;

		// SCALE UPDATING
		if (p.dScale != 0) {
			cellSize = p.scale;
			for (int gx = 0; gx < gw; gx++) {
				for (int gy = 0; gy < gh; gy++) {
					squares[gx][gy].w = cellSize;
					squares[gx][gy].h = cellSize;
					squares[gx][gy].x = gx * cellSize + x;
					squares[gx][gy].y = gy * cellSize + y;
					// set determinants
				}
			}
		}

		// COLOUR RANDOMIZATION
		if (p.millis() - timer >= 1500) {
			for (int gx = 0; gx < gw; gx++) {
				for (int gy = 0; gy < gh; gy++) {
					squares[gx][gy].bgc = DayBreakMain.terrainColours[squares[gx][gy].terrainType]
							+ p.color((int) (10* Math.random()));
				}

			}
			timer = p.millis();
		}

		// CELL BG
		for (int gx = 0; gx < gw; gx++) {
			for (int gy = 0; gy < gh; gy++)
				if (squares[gx][gy].x + cellSize < x + w - p.cameraX && squares[gx][gy].y + cellSize < y + h - p.cameraY
						&& squares[gx][gy].x > x - p.cameraX && squares[gx][gy].y > y - p.cameraY) {
					squares[gx][gy].drawBG();
					if (squares[gx][gy].occupant.isSelected()) {
						drawUI = true;
					}
				}
		}

		p.stroke(0);
		// CELL UNIT
		for (int gx = 0; gx < gw; gx++) {
			for (int gy = 0; gy < gh; gy++) {
				if (squares[gx][gy].x + cellSize < x + w - p.cameraX && squares[gx][gy].y + cellSize < y + h - p.cameraY
						&& squares[gx][gy].x > x - p.cameraX && squares[gx][gy].y > y - p.cameraY)
					squares[gx][gy].drawUnit();
			}

		}

		// CELL UI
		if (drawUI) {
			// if displaying UI, need to draw an additional layer
			for (int gx = 0; gx < gw; gx++) {
				for (int gy = 0; gy < gh; gy++)
					if (squares[gx][gy].x + cellSize < x + w - p.cameraX
							&& squares[gx][gy].y + cellSize < y + h - p.cameraY && squares[gx][gy].x > x - p.cameraX
							&& squares[gx][gy].y > y - p.cameraY)
						squares[gx][gy].drawUI();
			}
		}

		if (p.gameState == 3) {
			// bullet
			p.fill(0, 255);
			p.ellipse(bulletP.pCell.x + bulletX + p.cameraX, bulletP.pCell.y + bulletY + p.cameraY, p.scale / 6,
					p.scale / 6);
		}

		// MASK
		p.fill(220);
		p.stroke(0, 0);
		p.rect(x + 1, y + 1, w - 1, (cellSize < 40 ? 40 : cellSize) - 1); // top
		p.rect(x + 1, y + 1, (cellSize < 40 ? 40 : cellSize) - 1, h - 1); // left
		p.rect(x + w - (cellSize < 40 ? 40 : cellSize), y, (cellSize < 40 ? 40 : cellSize), h);
		p.rect(x + 1, y + h - (cellSize < 40 ? 40 : cellSize), w - 1, (cellSize < 40 ? 40 : cellSize)); // bottom

		// LABEL
		unitIndicator.draw();
	}

	public int mouseGridX() {
		return (int) Math.floor((p.mouseX - x - p.cameraX) / (double) (p.scale + gapSpace));
	}

	public int mouseGridY() {
		return (int) Math.floor((p.mouseY - y - p.cameraY) / (double) (p.scale + gapSpace));
	}

	public void deselectAll() {
		for (int gx = 0; gx < gw; gx++) {
			for (int gy = 0; gy < gh; gy++)
				if (squares[gx][gy].x + cellSize < x + w - p.cameraX && squares[gx][gy].y + cellSize < y + h - p.cameraY
						&& squares[gx][gy].x > x - p.cameraX && squares[gx][gy].y > y - p.cameraY) {
					squares[gx][gy].drawBG();
					if (squares[gx][gy].occupant.isSelected()) {
						squares[gx][gy].occupant.deselect();
					}
				}
		}
	}
	
	public void click() {
		// Optimizing clicking process
		if (p.mouseX > x + cellSize && p.mouseX < x + w - cellSize && p.mouseY > y + cellSize
				&& p.mouseY < y + h - cellSize) {
			// Bounds Check

			Cell tgtCell = squares[mouseGridX()][mouseGridY()];
			if (p.gameState == 0 && DayBreakMain.shopID != -1
					&& DayBreakMain.terrainisValid(tgtCell, DayBreakMain.shopID)) {

				// Buying mode

				tgtCell.occupant = tgtCell.occupant.id == -1
						? new Occupant(tgtCell, DayBreakMain.shopID, mouseGridX(), mouseGridY(), p.activePlayer)
						: (tgtCell.occupant.justPlaced ? new Occupant(tgtCell, -1, mouseGridX(), mouseGridY(), -1)
								: tgtCell.occupant);
			}

			else if (p.gameState == 1) {
				// Action mode - lockout input otherwise
				// Select or attack/move
				if (p.selectedUnit.exists && p.selectedUnit.id != -1 && p.selectedUnit.owner == p.activePlayer) {
					// handle click on target
					if (tgtCell.occupant.isSelected()) {
						tgtCell.occupant.deselect();
					} else
						p.selectedUnit.handleClick(mouseGridX(), mouseGridY());
					// p.selectedUnit.deselect(); moved elsewhere
				} else if (tgtCell.occupant.id != -1 && tgtCell.occupant.owner == p.activePlayer) {
					// handle selection
					p.selectedUnit.deselect(); // moved elsewhere
					tgtCell.occupant.select();

				}

			}

		}
		p.superDraw();

		// Make it the unit or resets the square
	}

	public void fillOccupants() {
		// TODO fills with empty occupants by default for now, add players later
		for (int gx = 0; gx < gw; gx++) {
			for (int gy = 0; gy < gh; gy++) {
				squares[gx][gy].occupant = new Occupant(squares[gx][gy], -1, gx, gy, -1);
			}
		}
	}

}