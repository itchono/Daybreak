import java.io.*;
import java.util.ArrayList;

public class FileIO {

	// modified code from internet
	@SuppressWarnings("finally")
	public static String[] readStat(String query) {
		BufferedReader reader = null;
		ArrayList<String> result = new ArrayList<String>();
		String line = null;
		
		try {
			reader = new BufferedReader(new FileReader(new File("data/Information.dat")));

			while ((line = reader.readLine()) != null) {
				if (line.contains(query)) {
					result.add(new String(line).substring(line.indexOf("=") + 2, line.length()));
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

	public static String[] trimArray(ArrayList<String> input) {
		return input.toArray(new String[input.size()]);
	}

}
