import java.sql.SQLException;

public class RadioApp {



	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		//ParseData parsing = new ParseData();
		RadioDAO instance = new RadioDAO ();
		//RadioDAO.getRadius();
	instance.ParseWiki();
		//RadioDAO.addCity();
		instance.addData();
		//RadioDAO.getRadioStations();
		//parsing.getSites();
	}
}