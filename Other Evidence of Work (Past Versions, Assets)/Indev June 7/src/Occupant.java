class Occupant {
	// Base class for units and buildings
	int id, gx, gy;
	Cell pCell;

	Cell tgt;
	boolean exists;

	Stats stats;

	final static int attack = 0;
	final static int moveRange = 1;
	final static int attackRange = 2;
	final static int terrainType = 3;

	int owner;

	int moveX, moveY;

	DayBreakMain p;

	ChoiceIndicator[][] indicators;

	boolean justPlaced;
	// how long ago it was placed TODO

	public void setCoords(int gx, int gy) {
		this.gx = gx;
		this.gy = gy;
	}

	public Occupant(Cell parent, int id, int gx, int gy, int owner) {
		pCell = parent;
		p = pCell.p;
		this.id = id;
		setCoords(gx, gy);

		exists = true;

		this.owner = owner;

		stats = new Stats();

		// STAT LOAD

		if (id >= 0) {
			String query = "";

			for (int statType = 0; statType < 4; statType++) {
				switch (statType) {
				case attack:
					query = "attack";
					break;
				case moveRange:
					query = "moveRange";
					break;
				case attackRange:
					query = "attackRange";
					break;
				case terrainType:
					query = "terrainType";
					break;

				}
				stats.vals[statType] = Integer.parseInt(FileIO.readStat(query)[id]);
				stats.name = FileIO.readStat("name")[id];

			}
		}

	}

	public Occupant() {
		// sets all fields to -1
		// Graveyard constructor
		p = new DayBreakMain();
		id = -1;
		gx = -1;
		gy = -1;
		exists = false;
		owner = -1;
	}

	
	public void select() {

		if (p.selectedUnit.equals(this)) {
			deselect();
			// blank occupant
		} else if (p.players[p.activePlayer].actionsRemaining > 0) {

			p.selectedUnit = this;

			pCell.pGrid.unitIndicator.text = stats.name + "-- Move Range: " + stats.vals[moveRange] + "  Atk Range: "
					+ stats.vals[attackRange];

			// Creation of actionchoice indicators
			indicators = new ChoiceIndicator[2 * maxRange() + 1][2 * maxRange() + 1];

			for (int dx = 0; dx < 2 * maxRange() + 1; dx++) {
				for (int dy = 0; dy < 2 * maxRange() + 1; dy++) {

					// bounds check
					// somewhat complicated code
					if (dx + pCell.gridX - maxRange() >= 0 && dx + pCell.gridX - maxRange() < pCell.pGrid.squares.length
							&& dy + pCell.gridY - maxRange() >= 0
							&& dy + pCell.gridY - maxRange() < pCell.pGrid.squares[0].length) {
						Cell targetSquare = pCell.pGrid.squares[dx + pCell.gridX - maxRange()][dy + pCell.gridY
								- maxRange()];
						// The square that is being scanned

						int range = targetSquare.occupant.id != -1 ? stats.vals[attackRange] : stats.vals[moveRange];
						int indC = targetSquare.occupant.id != -1 ? p.color(200, 0, 0, 100)
								: p.color(122, 195, 255, 100);
						// corresponding colours
						if (dx >= maxRange() - range && dx <= maxRange() + range && dy >= maxRange() - range
								&& dy <= maxRange() + range && targetSquare.occupant.owner != p.activePlayer
								&& (targetSquare.occupant.id != -1 ? true : DayBreakMain.terrainisValid(targetSquare, id))) {
							// if it's a valid indicator
							indicators[dx][dy] = new ChoiceIndicator(this, targetSquare,
									targetSquare.occupant.id != -1 ? ChoiceIndicator.ATTACK : ChoiceIndicator.MOVE);
							indicators[dx][dy].color = indC;
						} else {
							indicators[dx][dy] = new ChoiceIndicator();
							// graveyard indicator
						}
					} else {
						indicators[dx][dy] = new ChoiceIndicator();
						// empty indicator
					}

				}

			}

			// Render action indicators
		} else if (p.players[p.activePlayer].actionsRemaining <= 0) {
			// idk do the thing
		}

	}

	public void deselect() {
		if (exists) {
			p.selectedUnit = new Occupant();
			pCell.pGrid.unitIndicator.text = "Select a unit for more info";
		}

	}

	public boolean isSelected() {
		return this == p.selectedUnit;
	}

	public int maxRange() {
		return stats.vals[moveRange] >= stats.vals[attackRange] ? stats.vals[moveRange] : stats.vals[attackRange];
	}

	public void drawComponents() {

		for (int dx = 0; dx < indicators.length; dx++) {
			for (int dy = 0; dy < indicators.length; dy++) {
				if (indicators[dx][dy].exists
						&& indicators[dx][dy].tgt.x + pCell.pGrid.cellSize < pCell.pGrid.x + pCell.pGrid.w
								- pCell.pGrid.p.cameraX
						&& indicators[dx][dy].tgt.y + pCell.pGrid.cellSize < pCell.pGrid.y + pCell.pGrid.h
								- pCell.pGrid.p.cameraY
						&& indicators[dx][dy].tgt.x > pCell.pGrid.x - pCell.pGrid.p.cameraX
						&& indicators[dx][dy].tgt.y > pCell.pGrid.y - pCell.pGrid.p.cameraY) {
					indicators[dx][dy].draw();
				}
			}
		}
	}

	public void handleClick(int gx, int gy) {
		// responds to range stuff
		int convX = gx - pCell.gridX + maxRange();
		int convY = gy - pCell.gridY + maxRange();

		if (convX >= 0 && convX < indicators.length && convY >= 0 && convY < indicators.length
				&& p.players[p.activePlayer].actionsRemaining > 0) {

			if (indicators[convX][convY].role == ChoiceIndicator.ATTACK && !(pCell.gridX == gx && pCell.gridY == gy)) {
				attack(gx, gy);
				p.players[p.activePlayer].actionsRemaining--;

			} else if (indicators[convX][convY].role == ChoiceIndicator.MOVE) {
				move(gx, gy);
				p.players[p.activePlayer].actionsRemaining--;
			} else if (pCell.pGrid.squares[gx][gy].occupant.owner == owner) {
				pCell.pGrid.squares[gx][gy].occupant.select();
			}

		} else if (pCell.pGrid.squares[gx][gy].occupant.owner == owner) {
			pCell.pGrid.squares[gx][gy].occupant.select();
		}

	}

	public void attack(int tgx, int tgy) {
		// TODO - kill code
		tgt = pCell.pGrid.squares[tgx][tgy];
		p.gameState = 3;
		pCell.pGrid.bulletX = pCell.p.scale/2;
		pCell.pGrid.bulletY = pCell.p.scale/2;
		pCell.pGrid.bulletP = this;
	}
	
	public void atkAnim() {
		if (Math.abs(tgt.x - pCell.x  + pCell.w/2) - Math.abs(pCell.pGrid.bulletX) < 5 && Math.abs(tgt.y - pCell.y  + pCell.w/2) - Math.abs(pCell.pGrid.bulletY) < 5) {
			p.gameState = 1;
			// return to movement
			tgt.occupant.destroy();
			p.superDraw();
			pCell.pGrid.bulletX = 0;
			pCell.pGrid.bulletY = 0;
			this.select();
		}
		else {
			int vel = 20;
			if (Math.abs(tgt.x - pCell.x + pCell.w/2) - Math.abs(pCell.pGrid.bulletX) >= 5)
				pCell.pGrid.bulletX += tgt.x - pCell.x < 0 ? -1 * Math.abs(tgt.x - pCell.x) / vel : Math.abs(tgt.x - pCell.x) / vel;

			if (Math.abs(tgt.y - pCell.y + pCell.w/2) - Math.abs(pCell.pGrid.bulletY) >= 5)
				pCell.pGrid.bulletY += tgt.y - pCell.y < 0 ? -1 * Math.abs(tgt.y - pCell.y) / vel : Math.abs(tgt.y - pCell.y) / vel;
				
		}
			
	}
	
	public void move(int tgx, int tgy) {
		// initiates move

		tgt = pCell.pGrid.squares[tgx][tgy];
		p.gameState = 2;
		p.selectedUnit = this;
		// Motion

	}

	public void moveAnim() {
		// makes the unit move
		if (Math.abs(tgt.x - pCell.x) - Math.abs(moveX) < 5 && Math.abs(tgt.y - pCell.y) - Math.abs(moveY) < 5) {
			p.gameState = 1;
			pCell = tgt;
			tgt.occupant = this;

			// old gx
			pCell.pGrid.squares[gx][gy].occupant = new Occupant(pCell.pGrid.squares[gx][gy], -1, gx, gy, -1);
			// new gx
			gx = tgt.gridX;
			gy = tgt.gridY;

			moveX = 0;
			moveY = 0;
			this.select();
			p.superDraw();

		} else {

			int vel = 20;
			// Add move commands
			if (Math.abs(tgt.x - pCell.x) - Math.abs(moveX) >= 5)
				moveX += tgt.x - pCell.x < 0 ? -1 * Math.abs(tgt.x - pCell.x) / vel : Math.abs(tgt.x - pCell.x) / vel;

			if (Math.abs(tgt.y - pCell.y) - Math.abs(moveY) >= 5)
				moveY += tgt.y - pCell.y < 0 ? -1 * Math.abs(tgt.y - pCell.y) / vel : Math.abs(tgt.y - pCell.y) / vel;

		}

	}


	public void destroy() {
		// Note: not for movement
		// resets to empty
		this.id = -1;
		// empty unit
		this.owner = -1;

		// maybe add rubble
	}

}