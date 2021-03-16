/**
 *
 *  @author Malinowski Łukasz S19743
 *
 */

package zad2;


import javafx.application.Application;

import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI

    SwingUtilities.invokeLater(() -> new Gui().setVisible(true));
  }
}
