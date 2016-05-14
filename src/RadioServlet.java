
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class RadioServlet
 */
@WebServlet("/RadioServlet")
public class RadioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	RadioDAO radio = new RadioDAO();
	ParseData parseData = new ParseData();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RadioServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<RadioStationRadius> results = new ArrayList<>();
		Gson name = new Gson();

		try {
			results = radio.displayRadioStations();
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		response.getWriter().append(name.toJson(results));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			radio.truncateTable();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String lon = request.getParameter("longitude");
		String lat = request.getParameter("latitude");
		System.out.println(lon + " " + lat);
		String[] CONVlatitude = parseData.convertDEGtoDMS(lat);
		String[] CONVlongitude = parseData.convertDEGtoDMS(lon);
		String fm = "FM";
		String am = "AM";

		ArrayList<String> fmfccList = parseData.parseGetRadius(fm, CONVlongitude, CONVlatitude);
		ArrayList<String> amfccList = parseData.parseGetRadius(am, CONVlongitude, CONVlatitude);
		try {
			radio.addCityRadius(fmfccList);
			radio.addCityRadius(amfccList);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// results.clear();
	}
}
