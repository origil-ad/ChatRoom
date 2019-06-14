public class Client {

    public static void main(String[] args) {
        ClientView view = new ClientView();
        new ClientController(view);
    }
}