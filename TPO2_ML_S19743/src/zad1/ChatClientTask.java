/**
 *
 * @author Malinowski Łukasz S19743
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatClientTask implements Runnable {

    private ChatClient client;
    private List<String> messages;
    private int wait;

    private ChatClientTask(ChatClient client, List<String> messages, int wait) {
        this.client = client;
        this.messages = messages;
        this.wait = wait;
    }

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask(c, msgs, wait);
    }

    @Override
    public void run() {
        client.login();
        sleep(wait);
        messages.forEach(messages -> {
            client.send(messages);
            sleep(wait);
        });
        client.logout();
        sleep(wait);
    }

    public void get() throws InterruptedException, ExecutionException {

    }

    public ChatClient getClient() {
        return null;
    }

    private void sleep(int wait) {
        if (wait != 0) {
            try {
                this.wait(wait);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
