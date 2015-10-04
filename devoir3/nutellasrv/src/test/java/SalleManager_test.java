import junit.framework.TestCase;

/**
 * Created by Faouziath on 2015-04-04.
 */
public class SalleManager_test extends TestCase{
    SalleManager salleManager;

    public void setUp() throws Exception {
       salleManager = new SalleManager();
    }

    public void tearDown() throws Exception {
        salleManager.ServerClose();
    }

    public void testGetAllProfile()throws Exception {
        System.out.println("La liste des utilisateur dans la bd:  *****************************");
        for ( Utilisateur u : salleManager.getAllProfile()){
            System.out.println("Pseudo: "+ u.getPseudo()+" ,Nom: "+ u.getNom()+" ,Likedmsg "+ u.getLikedmsg()+" ,Totalmessage "+ u.getTotalmsg());
        }
        System.out.println("Fin de la liste des utilisateur dans la bd:  *****************************");
    }

    public void testConnectUser() throws Exception{
        System.out.println("Test connection.");
        Utilisateur usr1 = salleManager.ConnnectUser("mst", "mst");
        Utilisateur usr2 = salleManager.ConnnectUser("titi", "toto");
        assertTrue("Connected user mst in db",usr1.getPseudo().equals("mst") );
        assertTrue("Connected user mst in db", usr1.getNom().equals("St-Pierre"));
        assertFalse("Not connect user titi not in db", usr2 != null);
    }

    public  void testCreateSalle()throws Exception{
        System.out.println("Test salle creation");
        assertFalse("Faillure to create salle: this pseudo is not in db", salleManager.CreateSalle("bobo", "nnn", "kkkkkk"));
        assertTrue("Success to create salle:", salleManager.CreateSalle("mst", "salle1", "la salle des idees"));
        assertTrue("Success to create salle:", salleManager.CreateSalle("ocrot", "salle2", "Les philosophes"));
        assertFalse("Faillure to create salle: this salle already exist", salleManager.CreateSalle("mst", "salle1", "la salle des idees"));
        assertFalse("Faillure to create salle: this pseudo is already subscribe", salleManager.CreateSalle("mst", "salle1", "la salle des idees"));
        assertFalse("Faillure to create salle: this pseudo is already subscribe", salleManager.CreateSalle("mst", "salle2", "Les philosophes"));

    }

    public  void testSubscribe()throws Exception{
        System.out.println("Test salle abonnement");
        assertTrue("Success to create salle:", salleManager.CreateSalle("ocrot", "salle1", "la salle des idees"));
        assertTrue("Success to create salle:", salleManager.CreateSalle("vbeau", "salle2", "Les philosophes"));
        assertTrue("Success to subscribe salle:", salleManager.Subscribe("mst", "salle1"));
        assertFalse("Faillure to subscribe salle: this pseudo is already subscribe", salleManager.Subscribe("mst", "salle1"));
    }

    public  void testUnSubscribe()throws Exception{
        System.out.println("Test salle Desabonnement");
        assertTrue("Success to create salle:", salleManager.CreateSalle("ocrot", "salle1", "la salle des idees"));
        assertTrue("Success to create salle:", salleManager.CreateSalle("vbeau", "salle2", "Les philosophes"));
        assertTrue("Success to subscribe salle:", salleManager.Subscribe("mst", "salle1"));
        assertTrue("Success to Unsubscribe salle:", salleManager.Unsubscribe("mst"));
        assertFalse("Faillure to Unsubscribe salle: salle1 do not exit", salleManager.Unsubscribe("mst"));
    }

    public  void testAddMessage()throws Exception{
        System.out.println("Test Add message");
        assertTrue("Success to create salle:", salleManager.CreateSalle("ocrot", "salle1", "la salle des idees"));
        assertTrue("Success to create salle:", salleManager.CreateSalle("vbeau", "salle2", "Les philosophes"));
        assertTrue("Success to subscribe salle:", salleManager.Subscribe("mst", "salle1"));
        assertFalse("Success to send message:", salleManager.addMessage("ocrot", "Coucou"));
        assertFalse("Success to send message:", salleManager.addMessage("ocrot", "quoi de neuf"));
        assertTrue("Success to subscribe salle:", salleManager.Subscribe("ocrot", "salle1"));
        assertTrue("Success to send message:", salleManager.addMessage("ocrot", "Coucou"));
        assertTrue("Success to send message:", salleManager.addMessage("mst", "salut"));

    }


}
