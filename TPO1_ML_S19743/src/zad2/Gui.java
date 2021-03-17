package zad2;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {

    Service service;

    public Gui() {
        super();

        this.service = new Service("Poland");

        JPanel weatherPanel = new JPanel();
        JTextField cityTextField = new JTextField();
        cityTextField.setText("Warsaw");
        JButton cityButton = new JButton("Get weather for city in textfield");
        JTextArea weatherTextArea = new JTextArea();
        weatherTextArea.setEditable(false);
        weatherTextArea.setLineWrap(true);

        JPanel nbpPanel = new JPanel();
        JTextField countryTextField = new JTextField();
        countryTextField.setText("Poland");
        JButton nbpButton = new JButton("Get rate for country in textfield");
        JLabel nbpLabel = new JLabel();

        JPanel ratePanel = new JPanel();
        JTextField currencyCodeTextField = new JTextField();
        currencyCodeTextField.setText("USD");
        JButton currencyButton = new JButton("Get rate for currency in textfield");
        JLabel rateLabel = new JLabel();

        cityTextField.setPreferredSize(new Dimension(100, 30));
        countryTextField.setPreferredSize(new Dimension(100, 30));
        currencyCodeTextField.setPreferredSize(new Dimension(100, 30));

        weatherTextArea.setPreferredSize(new Dimension(600, 100));

        weatherPanel.add(cityTextField, TOP_ALIGNMENT);
        weatherPanel.add(cityButton, TOP_ALIGNMENT);
        weatherPanel.add(weatherTextArea, BOTTOM_ALIGNMENT);

        cityButton.addActionListener(event -> {
            String weatherJson = service.getWeather(cityTextField.getText());
            weatherTextArea.setText(weatherJson);
        });

        rateLabel.setLayout(new GridLayout(2, 1));

        ratePanel.add(currencyCodeTextField);
        ratePanel.add(currencyButton);
        ratePanel.add(rateLabel);

        currencyButton.addActionListener(event -> {
            if (!countryTextField.getText().equals("")) {
                service.setCountry(countryTextField.getText());
            }

            Double rate = service.getRateFor(currencyCodeTextField.getText().toUpperCase());
            rateLabel.setText(String.valueOf(rate));
        });

        nbpPanel.add(countryTextField);
        nbpPanel.add(nbpButton);
        nbpPanel.add(nbpLabel);

        nbpButton.addActionListener(event -> {
            service.setCountry(countryTextField.getText());

            Double rate = service.getNBPRate();
            nbpLabel.setText(String.valueOf(rate));
        });

        this.setLayout(new GridLayout(4, 1));

        this.add(weatherPanel, TOP_ALIGNMENT);
        this.add(ratePanel, CENTER_ALIGNMENT);
        this.add(nbpPanel, BOTTOM_ALIGNMENT);

//        JFXPanel jfxPanel = new JFXPanel();
//        Platform.runLater(() -> {
//            WebView webView = new WebView();
//            WebEngine engine = webView.getEngine();
//            engine.load("https://en.wikipedia.org/wiki/" + cityTextField.getText());
//            jfxPanel.setScene(new Scene(webView));
//        });

        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
