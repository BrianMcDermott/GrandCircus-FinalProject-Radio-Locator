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
	static ArrayList<String> fccList = new ArrayList<String>();
	
	
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
	/*public static ArrayList<RadioStationRadius> getRadius(String[]y, String[]x) {
		URL url;
		System.out.println("Checkpoint 1");
		String radiusSite = "https://transition.fcc.gov/fcc-bin/fmq?call=&arn=&state=&city=&freq=0.0&fre2=107.9&serv=&vac=&facid=&asrn=&class=&list=4&dist=50&dlat2=" +x[0]+"&mlat2=" +x[1]+"&slat2="+x[2]+"&NS=N&dlon2="+y[0]+"&mlon2="+y[1]+"&slon2="+y[2]+"&EW=W&size=9&NextTab=Results+to+Next+Page%2FTab";
		try {
			// Get URL content
			url = new URL(radiusSite);

			// Opens connection to the website
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			BufferedReader htmlInfo = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			toArrayList(htmlInfo);

			
			 * ArrayList<RadioStationRadius> radioList = toArrayList(htmlInfo);
			 * System.out.println(radioList);
			 
			System.out.println("Checkpoint 2");
			System.out.println(radiusSite);
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
	}*/
	
	private static Connection connect = null;

	private static PreparedStatement preparedStatement = null;

	public static void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/radioapp", "root", "sesame");
	}

	public static void addCity() throws SQLException, ClassNotFoundException {
		getConnection();
		

		preparedStatement = connect.prepareStatement("INSERT IGNORE INTO radioapp.city (callsign) VALUEs (?)");
		for (int i = 0; i < fccList.size(); i++) {
			if(fccList.get(i).length() > 4){
				String moddedCallSign = fccList.get(i).substring(0, 4);
				preparedStatement.setString(1, moddedCallSign);
			}else{
				
				preparedStatement.setString(1, fccList.get(i));
			}
			preparedStatement.executeUpdate();

		}

		
		connect.close();
	}
	
/*	static void truncateTable() throws SQLException, ClassNotFoundException {
		getConnection();
		preparedStatement = connect.prepareStatement("truncate table city");
		preparedStatement.executeUpdate();
		connect.close();
	}*/

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
//		preparedStatement = connect.prepareStatement("select substring( callsign, 1, 4 ) from radioapp.information");
//		preparedStatement = connect.prepareStatement("select substring( callsign, 1, 4 ) from radioapp.city");
		
		preparedStatement = connect.prepareStatement(
				"SELECT city.callsign, information.frequency,information.city,information.genre FROM city INNER JOIN information ON city.callsign = information.callsign");
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
	public static void parseFCC(String[]y, String[]x) {
        String html = "http://www.fccinfo.com/CMDProFacLookup.php?sCurrentService=AM&sKilometers=50&sLatitude="+x[0]+ "-"+ x[1]+"-"+x[2]+"&sLongitude="+ y[0]+"-"+y[1]+"-"+y[2]+"&tabSearchType=Within+Search";
        try {
            Document doc = Jsoup.connect(html).get();
            Elements tableElements = doc.select("tbody");
            Elements tableRowElements = tableElements.select("tr");
            
            for (int i = 8; i < tableRowElements.size(); i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
              
                for (int j = 1; j < rowItems.size(); j+=11) {
                    fccList.add(rowItems.get(j).text());
                }
                
            }
            System.out.println("this is the fcc " + fccList);
            System.out.println("test " + html);

        } catch (IOException e) {
            e.printStackTrace();
        }
     
    }
}
