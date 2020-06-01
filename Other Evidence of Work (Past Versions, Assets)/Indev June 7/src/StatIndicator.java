
public class StatIndicator extends Label{

	StatIndicator(DayBreakMain parent, int x, int y, int w, int h, int color, int size) {
		super(parent, x, y, "", color, size);
		// TODO Auto-generated constructor stub
	}
	
	public void draw() {
		p.fill(180);
		p.rect(x,y - 20,150,60);
		p.fill(0);
		text = "CameraX: " + p.cameraX + "  CameraY: " + p.cameraY + 
				"\nMouseX: " + p.mouseX + "  MouseY: " + p.mouseY + "\nFPS: " + p.frameRate + "  Players: " + p.players.length;
		super.draw();
		
	}

}
