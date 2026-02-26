/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package weatherapiclient;

/**
 *
 * @author Disha
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPIClient {

    public static void main(String[] args) {

        try {
            String apiUrl =
                "https://api.open-meteo.com/v1/forecast"
              + "?latitude=23.02&longitude=72.57"
              + "&current_weather=true";

            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br =
                new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            String json = response.toString();

            // -------- Extract current_weather safely --------
            String cwKey = "\"current_weather\":{";
            int cwStart = json.indexOf(cwKey);

            if (cwStart == -1) {
                System.out.println("Current weather data not found.");
                return;
            }

            cwStart += cwKey.length();
            int cwEnd = json.indexOf("}", cwStart);
            String currentWeather = json.substring(cwStart, cwEnd);

            // Extract values safely
            String temperature = getValue(currentWeather, "temperature");
            String windspeed   = getValue(currentWeather, "windspeed");
            String weathercode = getValue(currentWeather, "weathercode");

            // Structured output
            System.out.println("🌦️ Current Weather Report");
            System.out.println("--------------------------");
            System.out.println("Temperature : " + temperature + " °C");
            System.out.println("Wind Speed  : " + windspeed + " km/h");
            System.out.println("Weather Code: " + weathercode);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 🔒 Safe value extractor
    public static String getValue(String json, String key) {
        int keyIndex = json.indexOf("\"" + key + "\":");
        if (keyIndex == -1) return "N/A";

        keyIndex += key.length() + 3;
        int commaIndex = json.indexOf(",", keyIndex);

        if (commaIndex == -1) {
            return json.substring(keyIndex).trim();
        }
        return json.substring(keyIndex, commaIndex).trim();
    }
}
