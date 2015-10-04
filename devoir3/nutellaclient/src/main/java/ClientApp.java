import java.awt.*;

/**
 * Created by clement on 3/28/15.
 */
public class ClientApp {
    private static Connexion frame;
    public static void main(String[] args) {
        System.out.println("Client app start.");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new Connexion();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
