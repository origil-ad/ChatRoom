import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer {

    final int PORT = 9090;
    int _counter = 1;
    ArrayList<ClientSocket> _clients;
    ServerSocket server = null;

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
                ClientSocket client = new ClientSocket(server.accept(), _counter, new MessageListener(), new LogoutListener(), new LoginListener());
                _clients.add(client);
                client.start();
                System.out.println("accepted");
            } catch (IOException e) {
                System.out.println("failed to accept client ");
            }
        }

//        while (!_clients.isEmpty()) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            server.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    class MessageListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            var text = e.getActionCommand();
            _clients.forEach(c -> c.sendText(text));
        }
    }

    class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            var id = e.getID();
            var loginMessage = e.getActionCommand() + " logged in";
            System.out.println(loginMessage);
            _clients.forEach(c -> {
                if (id != c.Id) {
                    c.sendText(loginMessage);
                }
            });
        }
    }

    class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            var id = e.getID();
            System.out.println(id);
            System.out.println(_clients.removeIf(c -> id == c.Id));
        }
    }
}