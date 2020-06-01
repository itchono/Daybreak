// Superclass

public class Component {
	int x, y, w, h;

	boolean visible;

	DayBreakMain p;
	// parent

	Component(DayBreakMain parent, int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.p = parent;
		visible = true;

	}

	public boolean containsMouse() {
		return (p.mouseX > x && p.mouseX < x + w && p.mouseY > y && p.mouseY < y + h);
	}

	public boolean cameraContainsMouse() {
		return (p.mouseX - GC.cameraX > x && p.mouseX - GC.cameraX < x + w
				&& p.mouseY - GC.cameraY > y && p.mouseY - GC.cameraY < y + h);
	}

	public boolean mouseProximity(int range) {
		return (p.mouseX > x - range && p.mouseX < x + range + w && p.mouseY > y - range
				&& p.mouseY < y + range + h);
	}

//	public boolean cameraMouseProximity(int range) {
//		return (parent.mouseX + parent.cameraX > x - range && parent.mouseX + parent.cameraX < x + range + w
//				&& parent.mouseY + parent.cameraY > y - range && parent.mouseY + parent.cameraY < y + range + h);
//	}

	public void draw() {

		p.rect(x, y, w, h);
	}

	public void click() {

	}
	
}