import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by clement on 4/5/15.
 * classe qui permet la communication avec le serveur en json
 */
public class ServeurConnexion {
    private Socket skt;
    BufferedReader reader;
    BufferedWriter writer;
    JSONParser parser;

    StringBuffer requestCon;
    StringBuffer requestProfiles;
    StringBuffer requestMsgListe;
    StringBuffer requestSendMsg;
    StringBuffer requestSalleListe;
    StringBuffer requestAimeMessage;
    StringBuffer requestChSalle;
    StringBuffer requestCreerSalle;

    public ServeurConnexion() throws IOException {
        skt = new Socket("127.0.0.1", 11111);
        parser = new JSONParser();
        reader = new BufferedReader(new InputStreamReader(skt.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));
        requestCon = new StringBuffer("{\"status\" : \"connexion\", " +
                "\"username\":\"??\", " +
                "\"password\":\"??\"}");
        requestProfiles = new StringBuffer("{\"status\" : \"profilesListe\"," +
                "}");
        requestMsgListe = new StringBuffer("{\"status\" : \"messageListe\"," +
                "\"salle\": \"??\"}");
        requestSendMsg = new StringBuffer("{\"status\" : \"sendMessage\"," +
                "\"username\": \"??\"," +
                "\"message\": \"??\"}");
        requestSalleListe = new StringBuffer("{\"status\" : \"salleListe\"}");
        requestAimeMessage = new StringBuffer("{\"status\" : \"aimeMessage\"," +
                "\"user\":\"??\"}");
        requestChSalle = new StringBuffer("{\"status\" : \"changeSalle\"," +
                "\"change\":{\"user\": \"??\", \"old_salle\":\"??\", \"new_salle\": \"??\"}}");
        requestCreerSalle = new StringBuffer("{\"status\" : \"creerSalle\"," +
                "\"creer\":{\"user\":\"??\", \"nom\":\"??\", \"description\":\"??\"}}");
    }

    /**
     * Convertie les données reçu du serveur pour les donner au parser json
     *
     * @param data
     * @return
     */
    private String convertData(char[] data, boolean first) {
        String s = new StringBuilder().append(data).toString();

        if (s.indexOf(0) != -1)
            return s.substring(0, s.indexOf(0));
        return s;
    }

