
public class Player {

	int actionsRemaining = 6;

	int id;

	int money;

	int activeUnits;

	static int[] offsetX = new int[] { 4, 4, 30, 37, 14, 40, 17, 26 };
	static int[] offsetY = new int[] { 4, 35, 37, 3, 18, 18, 38, 17 };

	public boolean active() {
		return this.activeUnits > 0;
	}

	boolean[][] FOW;

	public Player(int AR, int startMoney, int gx, int gy, int id) {
		FOW = new boolean[gx][gy];
		actionsRemaining = AR;
		money = startMoney;

		// starting locations

		for (int dx = 0 + offsetX[id]; dx < 10 + offsetX[id]; dx++) {
			for (int dy = 0 + offsetY[id]; dy < 10 + offsetY[id]; dy++) {
				FOW[dx][dy] = true;
			}
		}
	}

	public int getFOWNum() {
		int result = 0;

		for (int x = 0; x < GC.mapData[0].length(); x++) {
			for (int y = 0; y < GC.mapData.length; y++) {
				if (FOW[x][y]) {
					result++;
				}
			}
		}

		return result;
	}

	// not defining constructor bc no need to (all initialise to 0)

}
