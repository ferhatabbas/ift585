import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by clement on 3/26/15.
 * Classe principale qui écoute sur le port 11111 pour la communication client/serveur.
 */
public class ServeurApp {
    private ServerSocket srv;
    private SalleManager mgr;

    /**
     * Fonction main elle démarre le serveur et attend les requêtes
     * elle trap aussi toutes les exceptions possibles.
     * @param args
     */
    static public void main(String[] args) {
        System.out.println("Start server.");
        try {
            new ServeurApp().serveurReceive();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Constructeur du Serveur il permet de se mettre en écoute sur le port 11111
     * et de demarrer la BD.
     * @throws IOException
     * @throws SQLException
     */
    public ServeurApp()throws IOException, SQLException{
        srv = new ServerSocket(11111);
        mgr = new SalleManager();
    }

    /**
     * Cette méthode permet de recevoir toutes les demandes faites
     * au serveur d'ouvrir un socket avec le client et de lancer
     * un thread qui va prendre en charge toutes les demandes du client.
     * @throws IOException
     */
    public void serveurReceive() throws IOException{
        while(true){
            Socket skt = srv.accept();
            new Thread(new ServeurRunner(skt, mgr)).start();
        }
    }
}
