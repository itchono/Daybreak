class Cell extends Component {
	// Square objects drawn on screen, can contain occupants, and are the basis of
	// interaction
	int gridX, gridY;

	Grid pGrid;

	int bgc;

	Occupant occupant;

	int terrainType;

	// determines bgc of square

	// x pos, y pos, side length, state variable

	Cell(Grid gridP, int px, int py, int gx, int gy, int w, int h) {
		super(gridP.p, px, py, w, h);
		gridX = gx;
		gridY = gy;
		this.pGrid = gridP;
		occupant = new Occupant(this, -1, gx, gy, -1);
		// NEVER EVER EVER EVER EVER DRAW WITH AN IMAGE IN SETUP
		terrainType = Integer.parseInt(String.valueOf(DayBreakMain.mapData[gy].charAt(gx)));

		bgc = DayBreakMain.terrainColours[terrainType];

	}

	public void drawBG() {
		// draw in terrain square

		p.stroke(DayBreakMain.terrainBorders[terrainType]);
		p.strokeWeight(1);
		p.fill((this.cameraContainsMouse() || this.occupant.isSelected() ? bgc + p.color(40) : bgc));
		// rnd colours make it interesting

		p.rect(x + p.cameraX, y + p.cameraY, w, h);

	}

	public void drawUnit() {
		// unit and triangle indicator
		if (p.gameState == 2 && occupant.isSelected()) {
			occupant.moveAnim();
		}else if (p.gameState == 3 && occupant.isSelected()) {
			occupant.atkAnim();
		}

		if (occupant.id != -1) {

			p.image(p.unitImg[occupant.id], x + occupant.moveX + p.cameraX, y +occupant.moveY + p.cameraY,
					w , h);

			p.fill((this.cameraContainsMouse() || this.occupant.isSelected()
					? DayBreakMain.ownerColours[occupant.owner] + p.color(50)
					: DayBreakMain.ownerColours[occupant.owner]));
			p.triangle(x + p.cameraX, y + p.cameraY, x + p.cameraX + (w / 2), y + p.cameraY + h / 5, x + w + p.cameraX,
					y + p.cameraY);
		}
	}

	public void drawUI() {
		if (occupant.isSelected() && occupant.id != -1) {
			occupant.drawComponents();
		}

	}

}