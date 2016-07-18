# GrandCircus-FinalProject-Radio-Locator
Developed by Brian McDermott and Zac Manring, the radio locator web app serves the purposes of displaying a directory of available radio stations within a radius of a given location.
The coordinates are entered into the Google Maps API or provided with HTML5's built in Geolocation.
These coords are used to query the FCC and the data is then parsed from the HTML and stored in a postgresQL database by call sign, city and frequency as long as they are within a relevant search radius predetermined to be the radius with the strongest signal.
A persistent database exists with each radio station's genre and the two databases are merged then displayed to the user on the front end via a sortable jQuery table.
