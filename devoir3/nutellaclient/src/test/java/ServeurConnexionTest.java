import junit.framework.TestCase;

/**
 * Created by clement on 4/6/15.
 */
public class ServeurConnexionTest extends TestCase {
    public void testChangeArgs(){
        StringBuffer requestAimeMessage = new StringBuffer("{\"status\" : \"aimeMessage\"," +
                "\"message\":{\"user\":\"??\"}}");
        StringBuffer requestChSalle = new StringBuffer("{\"status\" : \"changerSalle\"," +
                "\"change\":{\"user\": \"??\", \"old_salle\":\"??\", \"new_salle\": \"??\"}}");

        String user = "clement";
        String old_salle = "s1";
        String new_salle = "s2";

        String res1 = new String("{\"status\" : \"aimeMessage\"," +
                "\"message\":{\"user\":\"clement\"}}");
        String res2 = new String("{\"status\" : \"changerSalle\"," +
                "\"change\":{\"user\": \"clement\", \"old_salle\":\"s1\", \"new_salle\": \"s2\"}}");

        assertTrue("Request1 must be filled exactly.",
                ServeurConnexion.changeArgs(requestAimeMessage, user).contentEquals(res1));
        assertTrue("Request2 must be filled exactly.",
                ServeurConnexion.changeArgs(requestChSalle, user, old_salle, new_salle).contentEquals(res2));
        assertTrue("Request3 must be filled exactly.",
                ServeurConnexion.changeArgs(requestAimeMessage, user, old_salle, new_salle).contentEquals(res1));
    }
}
