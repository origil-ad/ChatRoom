import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientController {

    final int PORT = 9090;

    ClientView _view;
    Socket _socket = null;
    PrintWriter _out = null;
    BufferedReader _in = null;
    boolean _toListen = true;

    ClientController(ClientView view) {
        _view = view;
        _view.addEventListener(new InputListener());
        initTCP();
        startListening();
    }

    private void initTCP() {
        try {
            _socket = new Socket("localhost", PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            _out = new PrintWriter(_socket.getOutputStream(), true);
            _in = new BufferedReader(new InputStreamReader(
                    _socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startListening() {
        while (_toListen){
            try {
                var message = _in.readLine();
                _view.print(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        closeTCP();
    }

    private void closeTCP() {
        try {
            _out.close();
            _in.close();
            _socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class InputListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            var text = e.getActionCommand();
            _out.println(text);
        }
    }
}