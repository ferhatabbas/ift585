/**
 * Created by clement on 4/5/15.
 */

public class Utilisateur {
    //TODO:"A implementer"
    private String nom;
    private String prenom;
    private String img_path;
    private String pseudo;
    private int likedmsg;
    private int totalmsg;

    public Utilisateur(){}
    public Utilisateur(String nom, String prenom, String img_path, String pseudo, int like, int total){
        setNom(nom);
        setPrenom(prenom);
        setImg_path(img_path);
        setPseudo(pseudo);
        setTotalmsg(total);
        setLikedmsg(like);
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

    public boolean equals(Utilisateur usr){
        return getNom().equals(usr.getNom()) &
                getPrenom().equals(usr.getPrenom()) &
                getPseudo().equals(usr.getPseudo());
    }

    public boolean equals(Object obj){
        return equals((Utilisateur)obj);
    }

    public int hashCode(){
        return getPseudo().hashCode();
    }

}
