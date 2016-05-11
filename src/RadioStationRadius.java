
public class RadioStationRadius {
	private String callSign;
	private String frequency;
	private String city;
	private String genre;
	
	
	public RadioStationRadius() {
		
	}


	public RadioStationRadius(String callSign, String frequency, String city, String genre) {
		super();
		this.callSign = callSign;
		this.frequency = frequency;
		this.city = city;
		this.genre = genre;
	}

	
	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getCallSign() {
		return callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return callSign;
	}
}
