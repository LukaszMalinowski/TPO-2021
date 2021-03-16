/**
 * @author Malinowski Łukasz S19743
 */

package zad2;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.json.JSONObject;

import java.io.IOException;

public class Service {
    private final String WEATHER_API_KEY = "44f6dad7325facf5be789bcb3678ab09";

    private final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
    private final String EXCHANGE_API_URL = "https://api.exchangeratesapi.io/latest?base=%s";

    private String country;
    OkHttpClient client;

    public Service(String country) {
        this.country = country;
        this.client = new OkHttpClient();
    }

    public String getWeather(String city) {
        String url = String.format(WEATHER_API_URL, city, WEATHER_API_KEY);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        String response = "";
        try {
            response = client.newCall(request).execute().body().string();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public Double getRateFor(String currencyCode) {
        String url = String.format(EXCHANGE_API_URL, currencyCode);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        JSONObject json = null;

        try {
            ResponseBody response = client.newCall(request).execute().body();
            json = new JSONObject(response.string());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (json != null) {
            double rate = json.getJSONObject("rates").getDouble("PLN");
            System.out.println(rate);
            return rate;
        }

        return null;
    }

    public Double getNBPRate() {
        return null;
    }
}  
