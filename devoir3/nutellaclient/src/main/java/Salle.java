import java.util.HashSet;
import java.util.Set;

/**
 * Created by clement on 4/5/15.
 */
public class Salle {
    //TODO:Implanter la salle
    private String nom;
    private String description;

    private static int idSequence = 0;

    public Salle(){}

    public Salle(String nom, String description) {

        this.nom = nom;
        this.description = description;
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

    public boolean isYourName(String checkName){
        return checkName.equalsIgnoreCase(this.nom);
    }
}
