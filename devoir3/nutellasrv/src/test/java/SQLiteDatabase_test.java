import junit.framework.TestCase;
import java.sql.SQLException;

/**
 * Created by clement on 3/31/15.
 */

public class SQLiteDatabase_test extends TestCase{
    SQLiteDatabase db;

    public void setUp() throws SQLException{
        this.db = new SQLiteDatabase("db/test.db");
    }

    public void tearDown() throws SQLException{
        db.CloseDatabase();
    }

    public void testConnectUser() throws SQLException{
        System.out.println("Test connection.");
        assertTrue("Connected user mst in db", db.ConnectUser("mst", "mst"));
        assertFalse("Not connect user titi not in db", db.ConnectUser("titi", "toto"));
    }


    public void testProfileUserPseudo() throws  SQLException{
        System.out.println("Get user pseudo");
        Utilisateur user = new Utilisateur("Croteau", "Odette", "img/default.png", "ocrot");
        Utilisateur usr = db.GetProfile("ocrot");
        assertTrue("Get proper user from db with pseudo", user.equals(usr));
    }

    public void testProfileUserId() throws  SQLException{
        System.out.println("Get user ID");
        Utilisateur user = new Utilisateur();
        user.setNom("Croteau");
        user.setPrenom("Odette");
        user.setImg_path("img/default.png");
        user.setPseudo("ocrot");
        Utilisateur usr = db.GetProfile(1);
        assertTrue("Get proper user from db with id", user.equals(usr));
    }
}
