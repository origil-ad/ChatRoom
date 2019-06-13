import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientView extends JFrame {

    private final int SIZE = 600;
    private JTextField _textEditor;
    private JTextArea _chatDisplay;

    Scanner _reader = new Scanner(System.in);
    List<ActionListener> _listeners = new ArrayList<>();

    public ClientView() {

        _textEditor = new JTextField();
        _chatDisplay = new JTextArea();
        _chatDisplay.setEditable(false);


        setLayout(new BorderLayout());
        add(_chatDisplay, BorderLayout.PAGE_START);
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


    private void sendText(String text){
        for (ActionListener l : _listeners) {
            l.actionPerformed(new ActionEvent(this, 1, text));
        }
    }
}