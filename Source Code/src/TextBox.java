
public class TextBox extends Component {

	// used for numeric or text inputs
	// homebrewed by me so missing a lot of features like text selection
	// not fully featured because I only need it for one purpose

	private int rc, tc;
	private int textSize;

	final static int NUMERIC = 1;
	final static int ALLTEXT = 0;

	private String text;

	boolean active;

	Label prompt;

	private int mode;

	TextBox(DayBreakMain parent, int x, int y, int w, int h, int rc, int tc, int size, int mode, String msg) {
		super(parent, x, y, w, h);
		this.rc = rc;
		this.tc = tc;
		textSize = size;
		active = true;
		text = "";
		this.mode = mode;

		
		prompt = new Label(parent, x - 200, y, 200, h, msg, p.color(255), 24);
	}

	TextBox() {
		super(new DayBreakMain(), 0, 0, 0, 0);
		// graveyard constructor much like how occupant works
		active = false;
		text = "";
	}

	public void draw() {

		p.txtIn = this;
		p.fill(40);
		p.rect(prompt.x, prompt.y, prompt.w, prompt.h);
		p.fill(p.txtIn.equals(this) ? rc + p.color(10) : rc);
		prompt.draw();
		p.rect(x, y, w, h);
		p.fill(tc);
		p.textSize(textSize);
		p.textAlign(DayBreakMain.CENTER, DayBreakMain.CENTER);
		p.text(text, x, y, w, h);
	}

	public void click() {
//		if (this.containsMouse()) {
//			p.txtIn = this;
//		} unnecessary for this version

		// Make the txtb listen to the PApplet's inputs
	}

	public void receiveIn(char in) {
		if (String.valueOf(in).matches("[0-9]") || mode == ALLTEXT) {
			text += in;
		}

	}

	public int getInt() {
		if (text.isEmpty())
			return 0;
		return Integer.parseInt(text);
	}

	public void bksp() {
		if (text.length() > 0) {
			text = new String(text.substring(0, text.length() - 1));
		}
	}

}
