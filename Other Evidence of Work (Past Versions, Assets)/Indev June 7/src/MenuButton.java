class MenuButton extends Button {

	MenuButton(DayBreakMain p, String s, int x, int y, int w, int h) {
		super(p, x, y, w, h, s);
	}

	public void execute() {
		p.formTransition(2);;
	}

}