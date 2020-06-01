import java.io.*;

public class FileIO {

	@SuppressWarnings("finally")
	public static String[] readStat(String query) {
		BufferedReader reader = null;

		String[] result = new String[30];
		int iterator = 0;

		String line = null;
		try {
			File file = new File("data/Information.dat");
			reader = new BufferedReader(new FileReader(file));

			while ((line = reader.readLine()) != null) {
				if (line.contains(query)) {
					result[iterator] = new String(line).substring(line.indexOf("=") + 2, line.length());
					iterator++;
				}
			}
			reader.close();
			return trimArray(result);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return trimArray(result);
		}

	}

	public static String[] trimArray(String[] input) {
		int iterator = 0;

		while (input[iterator] != (null)) {
			iterator++;
		}

		String[] result = new String[iterator];

		for (int i = 0; i < iterator; i++) {
			result[i] = new String(input[i]);
		}

		return result;
	}

}
