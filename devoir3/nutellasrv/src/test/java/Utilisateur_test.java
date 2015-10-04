import junit.framework.TestCase;

/**
 * Created by clement on 3/31/15.
 */
public class Utilisateur_test extends TestCase{


    public void testUser(){
        Utilisateur user = new Utilisateur("toto", "titi", "test", "tiloup");
        Utilisateur user1 = new Utilisateur("user1", "titi", "test", "tiloup");
        Utilisateur user2 = new Utilisateur("toto", "user1", "test", "tiloup");
        Utilisateur user3 = new Utilisateur("toto", "titi", "user1", "tiloup");
        Utilisateur user4 = new Utilisateur("toto", "titi", "test", "user1");
        assertFalse("User1 not equals", user.equals(user1));
        assertFalse("User2 not equals", user.equals(user2));
        assertTrue("User3 equals", user.equals(user3));
        assertFalse("User4 not equals", user.equals(user4));
    }
}
