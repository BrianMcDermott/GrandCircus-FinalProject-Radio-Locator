import java.sql.SQLException;

public class RadioApp {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		UpdateRadioDatabase update = new UpdateRadioDatabase();
		RadioDAO add = new RadioDAO();
	
		update.ParseRadioInfo();
		add.addRadioInfo();
	}
	//backup radius database if primary goes down
	/*
	 * public static ArrayList<RadioStationRadius> getRadius(String[]y,
	 * String[]x) { URL url; System.out.println("Checkpoint 1"); String
	 * radiusSite =
	 * "https://transition.fcc.gov/fcc-bin/fmq?call=&arn=&state=&city=&freq=0.0&fre2=107.9&serv=&vac=&facid=&asrn=&class=&list=4&dist=50&dlat2="
	 * +x[0]+"&mlat2="
	 * +x[1]+"&slat2="+x[2]+"&NS=N&dlon2="+y[0]+"&mlon2="+y[1]+"&slon2="+y[2]+
	 * "&EW=W&size=9&NextTab=Results+to+Next+Page%2FTab"; try { // Get URL
	 * content url = new URL(radiusSite);
	 * 
	 * // Opens connection to the website HttpURLConnection conn =
	 * (HttpURLConnection) url.openConnection();
	 * 
	 * BufferedReader htmlInfo = new BufferedReader(new
	 * InputStreamReader(conn.getInputStream())); toArrayList(htmlInfo);
	 * 
	 * 
	 * ArrayList<RadioStationRadius> radioList = toArrayList(htmlInfo);
	 * System.out.println(radioList);
	 * 
	 * System.out.println("Checkpoint 2"); System.out.println(radiusSite); }
	 * catch (MalformedURLException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * return radioList; }
	 * 
	 * public static ArrayList<RadioStationRadius> toArrayList(BufferedReader
	 * htmlInfo) throws IOException { String inputLine; while ((inputLine =
	 * htmlInfo.readLine()) != null) {
	 * 
	 * RadioStationRadius radioStation = new RadioStationRadius();
	 * 
	 * inputLine = inputLine.replace("|", "");
	 * 
	 * String[] split = inputLine.split(" "); for (int m = 0; m < split.length;
	 * m++) { if (m == 0) { radioStation.setCallSign((split[m])); ;
	 * radioList.add(radioStation); } }
	 * 
	 * } return radioList; }
	 */
}