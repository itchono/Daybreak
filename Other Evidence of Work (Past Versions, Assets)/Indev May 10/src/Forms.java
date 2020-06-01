import java.util.ArrayList;
import java.util.List;

class Form {
	// Base object for forms which everything should be layered upon
	SquareSpace parent;

	List<Component> components = new ArrayList<Component>();

	int dispC, x, y, w, h;

	Form(SquareSpace p, int c, int x, int y, int w, int h) {
		parent = p;
		dispC = c;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}


	public void clickForm() {
		for (Component c : components)
			c.click();
	}

	public void drawComponents() {
		for (Component c : components)
			c.draw();
	}

	public void fillDraw() {
		for (Component c : components)
			c.fillRect();
	}
}

class MainMenu extends Form {

	MainMenu(SquareSpace p, int c) {
		super(p, c, 10, 10, 1200, 700);

		components.add(new MenuButton(parent, "Start", 500, 200, 100, 50));
	}

}

class GameForm extends Form {

	String msg;

	GameForm(SquareSpace p, int c) {
		super(p, c, 10, 10, 1200, 700);
		parent = p;
		dispC = c;

		components.add(new GameButton(parent, "Turn", 600, 50, 250, 50));
		components.add(new Grid(parent, 50, 100, 800, 400, 100, 50, 40, 0));
		components.add(new Shop(parent, 920, 50, 200, 600));
		components.add(new TextField(parent, 150, 60, 0, 0, "Click to do stuff", parent.color(0)));



	}
	
	public void drawComponents() {
		for (Component c : components) {
			if (c.toString().contains("GameButton")) {
				GameButton m = (GameButton) c;
				m.text = (parent.gameState == 0 ? "Placing Units" : "Moving Units");
			}
			c.draw();
			
		}
			
	}

	public void fillDraw() {
		// Onetime setup for BG
		parent.fill(dispC);
		parent.rect(x, y, w, h);
		for (Component c : components)
			c.fillRect();
		System.out.println("DONE");
		// Draw itself
	}

}
