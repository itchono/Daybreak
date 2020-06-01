import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PImage;

public class GC {

	// GAME CONTROLLER
	// stores global variables

	static int[] ownerColours, terrainColours, terrainBorders;
	static int[][] unitStats;
	final static int HP = 0;
	final static int ATK = 1;

	// for cam movement/scale/players
	static int activePlayer, cameraX, cameraY, dcX, dcY, scale;
	static double dScale = 1;

	static int shopID = -1;
	// Selected unitID

	static Occupant selectedUnit;
	static String[] mapData;

	static TargetingPane tp;

	static Player players[];

	public static boolean terrainisValid(Cell tgt, int id) {
		return (Integer.parseInt(FileIO.readStat("terrainType")[id]) == tgt.terrainType
				|| Integer.parseInt(FileIO.readStat("terrainType")[id]) == 2)
				&& players[activePlayer].FOW[tgt.gridX][tgt.gridY] && !tgt.occupant.exists && tgt.occupant.id != -2;
		// valid terrain type, within fog of war
	}

	public static boolean hasEnoughMoney(int id) {
		return players[activePlayer].money >= Integer.parseInt(FileIO.readStat("cost")[id]);
	}

	public static int numUnits() {
		// Number of units loaded in file; not to be confused with number of standing
		// units
		return FileIO.readStat("name").length;
	}

	public static void loadTP(TargetingPane p) {
		tp = p;
	}
	

	static int turns;

	static int bulletX, bulletY;
	static Occupant bulletP;
	// Used for bullets

	static int gameState;
	// game state
	// 0 unit placing
	// 1 unit moving
	// 2 moving animation
	// 3 atk animation
	static PImage[] unitImg;
	// Images, all loaded at startup
	
	static AudioPlayer player;
	static Minim sound;

	static int winner;

	// kind of messy but its the best thing I can think of
}
