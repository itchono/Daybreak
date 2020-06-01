public class Shop extends Component {

	class ShopItem extends Component {

		int unitID;

		Shop p;

		ShopItem(Shop parent, int x, int y, int w, int h, int unitID) {
			super(parent.p, x, y, w, h);
			p = parent;
			this.unitID = unitID;

		}

		public void draw() {
			// only updates near button
			p.p.fill(GC.shopID == this.unitID ? p.p.color(65, 242, 183)
					: (this.containsMouse() ? p.p.color(150, 150, 200) : p.p.color(100, 100, 200)));

			p.p.rect(x, y, w, h);

			p.p.image(GC.unitImg[unitID], x + 5, y, w - 10, h - 10);

			p.p.fill(255);
			p.p.textSize(12);

		}

		public void click() {
			GC.shopID = this.containsMouse() ? unitID : GC.shopID;
			if (this.containsMouse())
				p.loadDesc(unitID);
		}

	}

	ShopItem[] shopSquares;
	Label[] shopTexts;

	int[] prices;

	Label description;

	Shop(DayBreakMain p, int x, int y, int w, int h) {
		super(p, x, y, w, h);

		shopSquares = new ShopItem[GC.numUnits()];
		shopTexts = new Label[GC.numUnits()];

		int initialGap = 20;

		for (int i = 0; i < GC.numUnits(); i++) {
			shopSquares[i] = new ShopItem(this, x + initialGap + (i % 3) * 50, y + initialGap + (i / 3) * 50, 50, 50,
					i);
			shopTexts[i] = new Label(p, x + initialGap + (i % 3) * 50 + 5, y + initialGap + (i / 3) * 50 + 45,
					FileIO.readStat("cost")[i], p.color(255), 14);
		}

		description = new Label(p, x + 10, y + h - 350, w - 20, 350, "Click on a shop item to view its description",
				p.color(42), 14);

	}

	public void draw() {
		if (GC.gameState == 0) {
			p.fill(240);
			p.rect(x, y, w, h);

			for (int i = 0; i < GC.numUnits(); i++) {
				shopSquares[i].draw();
				shopTexts[i].draw();
			}

			description.draw();
		}

	}

	public void click() {
		boolean contains = false;

		if (this.containsMouse()) {
			for (int i = 0; i < GC.numUnits(); i++) {
				shopSquares[i].click();
				if (shopSquares[i].containsMouse())
					contains = true;
				// making sure it contains
			}
			if (!contains) {
				GC.shopID = -1;
				loadDesc(-1);
			}

		}

	}

	public void loadDesc(int id) {
		if (id == -1) {
			description.text = "Click on a shop item for more information";
		} else {
			description.text = FileIO.readStat("name")[id] + "\nCost: " + FileIO.readStat("cost")[id] + "\n"
					+ FileIO.readStat("desc")[id] + "\n" + "Attack: " + FileIO.readStat("attack")[id] + "\n" + "HP: "
					+ FileIO.readStat("hp")[id] + "\n" + "Move Range: " + FileIO.readStat("moveRange")[id] + "\n"
					+ "Atk Range: " + FileIO.readStat("atkRange")[id] + "\n" + "Can Occupy: ";
			switch (Integer.parseInt(FileIO.readStat("terrainType")[id])) {
			case 0:
				description.text += "Land";
				break;
			case 1:
				description.text += "Water";
				break;
			case 2:
				description.text += "Land OR Water";
				break;
			}

		}

	}

}