/**
 * @author Malinowski Łukasz S19743
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChatClient {

    private String host;
    private int port;
    private String id;
    private List<String> chatViewList;
    private SocketChannel channel;
    private static Charset charset = Charset.forName("ISO-8859-2");

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        chatViewList = new ArrayList<>();
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

        send(req);
    }

    public void logout() {
        String req = id + " logged out";
        send(req);

        try {
            channel.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String req) {
        CharBuffer charBuffer = CharBuffer.wrap(req);
        ByteBuffer byteBuffer = charset.encode(charBuffer);

        try {
            if (channel.isConnected()) {
                channel.write(byteBuffer);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
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
        //TODO I'll probably need to remove last newline char
        return chatView.toString();
    }
}
