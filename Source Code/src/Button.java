
public class Button extends Component {

	String text;

	Button(DayBreakMain parent, int x, int y, int w, int h, String msg) {
		super(parent, x, y, w, h);
		this.text = msg;
	}

	public void click() {
		if (this.containsMouse())
			execute();
	}

	public void draw() {
		// try to only change on update
		p.textAlign(DayBreakMain.CENTER, DayBreakMain.CENTER);
		p.textSize(32);
		p.stroke(0, 0);
		p.fill(this.containsMouse() ? p.color(220) : p.color(200));
		p.rect(x, y, w, h);
		p.fill(p.color(42));
		p.text(text, x + w / 2, y + h / 2);

	}

	public void execute() {

	}
}