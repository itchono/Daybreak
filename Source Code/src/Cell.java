public class Cell {
	// Square objects drawn on screen, can contain occupants, and are the basis of
	// interaction
	int gridX, gridY;

	Grid pGrid;

	int bgc;
	// determines bgc of square

	Occupant occupant;

	int terrainType;

	Cell(Grid gridP, int gx, int gy) {

		gridX = gx;
		gridY = gy;
		this.pGrid = gridP;
		occupant = new Occupant();
		// graveyarded occupant for the moment
		terrainType = Integer.parseInt(String.valueOf(GC.mapData[gy].charAt(gx)));

		bgc = GC.terrainColours[terrainType];

	}

	public boolean cameraContainsMouse() {
		return (pGrid.p.mouseX - GC.cameraX > x() && pGrid.p.mouseX - GC.cameraX < x() + GC.scale
				&& pGrid.p.mouseY - GC.cameraY > y() && pGrid.p.mouseY - GC.cameraY < y() + GC.scale);
	}

	public void drawBG() {
		// draGC.scale in terrain square

		pGrid.p.stroke(GC.terrainBorders[terrainType]);
		pGrid.p.strokeWeight(1);
		pGrid.p.fill(this.cameraContainsMouse() || this.occupant.isSelected() ? bgc + pGrid.p.color(40) : bgc);
		// rnd colours make it interesting

		pGrid.p.rect(x() + GC.cameraX, y() + GC.cameraY, GC.scale, GC.scale);
		if (!GC.players[GC.activePlayer].FOW[gridX][gridY]) {
			pGrid.p.fill(pGrid.p.color(30, 100));
			pGrid.p.stroke(40, 100);
			pGrid.p.rect(x() + GC.cameraX, y() + GC.cameraY, GC.scale, GC.scale);
		}

	}

	public void drawUnit() {
		// unit and triangle indicator
		if (GC.gameState == 2 && occupant.isSelected()) {
			occupant.moveAnim();
		} else if (GC.gameState == 3 && occupant.isSelected()) {
			occupant.atkAnim();
		}

		if (occupant.id != -1) {

			
			if (occupant.id == -2) {
				pGrid.p.image(pGrid.p.loadImage("Unitimages/Rubble.png"), x() + occupant.moveX + GC.cameraX, y() + occupant.moveY + GC.cameraY,
						GC.scale, GC.scale);
			}
			else {
				pGrid.p.image(GC.unitImg[occupant.id], x() + occupant.moveX + GC.cameraX, y() + occupant.moveY + GC.cameraY,
						GC.scale, GC.scale);
				pGrid.p.fill((this.cameraContainsMouse() || this.occupant.isSelected()
						? GC.ownerColours[occupant.owner] + pGrid.p.color(30)
						: GC.ownerColours[occupant.owner]));
				pGrid.p.triangle(x() + GC.cameraX, y() + GC.cameraY, x() + GC.cameraX + (GC.scale / 2),
						y() + GC.cameraY + GC.scale / 5, x() + GC.scale + GC.cameraX, y() + GC.cameraY);

				pGrid.p.fill(255, 100, 100);
				pGrid.p.rect(x() + GC.cameraX, y() + GC.cameraY + GC.scale * 7 / 8,
						GC.scale * occupant.stats.vals[Occupant.hp] / occupant.stats.maxHP, GC.scale / 8);
			}
			

			
		}

	}

	public void drawUI() {
		if (occupant.isSelected() && occupant.id != -1) {
			occupant.drawComponents();
		}

	}

	public int x() {
		return pGrid.x + GC.scale * gridX;
	}

	public int y() {
		return pGrid.y + GC.scale * gridY;
	}

}