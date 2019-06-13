import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket extends Thread {
    public int Id;
    Socket _socket;
    PrintWriter _out = null;
    BufferedReader _in = null;
    ActionListener _messageListener;
    ActionListener _removeListener;

    public ClientSocket(Socket socket, int id, ActionListener messageListener, ActionListener removeListener){
        _socket = socket;
        Id = id;
        _messageListener = messageListener;
        _removeListener = removeListener;
        try {
            _out = new PrintWriter(_socket.getOutputStream(), true);
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String input;
        try {
            while ((input = _in.readLine()) != null) {
                System.out.println("receive:" + input);
                _messageListener.actionPerformed(new ActionEvent(this, Id, input));
                //sendText(input);
                System.out.println("sent: " + input);
            }
        } catch (IOException e) {
            _out.close();
            try {
                _in.close();
                _socket.close();
            } catch (IOException ex) {}
            finally {
                _removeListener.actionPerformed(new ActionEvent(this, Id, ""));
                notify();
            }
        }
    }

    public void sendText(String text){
        _out.println(text);
    }
}