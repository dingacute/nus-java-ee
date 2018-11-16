package workshop01.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

// This program is to call an external service web address to search and display
// the weather of the city which you input from the web page
@WebServlet(urlPatterns = { "/weather" })
public class WeatherServlet extends HttpServlet{

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String APPID = "2ed6d71a7125ee4f69aadcadcc508d6a";
    private Client client;
    
    //Initialize the client
    @Override
    public void init()
            throws ServletException {        
        client = ClientBuilder.newClient();
    }

    //Cleanup
    @Override
    public void destroy() {
        client.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        //Form field name
        String cityName = req.getParameter("cityName");
        
        //if no input city name, default is Singapore
        //.trim will remove all the white spaces at the sides
        if ((cityName == null) || (cityName.trim().length() <= 0)) {
            cityName = "Singapore";
        }
        
        //Start to setup the resource part
        //Create the target
        WebTarget target = client.target(WEATHER_URL);
        //Set the query string
        target = target.queryParam("q", cityName);
        target = target.queryParam("appid", APPID);
        target = target.queryParam("units", "metric");
        
        //or you can chain all them together
        /*WebTarget target = client.target(WEATHER_URL)
                .queryParam("q", cityName)
                .queryParam("appid", APPID)
                .queryParam("units", "metric");*/
        
        //Create invocation, expect JSON as result
        Invocation.Builder inv = target.request(MediaType.APPLICATION_JSON);
        //Complete the setup of the resource part
        
        //Make the call using GET HTTP method
        //result will be null if there are error
        JsonObject result = inv.get(JsonObject.class);
        JsonArray weatherDetails = result.getJsonArray("weather");
        
        log("RESULT: " + result);
        
        //Echo back the name
        //Set the 200 OK status code
        resp.setStatus(HttpServletResponse.SC_OK);
        
        //Set the content type/media type
        //We are returning text/html
        resp.setContentType(MediaType.TEXT_HTML);
        
        try (PrintWriter pw = resp.getWriter()) {
            pw.print("<h2>The weather for " + cityName.toUpperCase() + "</h2>");
            for (int i = 0; i < weatherDetails.size(); i++) {
                JsonObject wd = weatherDetails.getJsonObject(i);
                String main = wd.getString("main");
                String description = wd.getString("description");
                String icon = wd.getString("icon");//an image for this weather
                pw.print("<div>");
                //inside the html, type &dash instead of real dash '-'
                //pw.printf("%s &dash; %s", main, description);
                pw.print(main + " &dash; " + description);
                pw.printf("<img src=\"http://openweathermap.org/img/w/%s.png\">", icon);
                pw.print("</div>");
            }
        }
    }
    
    
}
