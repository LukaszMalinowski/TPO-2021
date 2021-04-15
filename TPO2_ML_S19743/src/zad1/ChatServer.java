/**
 *
 *  @author Malinowski Łukasz S19743
 *
 */

package zad1;


public class ChatServer {

    private String host;
    private int port;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startServer() {
        System.out.println("Server started");
    }

    public void stopServer() {
        System.out.println("Server stopped");
    }


    public String getServerLog() {
        return null;
    }
}
