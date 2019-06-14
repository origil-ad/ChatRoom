import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientSocket extends Thread {
    String Name;
    int Id;
    private Socket _socket;
    private PrintWriter _out = null;
    private BufferedReader _in = null;
    private ActionListener _messageListener;
    private ActionListener _removeListener;
    private ActionListener _loginListener;
    private List<String>  _initialParticipants;

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
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            finally {
                _removeListener.actionPerformed(new ActionEvent(this, Id, Name));
            }
        }
    }

    private void handleParticipantsNames() {
        try {
            //first communication is new client's name and participants names
            Name = _in.readLine();
            String participants = "";
            for (var p : _initialParticipants) participants += "," + p; // do not use toString because of redundant spaces
            _out.println(participants.length() > 1 ? participants.substring(1) : ""); // remove first comma (,)
            _loginListener.actionPerformed(new ActionEvent(this, Id, Name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendText(String text){
        _out.println(text);
    }
}