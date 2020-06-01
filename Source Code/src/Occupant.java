public class Occupant {
	// Base class for units and buildings
	int id;
	Cell pCell;

	Cell tgt;
	boolean exists;

	boolean justPlaced;

	Stats stats;

	class Stats {

		Occupant p;
		int[] vals;
		// 5 vals
		int maxHP;
		String name;

		public Stats() {
			vals = new int[5];
		}

	}

	final static int attack = 0;
	final static int moveRange = 1;
	final static int attackRange = 2;
	final static int terrainType = 3;
	final static int hp = 4;

	int owner;

	int moveX, moveY;

	DayBreakMain p;

	ChoiceIndicator[][] indicators;

	class ChoiceIndicator {

		int color;

		int role;

		Occupant unit;
		Cell tgt;

		final static int ATTACK = 1;
		final static int MOVE = 2;

		boolean exists;

		ChoiceIndicator(Occupant unit, Cell targetSquare, int role) {
			exists = true;
			this.role = role;

			this.unit = unit;
			tgt = targetSquare;
		}

		ChoiceIndicator() {
			// empty constructor
			exists = false;
		}

		public void draw() {

			if (exists) {
				// Action prompt types

				p.stroke(0, 0);
				p.fill(color);
				p.ellipse(tgt.x() + GC.cameraX, tgt.y() + GC.cameraY, GC.scale, GC.scale);
			}

		}

	}

	// Inner class for indicators

	public Occupant(Cell parent, int id, int owner) {
		pCell = parent;
		p = pCell.pGrid.p;
		this.id = id;

		exists = true;

		this.owner = owner;

		stats = new Stats();

		justPlaced = true;

		// STAT LOAD

		if (id >= 0) {
			String query = "";

			for (int statType = 0; statType < 5; statType++) {
				switch (statType) {
				case attack:
					query = "attack";
					break;
				case moveRange:
					query = "moveRange";
					break;
				case attackRange:
					query = "atkRange";
					break;
				case terrainType:
					query = "terrainType";
					break;
				case hp:
					query = "hp";
					break;

				}
				stats.vals[statType] = Integer.parseInt(FileIO.readStat(query)[id]);
				stats.name = FileIO.readStat("name")[id];
				stats.maxHP = Integer.parseInt(FileIO.readStat("hp")[id]);

			}

			GC.players[GC.activePlayer].activeUnits++;
		}

	}

	public Occupant() {
		// sets all fields to -1
		// Graveyard constructor
		p = new DayBreakMain();
		id = -1;
		exists = false;
		owner = -1;
	}

	public void select() {

		if (GC.selectedUnit.equals(this)) {
			deselect();
			// blank occupant
		} else if (GC.players[GC.activePlayer].actionsRemaining > 0) {

			GC.selectedUnit = this;

			GC.tp.loadSelect(this);

			// Creation of actionchoice indicators
			indicators = new ChoiceIndicator[2 * maxRange() + 1][2 * maxRange() + 1];

			for (int dx = 0; dx < 2 * maxRange() + 1; dx++) {
				for (int dy = 0; dy < 2 * maxRange() + 1; dy++) {

					// bounds check
					// somewhat complicated code
					if (dx + pCell.gridX - maxRange() >= 0 && dx + pCell.gridX - maxRange() < GC.mapData[0].length()
							&& dy + pCell.gridY - maxRange() >= 0
							&& dy + pCell.gridY - maxRange() < GC.mapData.length) {
						Cell targetSquare = pCell.pGrid.squares[dx + pCell.gridX - maxRange()][dy + pCell.gridY
								- maxRange()];
						// The square that is being scanned

						int range = targetSquare.occupant.id > -1 ? stats.vals[attackRange] : stats.vals[moveRange];
						int indC = targetSquare.occupant.id > -1 ? p.color(200, 0, 0, 100)
								: p.color(122, 195, 255, 100);
						// corresponding colours
						if (dx >= maxRange() - range && dx <= maxRange() + range && dy >= maxRange() - range
								&& dy <= maxRange() + range && targetSquare.occupant.owner != GC.activePlayer
								&& (targetSquare.occupant.id > -1 ? true : GC.terrainisValid(targetSquare, id))
								&& GC.players[GC.activePlayer].FOW[targetSquare.gridX][targetSquare.gridY]) {
							// if it's a valid indicator
							indicators[dx][dy] = new ChoiceIndicator(this, targetSquare,
									targetSquare.occupant.id > -1 ? ChoiceIndicator.ATTACK : ChoiceIndicator.MOVE);
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
		} else if (GC.players[GC.activePlayer].actionsRemaining <= 0) {
			// idk do the thing
		}

	}

	public void deselect() {
		if (exists) {
			GC.selectedUnit = new Occupant();
			GC.tp.clearSelect();
		}

	}

	public boolean isSelected() {
		return this == GC.selectedUnit;
	}

	public int maxRange() {
		return stats.vals[moveRange] >= stats.vals[attackRange] ? stats.vals[moveRange] : stats.vals[attackRange];
	}

	public void drawComponents() {

		for (int dx = 0; dx < indicators.length; dx++) {
			for (int dy = 0; dy < indicators.length; dy++) {
				if (indicators[dx][dy].exists
						&& indicators[dx][dy].tgt.x() + GC.scale < pCell.pGrid.x + pCell.pGrid.w - GC.cameraX
						&& indicators[dx][dy].tgt.y() + GC.scale < pCell.pGrid.y + pCell.pGrid.h - GC.cameraY
						&& indicators[dx][dy].tgt.x() > pCell.pGrid.x - GC.cameraX
						&& indicators[dx][dy].tgt.y() > pCell.pGrid.y - GC.cameraY) {

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
				&& GC.players[GC.activePlayer].actionsRemaining > 0) {

			if (indicators[convX][convY].role == ChoiceIndicator.ATTACK && !(pCell.gridX == gx && pCell.gridY == gy)) {
				attack(gx, gy);
				GC.players[GC.activePlayer].actionsRemaining--;

			} else if (indicators[convX][convY].role == ChoiceIndicator.MOVE) {
				move(gx, gy);
				GC.players[GC.activePlayer].actionsRemaining--;
			} else if (pCell.pGrid.squares[gx][gy].occupant.owner == owner) {
				pCell.pGrid.squares[gx][gy].occupant.select();
			}

		} else if (pCell.pGrid.squares[gx][gy].occupant.owner == owner) {
			pCell.pGrid.squares[gx][gy].occupant.select();
		}

	}

	public void attack(int tgx, int tgy) {
		if (justPlaced)
			justPlaced = false;

		tgt = pCell.pGrid.squares[tgx][tgy];
		GC.gameState = 3;
		GC.bulletX = 0;
		GC.bulletY = 0;
		GC.bulletP = this;
		GC.player = GC.sound.loadFile("data/TankFire.mp3");
		GC.player.play();
		// crude audio implementation
	}

	public void atkAnim() {
		if (Math.abs(tgt.x() - pCell.x()) - Math.abs(GC.bulletX) < 2
				&& Math.abs(tgt.y() - pCell.y()) - Math.abs(GC.bulletY) < 2) {
			GC.gameState = 1;
			// return to movement
			tgt.occupant.applyATK(this);
			GC.bulletX = 0;
			GC.bulletY = 0;
			this.select();
		} else {
			int vel = 10;
			if (Math.abs(tgt.x() - pCell.x()) - Math.abs(GC.bulletX) >= 2)
				GC.bulletX += tgt.x() < pCell.x() ? -1 * Math.abs(tgt.x() - pCell.x()) / vel
						: Math.abs(tgt.x() - pCell.x()) / vel;

			if (Math.abs(tgt.y() - pCell.y()) - Math.abs(GC.bulletY) >= 2)
				GC.bulletY += tgt.y() < pCell.y() ? -1 * Math.abs(tgt.y() - pCell.y()) / vel
						: Math.abs(tgt.y() - pCell.y()) / vel;

		}

	}

	public void move(int tgx, int tgy) {
		// initiates move
		if (justPlaced)
			justPlaced = false;
		tgt = pCell.pGrid.squares[tgx][tgy];
		GC.gameState = 2;
		GC.selectedUnit = this;
		// Motion

	}

	public void moveAnim() {
		// makes the unit move
		if (Math.abs(tgt.x() - pCell.x()) - Math.abs(moveX) < 2
				&& Math.abs(tgt.y() - pCell.y()) - Math.abs(moveY) < 2) {
			GC.gameState = 1;
			int oldgx, oldgy;
			oldgx = pCell.gridX;
			oldgy = pCell.gridY;

			pCell = tgt;
			tgt.occupant = this;
			// old gx
			pCell.pGrid.squares[oldgx][oldgy].occupant = new Occupant();

			moveX = 0;
			moveY = 0;

			updateFOW();

			this.select();

		} else {

			int vel = 10;
			// Add move commands
			if (Math.abs(tgt.x() - pCell.x()) - Math.abs(moveX) >= 2)
				moveX += tgt.x() < pCell.x() ? -1 * Math.abs(tgt.x() - pCell.x()) / vel
						: Math.abs(tgt.x() - pCell.x()) / vel;

			if (Math.abs(tgt.y() - pCell.y()) - Math.abs(moveY) >= 2)
				moveY += tgt.y() < pCell.y() ? -1 * Math.abs(tgt.y() - pCell.y()) / vel
						: Math.abs(tgt.y() - pCell.y()) / vel;

		}

	}

	public void updateFOW() {
		// expand fog of war
		int sightRadius = 2;

		for (int dx = -1 * sightRadius; dx <= sightRadius; dx++) {
			for (int dy = -1 * sightRadius; dy <= sightRadius; dy++) {
				if (dx + pCell.gridX >= 0 && dy + pCell.gridY >= 0 && dx + pCell.gridX < GC.mapData[0].length()
						&& dy + pCell.gridY < GC.mapData.length) {
					GC.players[GC.activePlayer].FOW[dx + pCell.gridX][dy + pCell.gridY] = true;
				}
			}
		}
	}

	public void applyATK(Occupant attacker) {
		this.stats.vals[hp] -= attacker.stats.vals[attack];

		if (this.stats.vals[hp] <= 0) {
			GC.player = GC.sound.loadFile("data/Destroyed.mp3");
			GC.player.play();
			this.exists = false;
			this.pCell.occupant = new Occupant();
			this.pCell.occupant.id = -2;
			// ded
			GC.players[this.owner].activeUnits--;
		}

	}

}