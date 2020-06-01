
public class TurnIndicator extends Label{

	TurnIndicator(DayBreakMain parent, int x, int y,  int color, int size) {
		super(parent, x, y, "", color, size);
	}
	
	public void draw(){
		text = "Actions remaining this turn: " + p.players[p.activePlayer].actionsRemaining;
		super.draw();
	}

}
