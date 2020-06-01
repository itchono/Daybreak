
public class TargetingPane extends Component {

	// much like shop but drawn when gameState >= 1

	Label selectedInfo;
	Label tgtInfo;

	TargetingPane(DayBreakMain parent, int x, int y, int w, int h) {
		super(parent, x, y, w, h);

		selectedInfo = new Label(parent, x + 10, y + 20, w - 20, 200, "Selected Unit: N/A", p.color(255), 18);
		tgtInfo = new Label(parent, x + 10, y + 230, w - 20, 400, "Hovered Square: N/A", p.color(255), 16);
	}

	public void loadSelect(Occupant selected) {
		selectedInfo.text = "Selected Unit: " + selected.stats.name + "\n";
		selectedInfo.text += "Move Range: " + selected.stats.vals[Occupant.moveRange] + "\nAtk Range: "
				+ selected.stats.vals[Occupant.attackRange] + "\nDamage: " + selected.stats.vals[Occupant.attack]
				+ "\nHP: " + selected.stats.vals[Occupant.hp] + "/" + selected.stats.maxHP;
	}

	public void clearSelect() {
		selectedInfo.text = "Selected Unit: N/A";
	}

	public void loadHover(Cell hover) {
		tgtInfo.text = "Hovered Square:\n";

		if (GC.players[GC.activePlayer].FOW[hover.gridX][hover.gridY]) {
			if (hover.occupant.id == -2)
				tgtInfo.text += "DESTROYED UNIT";
			else if (hover.occupant.id > -1)
				tgtInfo.text += "ACTIVE UNIT - " + hover.occupant.stats.name;
			else
				tgtInfo.text += "EMPTY SQUARE";
		} else
			tgtInfo.text += "UNEXPLORED SQUARE";

		tgtInfo.text += "\n(" + hover.gridX + "," + hover.gridY + ")";
		tgtInfo.text += "\nTerrain Type:\n" + (hover.terrainType == 0 ? "Land" : "Water");

		if (hover.occupant.exists && GC.players[GC.activePlayer].FOW[hover.gridX][hover.gridY]) {
			// load occupant info
			tgtInfo.text += "\nMove Range: " + hover.occupant.stats.vals[Occupant.moveRange] + "\nAtk Range: "
					+ hover.occupant.stats.vals[Occupant.attackRange] + "\nDamage: "
					+ hover.occupant.stats.vals[Occupant.attack] + "\nHP: " + hover.occupant.stats.vals[Occupant.hp]
					+ "/" + hover.occupant.stats.maxHP + "\nOwned by: Player " + (hover.occupant.owner + 1);
		}

	}
	
	public void clearHover() {
		tgtInfo.text = "Hovered Square: N/A\n";
	}

	public void draw() {
		if (GC.gameState >= 1) {
			p.fill(42);
			p.rect(x, y, w, h);

			selectedInfo.draw();
			tgtInfo.draw();
		}
	}

}
