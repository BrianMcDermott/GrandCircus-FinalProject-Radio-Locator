import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RadioDAO {
	static ArrayList<RadioStationRadius> radioList = new ArrayList<RadioStationRadius>();
	static ArrayList<String> callSign = new ArrayList<String>();
	static ArrayList<String> frequency = new ArrayList<String>();
	static ArrayList<String> city = new ArrayList<String>();
	static ArrayList<String> genre = new ArrayList<String>();
	static ArrayList<String> wikiArrayList = new ArrayList<String>();
	public String dLatitude = "";
	public String mLatitude = "";
	public String sLatitude = "";
	public String dLongitude = "";
	public String mLongitude = "";
	public String sLongitude = "";
	
	
    public static String[] convertDEGtoDMS(String s) {
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
        for (int i = 0; i < DMS.length; i++) {
			System.out.println(DMS[i]);
		}
        return DMS;
    }
	public static ArrayList<RadioStationRadius> getRadius() {
		URL url;
		String radiusSite = "https://transition.fcc.gov/fcc-bin/fmq?call=&arn=&state=&city=&freq=0.0&fre2=107.9&serv=&vac=&facid=&asrn=&class=&list=4&dist=50&dlat2=41&mlat2=52&slat2=54.60&NS=N&dlon2=87&mlon2=37&slon2=23.44&EW=W&size=9&ThisTab=Results+to+This+Page%2FTab";
		try {
			// Get URL content
			url = new URL(radiusSite);

			// Opens connection to the website
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			BufferedReader htmlInfo = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			toArrayList(htmlInfo);

			/*
			 * ArrayList<RadioStationRadius> radioList = toArrayList(htmlInfo);
			 * System.out.println(radioList);
			 */

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return radioList;
	}

	public static ArrayList<RadioStationRadius> toArrayList(BufferedReader htmlInfo) throws IOException {
		String inputLine;
		while ((inputLine = htmlInfo.readLine()) != null) {

			RadioStationRadius radioStation = new RadioStationRadius();

			inputLine = inputLine.replace("|", "");

			String[] split = inputLine.split(" ");
			for (int m = 0; m < split.length; m++) {
				if (m == 0) {
					radioStation.setCallSign((split[m]));
					;
					radioList.add(radioStation);
				}
			}

		}
		return radioList;
	}
	
	private static Connection connect = null;

	private static PreparedStatement preparedStatement = null;

	public static void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");

		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/radioapp", "root", "sesame");
	}

	public static void addCity() throws SQLException, ClassNotFoundException {
		getConnection();

		preparedStatement = connect.prepareStatement("INSERT IGNORE INTO radioapp.chicago (callsign) VALUEs (?)");

		for (int i = 0; i < radioList.size(); i++) {

			preparedStatement.setString(1, radioList.get(i).getCallSign());
			preparedStatement.executeUpdate();

		}

		connect.close();
	}

	public static void addData() throws ClassNotFoundException, SQLException {
		getConnection();
		preparedStatement = connect.prepareStatement(
				"INSERT IGNORE INTO radioapp.information (callsign, frequency, city, genre) VALUES (?, ?, ?, ?)");
		for (int j = 0; j < callSign.size(); j++) {
			preparedStatement.setString(1, callSign.get(j));
			preparedStatement.setString(2, frequency.get(j));
			preparedStatement.setString(3, city.get(j));
			preparedStatement.setString(4, genre.get(j));
			preparedStatement.executeUpdate();

		}
		connect.close();
	}

	public static ArrayList<RadioStationRadius> getRadioStations() throws ClassNotFoundException, SQLException {
		getConnection();
		ArrayList<RadioStationRadius> radioList = new ArrayList<RadioStationRadius>();
		System.out.println("test");

		preparedStatement = connect.prepareStatement(
				"SELECT chicago.callsign, information.frequency,information.city,information.genre FROM chicago INNER JOIN information ON chicago.callsign = information.callsign");
		ResultSet results = preparedStatement.executeQuery();

		while (results.next()) {
			String callSign = results.getString("callsign");
			String frequency = results.getString("frequency");
			String city = results.getString("city");
			String genre = results.getString("genre");
			RadioStationRadius cityResults = new RadioStationRadius(callSign, frequency, city, genre);
			radioList.add(cityResults);
		}
		connect.close();
		return radioList;
	}

	public static void ParseWiki() {

		String html = "https://en.wikipedia.org/wiki/List_of_radio_stations_in_Florida";
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
					callSign.add(wikiArrayList.get(i));
				}
				if (i % 5 == 1) {
					frequency.add(wikiArrayList.get(i));
				}
				if (i % 5 == 2) {
					city.add(wikiArrayList.get(i));
				}
				if (i % 5 == 4) {
					genre.add(wikiArrayList.get(i));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
