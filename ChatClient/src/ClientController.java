import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientController {

    private final int PORT = 9090;
    private final String LOGOUT_TEXT = " logged out";
    private final String LOGIN_TEXT = " logged in";

    private ClientView _view;
    private Socket _socket = null;
    private PrintWriter _out = null;
    private BufferedReader _in = null;
    private boolean _toListen = true;

    ClientController(ClientView view) {
        _view = view;
        _view.addEventListener(new InputListener());
        initTCP();
        _view.setClientName();
        _view._setParticipantsNames(getParticipantsNames());
        listen();
    }

    private String[] getParticipantsNames() {
        String[] names = {};
        try {
            var str =_in.readLine();
            names = str.split(",");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }

    private void initTCP() {
        try {
            var host = JOptionPane.showInputDialog("Type server host, leave empty for localhost:");
            _socket = new Socket(host.isEmpty() ? "localhost" : host, PORT);
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

    private void listen() {
        while (_toListen){
            try {
                var message = _in.readLine();
                if (message.contains(LOGOUT_TEXT)){
                    _view.removeParticipant(message.substring(0, message.indexOf(LOGOUT_TEXT)));
                }
                else if (message.contains(LOGIN_TEXT)){
                    _view.addParticipant(message.substring(0, message.indexOf(LOGIN_TEXT)));
                }
                _view.print(message);
            } catch (IOException e) {
                _toListen = false;
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
            try {
                var textField = (JTextField)e.getSource();
                textField.setText("");
            } catch (Exception ex) {
            }
            _out.println(text);
        }
    }
}