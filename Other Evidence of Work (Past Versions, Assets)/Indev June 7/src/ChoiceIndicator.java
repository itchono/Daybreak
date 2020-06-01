
public class ChoiceIndicator {

	int color;

	int role;

	Occupant unit;
	Cell tgt;

	final static int ATTACK = 1;
	final static int MOVE = 2;

	boolean exists;

	ChoiceIndicator(Occupant unit, Cell targetSquare, int role) {

		// TODO Auto-generated constructor stub
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
			tgt.p.stroke(0, 0);
			tgt.p.fill(color);
			tgt.p.ellipse(tgt.x + tgt.p.cameraX, tgt.y + tgt.p.cameraY, tgt.w, tgt.h);
		}

	}
// test code - WIP
//	public static boolean isDirectMove(Cell tgt, Occupant unit) {
//		if (tgt.occupant.isSelected())
//			return true;
//		else {
//			boolean present = false;
//			for (int i = -1; i <= 1; i++) {
//				for (int k = Math.abs(i) - 1; k <= (i == 0 ? 1 : 0); k += 2) {
//
//					// Check
//					if (tgt.gridX + i >= (unit.pCell.gridX - unit.maxRange()) && tgt.gridX + i <= (unit.pCell.gridX - unit.maxRange()) && tgt.gridY + k >= (unit.pCell.gridY - unit.maxRange())
//							&& tgt.gridY + k <=  (unit.pCell.gridY + unit.maxRange())
//							&& isDirectMove(tgt.pGrid.squares[tgt.gridX + i][tgt.gridY + k], unit))
//						// TODO set limits
//						present = true;
//
//					// recursion
//
//				}
//			}
//			return present;
//		}
//
//	}

}
