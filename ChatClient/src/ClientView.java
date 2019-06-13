import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientView extends JPanel {

    Scanner _reader = new Scanner(System.in);
    List<ActionListener> _listeners = new ArrayList<>();

    public ClientView() {
        new Reader().start();
    }

    public void addEventListener(ActionListener l) {
        _listeners.add(l);
    }

    public void print(String message) {
        System.out.println(message);
    }

    class Reader extends Thread {
        @Override
        public void run() {
            while (true) {
                var text = _reader.nextLine();
                for (ActionListener l : _listeners) {
                    l.actionPerformed(new ActionEvent(this, 1, text));
                }
            }
        }
    }
}