/**
 * @author Malinowski Łukasz S19743
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatServer extends Thread {

    private final String host;
    private final int port;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    boolean serverIsRunning;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private final List<String> serverLog = Collections.synchronizedList(new ArrayList<>());
    private static final Charset charset = Charset.forName("ISO-8859-2");
    private final Map<String, SocketChannel> clients;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
        clients = new HashMap<>();
    }

    public void startServer() {
        System.out.println("Server started");
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(host, port));
            serverChannel.configureBlocking(false);
            selector = Selector.open();

            serverChannel.register(selector, serverChannel.validOps(), null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.start();
    }

    @Override
    public void run() {
        serverIsRunning = true;

        while (serverIsRunning) {
            try {
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();

                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        processRequest(socketChannel);
                    }
                }
            }
            catch (IOException e) {
            }
        }
    }

    private void processRequest(SocketChannel socketChannel) {
        if (!socketChannel.isOpen())
            return;

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuilder stringBuilder = new StringBuilder();

        buffer.clear();
        stringBuilder.setLength(0);

        try {
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                CharBuffer decode = charset.decode(buffer);
                stringBuilder.append(decode);
                buffer.clear();
            }
        }
        catch (IOException e) {
        }

        String request = stringBuilder.toString();

        if (request.contains("logged in")) {
            clients.put(request.substring(0, request.indexOf(' ')), socketChannel);
        }

        //TODO process logged out

        if (!stringBuilder.toString().isEmpty())
            sendMessage(stringBuilder.toString());
    }

    private void sendMessage(String message) {
        serverLog.add(simpleDateFormat.format(System.currentTimeMillis()) + " " + message);

        clients.forEach((client, socket) -> {
            ByteBuffer buffer = charset.encode(CharBuffer.wrap(message));
            try {
                socket.write(buffer);
            }
            catch (IOException e) {
            }
        });
    }

    public void stopServer() {
        System.out.println("Server stopped");
        serverIsRunning = false;
        this.interrupt();
        try {
            serverChannel.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getServerLog() {
        StringBuilder stringBuilder = new StringBuilder();
        serverLog.forEach(log -> {
            stringBuilder.append(log);
            stringBuilder.append("\n");
        });
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
