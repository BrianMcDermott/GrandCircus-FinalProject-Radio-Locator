import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RadioDAO {
		ArrayList<String> callSign = new ArrayList<String>();
		ArrayList<String> frequency = new ArrayList<String>();
		ArrayList<String> city = new ArrayList<String>();
		ArrayList<String> genre = new ArrayList<String>();

	private PreparedStatement preparedStatement = null;

	public Connection getConnection() throws ClassNotFoundException, SQLException {
	     String URL = System.getenv("DATABASE_URL_PARAMS");
		 String Username = System.getenv("DATABASE2_USERNAME");
		 String Password = System.getenv("DATABASE2_PASSWORD");
		
	Class.forName("org.postgresql.Driver");
	return DriverManager.getConnection(URL, Username, Password);
	}

	public void truncateTable() throws SQLException, ClassNotFoundException {
		Connection connect = getConnection();
		preparedStatement = connect.prepareStatement("truncate table city");
		preparedStatement.executeUpdate();
		connect.close();

	}

	public void addCityRadius(ArrayList<String> fccList) throws SQLException, ClassNotFoundException {

		Connection connect = getConnection();

		preparedStatement = connect.prepareStatement("INSERT INTO city (callsign) VALUEs (?) ON CONFLICT DO NOTHING");
		for (int i = 0; i < fccList.size(); i++) {
			if (fccList.get(i).length() > 4) {
				String moddedCallSign = fccList.get(i).substring(0, 4);
				preparedStatement.setString(1, moddedCallSign);
			} else {
				preparedStatement.setString(1, fccList.get(i));
			}
			preparedStatement.executeUpdate();
		}
		connect.close();
	}

	public void addRadioInfo() throws ClassNotFoundException, SQLException {

		Connection connect = getConnection();
		preparedStatement = connect.prepareStatement(
				"INSERT INTO information (callsign, frequency, city, genre) VALUES (?, ?, ?, ?)");
		for (int j = 0; j < callSign.size(); j++) {
			if (callSign.get(j).length() > 4) {
				String moddedCallSign = callSign.get(j).substring(0, 4);
				preparedStatement.setString(1, moddedCallSign);
			} else {
				preparedStatement.setString(1, callSign.get(j));
			}
			preparedStatement.setString(2, frequency.get(j));
			preparedStatement.setString(3, city.get(j));
			preparedStatement.setString(4, genre.get(j));
			preparedStatement.executeUpdate();

		}
		connect.close();
		System.out.println("finished saturday");
	}

	public ArrayList<RadioStationRadius> displayRadioStations() throws ClassNotFoundException, SQLException {
		Connection connect = getConnection();
		ArrayList<RadioStationRadius> radioList = new ArrayList<RadioStationRadius>();

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

}
