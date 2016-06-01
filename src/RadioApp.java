import java.sql.SQLException;

public class RadioApp {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		UpdateRadioDatabase update = new UpdateRadioDatabase();
		RadioDAO add = new RadioDAO();
	
		update.ParseRadioInfo();
		add.addRadioInfo();
	}
}