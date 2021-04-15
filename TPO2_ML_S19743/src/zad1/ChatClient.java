/**
 * @author Malinowski Łukasz S19743
 */

package zad1;


import java.util.ArrayList;
import java.util.List;

public class ChatClient {

    private String host;
    private int port;
    private String id;
    List<String> chatViewList;

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        chatViewList = new ArrayList<>();
    }

    public void login() {
        String req = id + " logged in";
        send(req);
    }

    public void logout() {
        String req = id + " logged out";
        send(req);
    }

    public void send(String req) {
        chatViewList.add(req);

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
