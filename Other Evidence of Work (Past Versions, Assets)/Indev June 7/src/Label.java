import processing.core.PConstants;

class Label extends Component {

	String text;
	int color;
	int size;

	Label(DayBreakMain parent, int x, int y, String msg, int color, int size) {
		super(parent, x, y, 0, 0);
		text = msg;
		this.color = color;
		this.size = size;
	}

	public void draw() {
		p.textAlign(PConstants.LEFT, PConstants.LEFT);
		p.fill(p.color(color));
		p.textSize(size);
		p.text(text, x, y );
	}
}