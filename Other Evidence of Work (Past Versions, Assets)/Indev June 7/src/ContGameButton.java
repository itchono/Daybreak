
public class ContGameButton extends Button {

	ContGameButton(DayBreakMain parent, int x, int y, int w, int h, String msg) {
		super(parent, x, y, w, h, msg);
		// TODO Auto-generated constructor stub
	}
	
	public void execute() {
		p.gameSetup();
	}

}
