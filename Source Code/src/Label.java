public class Label extends Component {

	String text;
	int color;
	int size;
	int boxH, boxW;

	Label(DayBreakMain parent, int x, int y, String msg, int color, int size) {
		super(parent, x, y, 0, 0);
		text = msg;
		this.color = color;
		this.size = size;
	}

	Label(DayBreakMain parent, int x, int y, int boxW, int boxH, String msg, int color, int size) {
		super(parent, x, y, boxW, boxH);
		text = msg;
		this.color = color;
		this.size = size;
		this.boxH = boxH;
		this.boxW = boxW;
	}

	public void draw() {
		p.textAlign(DayBreakMain.LEFT);
		p.fill(color);
		p.textSize(size);

		if (boxW != 0 && boxH != 0)
			p.text(text, x, y, boxW, boxH);
		else
			p.text(text, x, y);
	}

}