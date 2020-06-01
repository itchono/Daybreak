class MovePlaceSwitch extends Button {

	MovePlaceSwitch(DayBreakMain p, String s, int x, int y, int w, int h) {
		super(p, x, y, w, h, s);
	}

	public void execute() {
		p.gameState = (p.gameState == 0 ? 1 : 0);
		text = (p.gameState == 0 ? "Placement Mode" : "Action Mode");
	}

}