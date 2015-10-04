import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by clement on 4/5/15.
 */
public class SalleManager {
    /**
    TODO: implenter la gestion des salles,
    On va passer cette classe aux thread ServeurRunner pour que le client puisse
    changer de salle, cree une salle ou autre.... tout doit être réentrant donc
    synchronize dans chaque méthode.
     */

    private SQLiteDatabase db;
    private Set<Salle> salleList ;

    public SalleManager()throws SQLException {
        this.db = new SQLiteDatabase("db/test.db");
        this.salleList = new HashSet<>();
    }

    public synchronized Set<Salle> getSalleList(){
        return salleList;
    }

    public synchronized Set<Utilisateur> getAllProfile()throws SQLException {
        return db.getAllProfile();
    }

    public void ServerClose()throws SQLException {
        db.CloseDatabase();
    }

    public synchronized Utilisateur ConnnectUser(String pseudo, String passwd)throws SQLException {
        if( db.ConnectUser(pseudo, passwd)){
            return db.GetProfile(pseudo);
        }
        else return null;
    }

    public synchronized boolean CreateSalle(String pseudo,String nom,String description) throws SQLException  {
        Utilisateur usr= db.GetProfile(pseudo);
        if(usr != null){
            Salle salle = new Salle(nom, description);
            //salle.addUser(pseudo);
            salleList.add(salle);
            return  true;
        }
        else return  false;
    }

    public synchronized boolean Subscribe(String pseudo,String nomSalle) throws SQLException {
        Utilisateur usr= db.GetProfile(pseudo);
        if(!usr.isSubscribe(this.salleList)){
            return addUserInSalle(pseudo, nomSalle);
        }
        else return  false;
    }

    public synchronized boolean Unsubscribe(String pseudo) {
        return RemoveFromSalle(pseudo);
    }


    public synchronized boolean addMessage(String pseudo, String textMsg) throws  SQLException{
        Salle salle = findUserInSalle(pseudo);
        if(salle==null)
            return false;
        db.UpdateUserMsg(pseudo);
        salle.addMessage(new Message(pseudo, textMsg));
        return true;
    }

    public synchronized void likeMsg(String msgAuthorPseudo) throws  SQLException {
        db.UpdateUserLikedMsg(msgAuthorPseudo);
    }

    public Salle findSalle(String nom){
        Salle s;
        for(Iterator<Salle> it = salleList.iterator(); it.hasNext();){
            s = it.next();
            if(s.getNom().contentEquals(nom))
                return s;
        }
        return null;
    }

    private Salle findUserInSalle(String pseudo){
        Salle s;
        for(Iterator<Salle> it = salleList.iterator(); it.hasNext();){
            s = it.next();
            if(s.containUser(pseudo))
                return s;
        }
        return null;
    }

    private boolean addUserInSalle(String pseudo,String nomSalle) throws SQLException {
        Salle s = findSalle(nomSalle);
        if(s == null)
            return false;
        s.addUser(pseudo);
        return true;
    }

    private boolean RemoveFromSalle(String pseudo) {
        Salle salle = findUserInSalle(pseudo);
        if(salle == null)
            return false;
        salle.removeUser(pseudo);
        return true;
    }

}
