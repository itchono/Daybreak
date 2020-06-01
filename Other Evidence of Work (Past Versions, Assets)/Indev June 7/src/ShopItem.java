class ShopItem extends Component {

	int unitID;

	int state;

	ShopItem(DayBreakMain parent, int x, int y, int w, int h, int unitID) {
		super(parent, x, y, w, h);
		this.unitID = unitID;

	}

	public void draw() {
		// only updates near button
		p.fill(this.state == 1 ? p.color(65, 242, 183) : (this.containsMouse() ? p.color(150, 150, 200) : p.color(100, 100, 200)));

		p.rect(x, y, w, h);

		p.image(p.unitImg[unitID], x + 5, y + 5, w - 10, h - 10);

		p.fill(255);
		p.textSize(12);

	}

	public void click() {
		p.shopID = this.containsMouse() ? unitID : p.shopID;
		state = this.containsMouse() ? 1 : 0;
	}

}