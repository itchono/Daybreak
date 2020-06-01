import processing.core.PConstants;

class Button extends Component {

	String text;

	Button(DayBreakMain parent, int x, int y, int w, int h, String msg) {
		super(parent, x, y, w, h);
		this.text = msg;
		// TODO Auto-generated constructor stub
	}

	public void click() {
		if (this.containsMouse())
			execute();
	}

	public void draw() {
		// try to only change on update

		p.textSize(32);

		p.fill(this.containsMouse() ? p.color(220) : p.color(200));
		p.rect(x, y, w, h);

		p.textAlign(PConstants.CENTER, PConstants.CENTER);
		p.fill(p.color(0));
		p.text(text, x + w / 2, y + h / 2);

	}

	public void execute() {

	}
}