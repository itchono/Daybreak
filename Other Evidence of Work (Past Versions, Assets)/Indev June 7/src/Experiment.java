
import processing.core.PApplet;

public class Experiment extends PApplet {

	public static void main(String[] args) {
		PApplet.main("Experiment");
	}

	public void settings() {
		setSize(1280, 720);
	}
	
	public void setup() {
		fill(100, 255, 100);
		this.strokeWeight(6);
		stroke(color(100, 255, 100) - color(100, 255));
		rect(200, 200, 200, 200);
	}
}
