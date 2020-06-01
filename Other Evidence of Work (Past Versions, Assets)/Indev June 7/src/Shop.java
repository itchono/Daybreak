class Shop extends Component {

	ShopItem[] shopSquares;
	Label[] shopTexts;
	
	int[] prices;

	Shop(DayBreakMain p, int x, int y, int w, int h) {
		super(p, x, y, w, h);

		shopSquares = new ShopItem[DayBreakMain.numUnits()];
		shopTexts = new Label[DayBreakMain.numUnits()];

		int initialGap = 20;

		for (int i = 0; i < DayBreakMain.numUnits(); i++) {
			shopSquares[i] = new ShopItem(p, x + initialGap + (i % 3) * 40, y + initialGap + (i / 3) * 40, 40, 40, i);
			shopTexts[i] = new Label(p, x + initialGap + (i % 3) * 40+5, y + initialGap + (i / 3) * 40 + 20, "U " + i,
					p.color(255), 12);
		}

	}

	public void draw() {
		if (p.gameState == 0) {
			p.fill(240);
			p.rect(x, y, w, h);

			for (int i = 0; i < DayBreakMain.numUnits(); i++) {
				shopSquares[i].draw();
				shopTexts[i].draw();
			}
		}
		
	}

	public void click() {
		boolean contains = false;
		
		if (this.containsMouse()) {
			for (int i = 0; i < DayBreakMain.numUnits(); i++) {
				shopSquares[i].click();
				if (shopSquares[i].containsMouse())
						contains = true;
				// making sure it contains
			}
			if (!contains)
				p.shopID = -1;
		}
		
	}

}