    /**
     * Ferme la connexion avec le serveur
     *
     * @throws IOException
     */
    public void doneConnexion() throws IOException {
        skt.close();
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
     * Fait une demande de connexion au serveur
     *
     * @param user
     * @param pass
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public boolean requestConnexion(String user, String pass) throws IOException, ParseException, InterruptedException {
        sendRequest(changeArgs(requestCon, user, pass));
        JSONObject jsobj = receiveRequest();
        return validate(jsobj);
    }

    /**
     * Retourne la liste des utilisateurs du réseau
     *
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<Utilisateur> requestAllProfile() throws IOException, ParseException, InterruptedException {
        sendRequest(requestProfiles.toString());
        int count = 100;

        ArrayList res = new ArrayList<Utilisateur>();
        JSONObject jsobj = receiveRequest();
        if (jsobj.containsKey("status") && ((String) jsobj.get("status")).contentEquals("profileSend")) {
            JSONArray profils = (JSONArray) jsobj.get("profils");

            // remplir le profil quand ferhat aura push ces modifs
            for (int i = 0; i < profils.size(); ++i) {
                String nom = (String) ((JSONObject) profils.get(i)).get("nom");
                String prenom = (String) ((JSONObject) profils.get(i)).get("prenom");
                String pseudo = (String) ((JSONObject) profils.get(i)).get("pseudo");
                String msg_like = (String) ((JSONObject) profils.get(i)).get("msg_like");
                String msg_send = (String) ((JSONObject) profils.get(i)).get("msg_send");
                res.add(new Utilisateur(nom, prenom, "", pseudo, Integer.parseInt(msg_like), Integer.parseInt(msg_send)));
            }
        }
        return res;
    }

    /**
     * Retourne la lise des messages que possède le serveur pour une salle
     *
     * @param salle
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<String> requestMessages(String salle) throws IOException, ParseException, InterruptedException {
        sendRequest(changeArgs(requestMsgListe, salle));

        int count = 100;
        ArrayList<String> res = new ArrayList<>();
        JSONObject jsobj = receiveRequest();
        if (jsobj.containsKey("status") && ((String) jsobj.get("status")).contentEquals("messagesSend")) {
            JSONArray messages = (JSONArray) jsobj.get("message");
            for(int i=0; i<messages.size(); ++i) {
                res.add((String)((JSONObject)messages.get(i)).get("data"));
            }
        }
        return res;
    }

    /**
     * Requete d'envoie de message au serveur
     * @param user
     * @param salle
     * @param msg
     */
    public void requestSendMessage(String user, String salle, String msg) throws IOException {
        sendRequest(changeArgs(requestSendMsg, user, msg));
    }

    /**
     * Retourne la liste des salle que possède le serveur
     *
     * @return ArrayList<Salle>
     * @throws IOException
     * @throws ParseException
     */
    public ArrayList<Salle> requestSalles() throws IOException, ParseException, InterruptedException {
        sendRequest(requestSalleListe.toString());

        int count = 100;
        ArrayList res = new ArrayList<Salle>();
        JSONObject jsobj = receiveRequest();
        if (jsobj.containsKey("status") && ((String) jsobj.get("status")).contentEquals("salleSend")) {
            JSONArray salles = (JSONArray) jsobj.get("salle");
            for(int i = 0; i<salles.size(); ++i) {
                res.add(new Salle(
                        (String) ((JSONObject) salles.get(i)).get("nom"),
                        (String) ((JSONObject) salles.get(i)).get("description")
                ));
            }
        }
        return res;
    }

    /**
     * Permet d'aimer le message d'un utilisateur
     *
     * @param user
     * @throws IOException
     * @throws ParseException
     */
    public void requestAimeMessage(String user) throws IOException, ParseException {
        sendRequest(changeArgs(requestAimeMessage, user));
    }

    /**
     * Permet de changer de salle du côté du serveur.
     *
     * @param user
     * @param olds
     * @param news
     * @throws IOException
     * @throws ParseException
     */
    public void requestChangeSalle(String user, String olds, String news) throws IOException, ParseException {
        sendRequest(changeArgs(requestChSalle, user, olds, news));
    }

    /**
     * Permet de créer une salle sur le serveur.
     *
     * @param user
     * @param news
     * @throws IOException
     * @throws ParseException
     */
    public void requestCreerSalle(String user, Salle news) throws IOException, ParseException {
        sendRequest(changeArgs(requestCreerSalle, user, news.getNom(), news.getDescription()));
    }

    /**
     * Méthode qui permet d'envoyer une requête au serveur au format JSON
     *
     * @param req
     * @throws IOException
     */
    private void sendRequest(String req) throws IOException {
        System.out.println(req);
        writer.write(req);
        writer.flush();
    }

    /**
     * Permet de recevoir une réponse du serveur et retourne l'objet JSON associé
     *
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private JSONObject receiveRequest() throws IOException, InterruptedException {
        StringBuffer data = new StringBuffer();
        int timeout = 1000;
        boolean first = true;
        JSONObject jsobj;
        while (true) {
            while (!reader.ready()) {
                Thread.sleep(100, 10);
                if(--timeout == 0)
                    return null;
            }
            char[] d = new char[65536];
            reader.read(d);
            String conv = convertData(d, first);
            data.append(conv);
            String req = data.toString();
            first = false;
            try {
                jsobj = (JSONObject) parser.parse(req);
                break;
            } catch (ParseException e) {
            }
        }
        return jsobj;
    }


    /**
     * Valide si la réponse du serveur est un succes ou pas.
     *
     * @param jsobj
     * @return
     */
    private boolean validate(JSONObject jsobj) {
        return jsobj.containsKey("status") && ((String) jsobj.get("status")).contains("Success");
    }
}
