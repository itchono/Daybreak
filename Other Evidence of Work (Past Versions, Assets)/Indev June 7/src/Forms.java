import java.util.ArrayList;
import java.util.List;

import gifAnimation.*;

class Form {
	// Base object for forms which everything should be layered upon
	DayBreakMain parent;

	List<Component> components = new ArrayList<Component>();



	Form(DayBreakMain p) {
		parent = p;
	}

	public void clickForm() {
		for (Component c : components)
			c.click();
	}

	public void drawComponents() {
		for (Component c : components)
			c.draw();
	}

}

class MainMenu extends Form {

	Gif loopingGif;

	MainMenu(DayBreakMain p) {
		super(p);

		components.add(new MenuButton(parent, "Start", 1000, 200, 100, 50));
		components.add(new Label(parent, 1000, 50, "DAYBREAK", parent.color(255), 40));
		components.add(new Label(parent, 1050, 90, "A square game", parent.color(255), 20));
		loopingGif = p.loopingGif;
		loopingGif.play();

	}

	public void drawComponents() {
		parent.image(loopingGif, 0, 0);
		super.drawComponents();

	}

}

class NumPlayers extends Form {

	NumPlayers(DayBreakMain p) {
		super(p);
		components.add(new ContGameButton(p, 600, 350, 200, 100, "Proceed to game"));
	}
	
}

class GameForm extends Form {

	String msg;

	GameForm(DayBreakMain p, int mapw, int maph) {
		super(p);

		components.add(new MovePlaceSwitch(parent, "Placing Units", 600, 655, 250, 50));
		components.add(new Grid(parent, 20, 50, 1000, 600, DayBreakMain.mapData[0].length(), DayBreakMain.mapData.length, 40, 0));
		components.add(new Label(parent, 40, 80, "Welcome to Prometheus Island", parent.color(40), 24));
		components.add(new Shop(parent, 1080, 50, 200, 600));
		components.add(new Label(parent, 250, 720, "Toggle mode using upper right hand button; movement and attacking WIP", parent.color(0), 24));
		components.add(new SwitchButton(parent, 200, 655, 250, 50,DayBreakMain.ownerColours[p.activePlayer], (Grid)components.get(1)));
		components.add(new StatIndicator(parent, 900, 20, 150, 90, parent.color(0), 10));
		components.add(new TurnIndicator(parent, 20, 40, parent.color(0), 32));
		
	}

	public void drawComponents() {
		
		parent.fill(250);
		parent.rect(0, 0, parent.width, parent.height);
		
		for (Component c : components) {
			if (c.toString().contains("GameButton")) {
				MovePlaceSwitch m = (MovePlaceSwitch) c;
				m.text = (parent.gameState == 0 ? "Placing Units" : "Moving Units");
			}
			c.draw();
			// set defaults TODO
			parent.stroke(0, 255);

		}

	}

}

class EndForm extends Form {

	EndForm(DayBreakMain p) {
		super(p);
		components.add(new Label(parent, 250, 720, "Game over", parent.color(0), 24));
		// TODO Auto-generated constructor stub
	}
	
}
