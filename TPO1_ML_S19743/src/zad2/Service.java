/**
 * @author Malinowski Łukasz S19743
 */

package zad2;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.StringReader;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Service {
    private final String WEATHER_API_KEY = "44f6dad7325facf5be789bcb3678ab09";

    private final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
    private final String EXCHANGE_API_URL = "https://api.exchangeratesapi.io/latest?base=%s&symbols=%s";
    private final String NBP_TABLE_A_URL = "https://www.nbp.pl/kursy/xml/a051z210316.xml";
    private final String NBP_TABLE_B_URL = "https://www.nbp.pl/kursy/xml/b010z210310.xml";

    private String country;
    OkHttpClient client;
    Map<String, String> countries = new HashMap<>();

    public Service(String country) {
        this.country = country;
        this.client = new OkHttpClient();

        Locale.setDefault(new Locale("", "en"));

        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
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
        String currency = getCurrencyCode();

        String url = String.format(EXCHANGE_API_URL, currencyCode, currency);
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
            double rate = json.getJSONObject("rates").getDouble(currency);
            return rate;
        }

        return null;
    }

    public Double getNBPRate() {
        String currency = getCurrencyCode();

        if (currency.equals("PLN")) {
            return 1d;
        }

        //TODO get current url
        
        Request requestTableA = new Request.Builder()
                .url(NBP_TABLE_A_URL)
                .method("GET", null)
                .build();

        Request requestTableB = new Request.Builder()
                .url(NBP_TABLE_B_URL)
                .method("GET", null)
                .build();

        try {
            ResponseBody responseTableA = client.newCall(requestTableA).execute().body();
            ResponseBody responseTableB = client.newCall(requestTableB).execute().body();

            Double value1 = searchResponseForCurrency(responseTableA.string(), currency);

            if(value1 != null) {
                return value1;
            }

            Double value2 = searchResponseForCurrency(responseTableB.string(), currency);

            if(value2 != null) {
                return value2;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Double searchResponseForCurrency(String response, String currency) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(response)));
            Element rootElement = document.getDocumentElement();

            NodeList elements = rootElement.getElementsByTagName("pozycja");

            for (int i = 0; i < elements.getLength(); i++) {
                Node node = elements.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String tempCurrencyCode = element.getElementsByTagName("kod_waluty").item(0).getTextContent();
                    if(tempCurrencyCode.equals(currency)) {
                        String value = element.getElementsByTagName("kurs_sredni").item(0).getTextContent();
                        return Double.parseDouble(value.replace(",","."));
                    }
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCurrencyCode() {
        String localeCode = countries.get(country);
        return Currency.getInstance(new Locale("", localeCode)).toString();
    }

    private static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}  
