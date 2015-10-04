import java.util.*;

/**
 * Created by clement on 3/26/15.
 */
public class Salle {
//    private int id;
    private String nom;
    private String description;
    private Set<String> userList; //pseudo list
    private ArrayList<Message> messageList;
    private static int idSequence = 0;

    public Salle(){}

    public Salle(String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.userList = new HashSet<>();
        this.messageList = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void addUser(String pseudo){
        this.userList.add(pseudo);
    }

    public void removeUser(String pseudo){
        this.userList.remove(pseudo);
    }

    public Set<String> getUserList(){
        return this.userList;
    }

    public ArrayList<Message> getMessageList(){
        return this.messageList;
    }

    public void addMessage(Message msg){
        this.messageList.add(msg);
    }

    public boolean containUser(String pseudo){
       return (this.userList.contains(pseudo));
    }


    public int hashCode(){
        return getNom().hashCode();
    }
    public boolean equals(Object obj){
        return getNom().contentEquals(((Salle)obj).getNom());
    }

}