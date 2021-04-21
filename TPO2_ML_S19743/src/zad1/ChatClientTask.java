/**
 *
 * @author Malinowski Łukasz S19743
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ChatClientTask extends FutureTask<ChatClient> {

    private ChatClientTask(Callable<ChatClient> callable) {
        super(callable);
    }

    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask(() -> {
            c.login();
            sleep(wait);
            msgs.forEach(messages -> {
                c.send(messages);
                sleep(wait);
            });
            c.logout();
            sleep(wait);

            return c;
        });
    }

    public ChatClient getClient() {
        try {
            return this.get();
        }
        catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static void sleep(int wait) {
        if (wait != 0) {
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
        }
    }
}
