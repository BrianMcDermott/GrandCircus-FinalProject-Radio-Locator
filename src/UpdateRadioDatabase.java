import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UpdateRadioDatabase {
	
	public void ParseRadioInfo() {
			
		RadioDAO updateDatabase = new RadioDAO();
		
		String[] wiki = { "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
				"Delaware", "Florida", "Georgia", "Idaho", "Illinois", "Indiana", "Kansas",
				"Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi",
				"Missouri", "Montana", "Nebraska", "Nevada", "New_Hampshire", "New_Jersey", "New_Mexico", "New_York",
				"North_Carolina", "North_Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode_Island",
				"South_Carolina", "South_Dakota", "Tennessee", "Utah", "Vermont", "Virginia", "Washington",
				"West Virginia", "Wisconsin", "Wyoming", "Ontario", "British_Columbia", "Alberta", "Manitoba",
				"Nova_Scotia", "Quebec"};
		
		// Iowa, Texas, and Hawaii do not parse properly  need to parse these specially
		
		 for (int z = 0; z < wiki.length; z++) {
		String html = "https://en.wikipedia.org/wiki/List_of_radio_stations_in_" + wiki[z];

		try {
			Document doc = Jsoup.connect(html).get();
			Elements tableElements = doc.select("table.wikitable");

			Elements tableRowElements = tableElements.select(":not(thead) tr");

			ArrayList<String> wikiArrayList = new ArrayList<String>();
			for (int i = 1; i < tableRowElements.size(); i++) {
				Element row = tableRowElements.get(i);
				Elements rowItems = row.select("td");

				for (int j = 0; j < rowItems.size(); j++) {

					wikiArrayList.add(rowItems.get(j).text());
				}

			}

			for (int i = 0; i < wikiArrayList.size(); i += 1) {
				if (i % 5 == 0) {
					updateDatabase.callSign.add(wikiArrayList.get(i));
				}
				if (i % 5 == 1) {
					updateDatabase.frequency.add(wikiArrayList.get(i));
				}
				if (i % 5 == 2) {
					updateDatabase.city.add(wikiArrayList.get(i));
				}
				if (i % 5 == 4) {
					updateDatabase.genre.add(wikiArrayList.get(i));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
}
