import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseData {
	

	public String[] convertDEGtoDMS(String s) {
		String[] DMS = new String[3];
		String[] parts = s.split("\\.");
		String part1 = parts[0];
		double part1Dble = Double.parseDouble(part1);
		double part2Dble = Math.abs(part1Dble);
		String part2 = "0." + parts[1];
		String degrees = String.valueOf(part2Dble);
		double parted = Double.parseDouble(part2);
		double result = parted * 60;
		String s2 = String.valueOf(result);
		String[] parts2 = s2.split("\\.");
		String minutes = parts2[0];
		String part4 = "0." + parts2[1];
		double parted2 = Double.parseDouble(part4);
		double result2 = parted2 * 60;
		String seconds = String.valueOf(result2);
		DMS[0] = degrees;
		DMS[1] = minutes;
		DMS[2] = seconds;
		return DMS;
	}

	public ArrayList<String> parseGetRadius(String freq, String[] y, String[] x) {
		ArrayList<String> fccList = new ArrayList<String>();
		String html = "http://www.fccinfo.com/CMDProFacLookup.php?sCurrentService=" + freq
				+ "&sKilometers=50&sLatitude=" + x[0] + "-" + x[1] + "-" + x[2] + "&sLongitude=" + y[0] + "-" + y[1]
				+ "-" + y[2] + "&tabSearchType=Within+Search";

		try {
			Document doc = Jsoup.connect(html).get();
			Elements tableElements = doc.select("tbody");
			Elements tableRowElements = tableElements.select("tr");

			for (int i = 8; i < tableRowElements.size(); i++) {
				Element row = tableRowElements.get(i);
				Elements rowItems = row.select("td");

				for (int j = 1; j < rowItems.size(); j += 11) {
					fccList.add(rowItems.get(j).text());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fccList;
	}

}
