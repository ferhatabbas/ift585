import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by clement on 4/5/15.
 * ServeurRunner cette classe s'occupe de répondre aux demande d'un client seulement
 */
public class ServeurRunner implements Runnable {

    private Socket socket;
    private JSONParser parser;
    private SalleManager mng;

    private StringBuffer senderProfils;

    /**
     * Constructeur du thread qui va répondre au client
     * pour ce faire il garde un référence sur le socket, la db
     * et possiblement sur le salle manager pour permettre la gestion
     * des salles pour les utilisateurs.
     *
     * @param skt
     * @param smanager
     */
    public ServeurRunner(Socket skt, SalleManager smanager) {
        socket = skt;
        mng = smanager;
        parser = new JSONParser();
    }


    public void run() {
        try {
            dispatchClientRequest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper fonction qui permet de convertir les données reçu du socket
     * en String pour que ce soit manger par le parser JSON.
     *
     * @param data
     * @return
     */
    private String convertData(char[] data) {
        String s = new StringBuilder().append(data).toString();

        if (s.charAt(0) != '{')
            s = "{" + s;
        s = s.substring(0, s.lastIndexOf("}") + 1);
        return s;
    }

    /**
     * Méthode principale de communication avec le client,
     * elle permet de répondre à toutes les requêtes d'un client
     * spécifique.
     * TODO: Enlever l'attente active.
     *
     * @throws InterruptedException
     * @throws ParseException
     * @throws SQLException
     */
    private void dispatchClientRequest() throws InterruptedException, ParseException, SQLException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {
                try {
                    //Attente de donnés du client
                    while (!in.ready()) {
                        Thread.sleep(100, 10);
                    }

                    //Lecture des données du client
                    char[] repdata = new char[65535];
                    in.read(repdata);
                    String tmp = convertData(repdata);
                    JSONObject jsobj = (JSONObject) parser.parse(tmp);

                    if (!jsobj.containsKey("status"))
                        continue;

                    //vérification du type de demande
                    if (((String) jsobj.get("status")).contentEquals("connexion")) {
                        if (!jsobj.containsKey("username") || !jsobj.containsKey("password"))
                            throw new RequestException("Ca chie");
                        String user = (String) jsobj.get("username");
                        String pass = (String) jsobj.get("password");

                        if (mng.ConnnectUser(user, pass) == null)
                            throw new RequestException("Ca chie");

                        out.write("{\"status\":\"connectionSuccess\"}");

                    } else if (((String) jsobj.get("status")).contentEquals("profilesListe")) {
                        // récupération de la liste des profils
                        StringBuffer userContent = new StringBuffer("{\"nom\" : \"??\"," +
                                "\"prenom\" : \"??\"," +
                                "\"pseudo\" : \"??\"," +
                                "\"msg_like\" : \"??\"," +
                                "\"msg_send\" : \"??\"},??");
                        StringBuffer responseTemplate = new StringBuffer("{\"status\" : \"profileSend\"," +
                                "\"profils\" : [??]}");

                        Set<Utilisateur> users = mng.getAllProfile();
                        for (Utilisateur user : users) {
                            String response = changeArgs(userContent,
                                    user.getNom(),
                                    user.getPrenom(),
                                    user.getPseudo(),
                                    Integer.toString(user.getLikedmsg()),
                                    Integer.toString(user.getTotalmsg()));
                            responseTemplate = changeArgsBuf(responseTemplate, response);
                        }
                        responseTemplate = changeArgsBuf(responseTemplate, "");
                        out.write(responseTemplate.toString());

                    } else if (((String) jsobj.get("status")).contentEquals("messageListe")) {
                        // récupération de la liste des messages pour une salle
                        String roomname = (String) jsobj.get("salle");

                        StringBuffer messagesSalle = new StringBuffer("{\"pseudo\" : \"??\"," +
                                "\"data\" : \"??\"},??");
                        StringBuffer responseTemplate = new StringBuffer("{\"status\" : \"messagesSend\"," +
                                "\"message\" : [??]}");


                        for (Message m : mng.findSalle(roomname).getMessageList()) {
                            String response = changeArgs(messagesSalle,
                                    m.getPseudo(),
                                    m.getText());
                            responseTemplate = changeArgsBuf(responseTemplate, response);
                        }
                        responseTemplate = changeArgsBuf(responseTemplate, "");
                        out.write(responseTemplate.toString());

                    } else if (((String) jsobj.get("status")).contentEquals("sendMessage")) {
                        String username = (String)jsobj.get("username");
                        String msg = (String)jsobj.get("message");
                        mng.addMessage(username, msg);
                    } else if (((String) jsobj.get("status")).contentEquals("salleListe")) {
                        /**
                         * Envoie de la liste des salles au serveur
                         */
                        StringBuffer salleListe = new StringBuffer("{\"nom\": \"??\"" +
                                "\"description\" : \"??\"},??");
                        StringBuffer responseTemplate = new StringBuffer("{\"status\" : \"salleSend\"," +
                                "\"salle\" : [??]}");

                        Set<Salle> rooms = mng.getSalleList();
                        int rest = rooms.size();

                        for (Salle room : rooms) {
                            String response = changeArgs(salleListe,
                                    room.getNom(),
                                    room.getDescription());
                            responseTemplate = changeArgsBuf(responseTemplate, response);
                        }
                        responseTemplate = changeArgsBuf(responseTemplate, "");
                        out.write(responseTemplate.toString());

                    } else if (((String) jsobj.get("status")).contentEquals("aimeMessage")) {
                        String username = (String) jsobj.get("user");
                        mng.likeMsg(username);
                    } else if (((String) jsobj.get("status")).contentEquals("changeSalle")) {
                        JSONObject change = (JSONObject)jsobj.get("change");
                        String username = (String) change.get("user");
                        String roomname = (String) change.get("new_salle");
                        mng.Unsubscribe(username);
                        mng.Subscribe(username, roomname);

                    } else if (((String) jsobj.get("status")).contentEquals("creerSalle")) {
                        JSONObject creer = (JSONObject) jsobj.get("creer");
                        String username = (String) creer.get("user");
                        String roomname = (String) creer.get("nom");
                        String description = (String) creer.get("description");
                        mng.CreateSalle(username, roomname, description);

                    } else {
                        throw new RequestException("ca chie");
                    }
                } catch (RequestException e) {
                    //Problème dans la requête
                    out.write("{\"status\":\"errorRequest\"}");
                }
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Socket closed");
        }
    }

    /**
     * Méthode qui va faire la liste des profils contenu dans la DB.
     *
     * @return JSONObjet qui va contenir les profils des utilisateurs
     */
    private JSONObject GetAllProfile() {
        JSONObject res = new JSONObject();
        return res;
    }

    /**
     * Méthode qui permet de remplir les requêtes du serveur graçe à une ellipse
     *
     * @param strbuf
     * @param args
     * @return
     */
    public static String changeArgs(StringBuffer strbuf, String... args) {
        StringBuffer res = new StringBuffer(strbuf);
        for (String str : args) {
            int offset = res.indexOf("??");
            if (offset == -1)
                break;
            res.replace(offset, offset + 2, str);
        }
        return res.toString();
    }

    /**
     * Méthode qui permet de remplir les requêtes du serveur graçe à une ellipse
     *
     * @param strbuf
     * @param args
     * @return
     */
    public static StringBuffer changeArgsBuf(StringBuffer strbuf, String... args) {
        StringBuffer res = new StringBuffer(strbuf);
        for (String str : args) {
            int offset = res.indexOf("??");
            if (offset == -1)
                break;
            res.replace(offset, offset + 2, str);
        }
        return res;
    }
}
