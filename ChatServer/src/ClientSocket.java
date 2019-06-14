import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientSocket extends Thread {
    public String Name;
    public int Id;
    Socket _socket;
    PrintWriter _out = null;
    BufferedReader _in = null;
    ActionListener _messageListener;
    ActionListener _removeListener;
    ActionListener _loginListener;
    List<String>  _initialParticipants;

    public ClientSocket(Socket socket, int id, List<String> participants, ActionListener messageListener, ActionListener removeListener, ActionListener loginListener){
        _socket = socket;
        Id = id;
        Name = ""+id;
        _messageListener = messageListener;
        _removeListener = removeListener;
        _loginListener = loginListener;
        _initialParticipants = participants;

        try {
            _out = new PrintWriter(_socket.getOutputStream(), true);
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        handleParticipantsNames();
        String input;
        try {
            while ((input = _in.readLine()) != null) {
                input = Name + ": " + input;
                _messageListener.actionPerformed(new ActionEvent(this, Id, input));
            }
        } catch (IOException e) {
            _out.close();
            try {
                _in.close();
                _socket.close();
            } catch (IOException ex) {}
            finally {
                _removeListener.actionPerformed(new ActionEvent(this, Id, Name));
            }
        }
    }

    private void handleParticipantsNames() {
        try {
            //first communication is new client's name and participants names
            Name = _in.readLine();
            _out.println(_initialParticipants.toString());
            _loginListener.actionPerformed(new ActionEvent(this, Id, Name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendText(String text){
        _out.println(text);
    }
}