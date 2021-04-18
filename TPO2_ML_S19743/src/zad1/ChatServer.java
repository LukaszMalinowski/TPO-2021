/**
 *
 *  @author Malinowski Łukasz S19743
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ChatServer {

    private String host;
    private int port;
    boolean serverIsRunning;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private final List<String> serverLog = Collections.synchronizedList(new ArrayList<>());

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startServer() {
        System.out.println("Server started");
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(host, port));
            serverChannel.configureBlocking(false);
            selector = Selector.open();

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        serviceConnections();
    }

    private void serviceConnections() {
        serverIsRunning = true;

        while (serverIsRunning) {
            try {
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void stopServer() {
        System.out.println("Server stopped");
        try {
            serverChannel.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getServerLog() {
        return null;
    }
}
