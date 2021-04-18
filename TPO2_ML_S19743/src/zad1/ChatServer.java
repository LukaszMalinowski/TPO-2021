/**
 *
 *  @author Malinowski Łukasz S19743
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
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
    private static Charset charset  = Charset.forName("ISO-8859-2");

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

                keys.forEach(key -> {
                    if(key.isAcceptable()) {
                        try {
                            SocketChannel socketChannel = serverChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(key.isReadable()) {
                        try {
                            SocketChannel socketChannel = serverChannel.accept();
                            processRequest(socketChannel);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void processRequest(SocketChannel socketChannel) {
        if(!socketChannel.isOpen())
            return;

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuffer stringBuffer = new StringBuffer();

        buffer.clear();
        stringBuffer.setLength(0);

        try {
            readLoop:
            while (true) {
                int read = socketChannel.read(buffer);

                if(read > 0) {
                    buffer.flip();
                    CharBuffer decode = charset.decode(buffer);
                    while (decode.hasRemaining()) {
                        char c = decode.get();
                        if (c == '\r' || c == '\n')
                            break readLoop;
                        stringBuffer.append(c);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String request = stringBuffer.toString();

        //TODO append datetime to request, add it to servel log, and send to all channels
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
