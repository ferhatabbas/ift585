
import java.util.Set;

/**
 * Created by clement on 3/26/15.
 * Classe utilisateur reflet de la bdd
 */
public class Utilisateur {
    private String nom;
    private String prenom;
    private String img_path;
    private String pseudo;
    private int likedmsg;
    private int totalmsg;

    public Utilisateur(){}
    public Utilisateur(String nom, String prenom, String img_path, String pseudo){
        setNom(nom);
        setPrenom(prenom);
        setImg_path(img_path);
        setPseudo(pseudo);
        setTotalmsg(0);
        setLikedmsg(0);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }


    public int getLikedmsg() {
        return likedmsg;
    }

    public void setLikedmsg(int likedmsg) {
        this.likedmsg = likedmsg;
    }

    public int getTotalmsg() {
        return totalmsg;
    }

    public void setTotalmsg(int totalmsg) {
        this.totalmsg = totalmsg;
    }

    public boolean equals(Object obj){
        return equals((Utilisateur)obj);
    }

    public boolean equals(Utilisateur usr){
        return getNom().equals(usr.getNom()) &
                getPrenom().equals(usr.getPrenom()) &
                getPseudo().equals(usr.getPseudo());
    }

    public boolean isSubscribe(Set<Salle> salleList)
    {
        for ( Salle s : salleList){
            if (s.containUser(this.getPseudo()))
                return true;
        }
        return false;
    }
}
