import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ChatServer {

    private final int PORT = 9090;
    private int _counter = 1;
    private ArrayList<ClientSocket> _clients;
    private ServerSocket server = null;

    public ChatServer() {
        _clients = new ArrayList<>();

        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("failed to listen on port: " + PORT);
        }
        System.out.println("listening on port: " + PORT);

        while (true) {
            try {
                System.out.println("waiting for new client...");
                ClientSocket client = new ClientSocket(server.accept(), _counter,
                        _clients.stream().map(c -> c.Name).collect(Collectors.toList()),
                        new MessageListener(), new LogoutListener(), new LoginListener());
                _clients.add(client);
                client.start();
                System.out.println(_counter + " accepted");
            } catch (IOException e) {
                System.out.println("failed to accept client " + _counter);
            }
            finally {
                _counter++;
            }
        }
    }

    class MessageListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            var message = e.getActionCommand();
            System.out.println(message);
            _clients.forEach(c -> c.sendText(message));
        }
    }

    class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            var id = e.getID();
            var loginMessage = e.getActionCommand() + " logged in";
            System.out.println(loginMessage);
            _clients.forEach(c -> c.sendText(loginMessage));
        }
    }

    class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            var id = e.getID();
            var logoutMessage = e.getActionCommand() + " logged out";
            System.out.println(logoutMessage);
            _clients.removeIf(c -> id == c.Id);
            _clients.forEach(c -> c.sendText(logoutMessage));
        }
    }
}