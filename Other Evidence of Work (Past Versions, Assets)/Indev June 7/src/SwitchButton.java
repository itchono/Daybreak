import processing.core.PConstants;

public class SwitchButton extends Button{
	
	int bgc;
	Grid g;
	
	SwitchButton(DayBreakMain parent, int x, int y, int w, int h, int colour, Grid g) {
		super(parent, x, y, w, h, "Switch Player (" + (parent.activePlayer + 1) + ")");
		bgc = colour;
		this.g = g;
		
	}
	
	public void execute(){
		if (p.activePlayer < p.players.length - 1) {
			p.activePlayer ++;
		}
		else {
			p.activePlayer = 0;
			// replenish
			for (int i = 0; i < p.players.length; i++) {
				p.players[i].actionsRemaining = 4;
			}
		}
		bgc = DayBreakMain.ownerColours[p.activePlayer];
		
		text = "Switch Player (" + (p.activePlayer + 1) + ")";
		
		
		// GAME CHANGE LOGIC
		g.deselectAll();
		
		p.superDraw();
	}
	
	public void draw() {
		// try to only change on update

		p.textSize(32);

		p.fill(this.containsMouse() ? bgc + p.color(20) : bgc);
		p.rect(x, y, w, h);

		p.textAlign(PConstants.CENTER, PConstants.CENTER);
		p.fill(p.color(0));
		p.text(text, x + w / 2, y + h / 2);

	}

}
