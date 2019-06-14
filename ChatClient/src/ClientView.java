import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ClientView extends JFrame {

    private final int SIZE = 400;
    private JTextField _textEditor;
    private JTextArea _chatDisplay;
    private JTextArea _participantsDisplay;
    //todo: add scroll to textArea

    List<ActionListener> _listeners = new ArrayList<>();

    public ClientView() {

        _textEditor = new JTextField();
        _chatDisplay = new JTextArea();
        _participantsDisplay = new JTextArea();

        _chatDisplay.setEditable(false);
        _chatDisplay.setPreferredSize(new Dimension((SIZE * 2 / 3) - 30, SIZE));
        _participantsDisplay.setEditable(false);
        _participantsDisplay.setPreferredSize(new Dimension(SIZE / 3, SIZE));

        setLayout(new BorderLayout());
        add(_chatDisplay, BorderLayout.LINE_START);
        add(_participantsDisplay, BorderLayout.LINE_END);
        add(_textEditor, BorderLayout.PAGE_END);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(SIZE, SIZE));
    }

    public void addEventListener(ActionListener l) {
        _listeners.add(l);
        _textEditor.addActionListener(l);
    }

    public void print(String message) {
        _chatDisplay.append("\r\n" + message);
    }

    public void setClientName() {
        sendText(JOptionPane.showInputDialog("Type your name:"));
        setVisible(true);
    }

    private void sendText(String text) {
        for (ActionListener l : _listeners) {
            l.actionPerformed(new ActionEvent(this, 1, text));
        }
    }

    public void _setParticipantsNames(String[] participantsNames) {
        _participantsDisplay.setText("");
        for (String name : participantsNames) {
            _participantsDisplay.append("\r\n" + name);
        }
        repaint();
    }

    public void _setParticipantsNames(String participantsNames) {
        _participantsDisplay.setText(participantsNames);
        repaint();
    }

    public void removeParticipant(String nameToRemove) {
        var str = _participantsDisplay.getText();
        nameToRemove = "\r\n" + nameToRemove;
        var index1 = str.indexOf(nameToRemove);
        var first = str.substring(0, index1);
        var index2 = nameToRemove.length();
        var second = str.substring(index1, index2);

        var newNames = first + second;
        _setParticipantsNames(newNames);
    }

    public void addParticipant(String nameToAdd) {
        _setParticipantsNames(_participantsDisplay.getText() + "\r\n" + nameToAdd);
    }
}