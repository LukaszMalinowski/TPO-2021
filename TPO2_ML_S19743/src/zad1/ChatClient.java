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
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ChatClient {

    private final String host;
    private final int port;
    private final String id;
    private final List<String> chatViewList;
    private SocketChannel channel;
    private static final Charset CHARSET = Charset.forName("ISO-8859-2");
    private boolean clientIsWorking;
    private final Thread receiveThread;

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        clientIsWorking = false;
        chatViewList = new ArrayList<>();
        receiveThread = new Thread(this::receive);
    }

    public void login() {
        String req = id + " logged in";

        try {
            channel = SocketChannel.open(new InetSocketAddress(host, port));
            channel.configureBlocking(false);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        clientIsWorking = true;

        receiveThread.start();

        send(req);
    }

    public void logout() {
        String req = id + " logged out";

        send(req);

        try {
            Thread.sleep(5);
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        clientIsWorking = false;
        receiveThread.interrupt();

        chatViewList.add(req);

        try {
            channel.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String req) {
        try {
            Thread.sleep(1);
        }
        catch (InterruptedException ignored) {
        }
        CharBuffer charBuffer = CharBuffer.wrap(req);
        ByteBuffer byteBuffer = CHARSET.encode(id + ": " + charBuffer);

        try {
            if (channel.isConnected()) {
                channel.write(byteBuffer);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive() {
        while (clientIsWorking) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder stringBuilder = new StringBuilder();

            buffer.clear();
            stringBuilder.setLength(0);

            try {
                while (channel.read(buffer) > 0) {
                    buffer.flip();
                    CharBuffer decode = CHARSET.decode(buffer);
                    stringBuilder.append(decode);
                    buffer.clear();
                }
            }
            catch (IOException ignored) {
            }

            if(!stringBuilder.toString().isEmpty()) {
                chatViewList.add(stringBuilder.toString());
            }
        }
    }

    public String getChatView() {
        StringBuilder chatView = new StringBuilder();
        chatView.append("=== ")
                .append(id)
                .append(" chat view")
                .append("\n");
        for (String message : chatViewList) {
            chatView.append(message)
                    .append("\n");
        }
        return chatView.toString();
    }
}
