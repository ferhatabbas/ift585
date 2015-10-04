import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by clement on 3/31/15.
 * Classe d'interface pour la base de donnée
 */
public class SQLiteDatabase {
    private Connection connection;
    private PreparedStatement select_user_pseudo;
    private PreparedStatement select_user_id;
    private PreparedStatement connect_user;
    private PreparedStatement get_user;
    private PreparedStatement get_user_total;
    private PreparedStatement set_user_total;
    private PreparedStatement get_user_like;
    private PreparedStatement set_user_like;
    public SQLiteDatabase(String dbfname) throws SQLException{
        StringBuilder con = new StringBuilder("jdbc:sqlite:");
        connection = DriverManager.getConnection(con.append(dbfname).toString());

        /*Preparation des statement pour les requetes*/
        select_user_pseudo = connection.prepareStatement(
                "select * from nutella_user where pseudo like ?");
        select_user_id = connection.prepareStatement(
                "select * from nutella_user where id = ?");
        connect_user = connection.prepareStatement(
                "select * from nutella_user where pseudo LIKE ? and password LIKE ?");
        get_user = connection.prepareStatement(
                "select * from nutella_user");

        get_user_total = connection.prepareStatement(
                "select totalmsg from nutella_user where pseudo LIKE ?");

        get_user_like = connection.prepareStatement(
                "select likedmsg from nutella_user where pseudo LIKE ?");

        set_user_total = connection.prepareStatement(
                "UPDATE nutella_user SET totalmsg = ? where pseudo LIKE ?");
        set_user_like = connection.prepareStatement(
                "UPDATE nutella_user SET likedmsg = ? where pseudo LIKE ?");
    }

    public boolean ConnectUser(String pseudo, String passwd) throws SQLException{
        connect_user.setString(1, pseudo);
        connect_user.setString(2, passwd);
        ResultSet rset = connect_user.executeQuery();
        if(rset.next()) {
            return rset.getRow() > 0;
        }
        return false;
    }

    /*incrementer le champs  likedMsg dans utilisateur*/
    public  void UpdateUserLikedMsg (String pseudo) throws SQLException {
        get_user_like.setString(1, pseudo);
        ResultSet rset = get_user_like.executeQuery();
        if(rset.next()){
            int ccount = rset.getInt(1);
            ccount++;
            set_user_like.setInt(1, ccount);
            set_user_like.setString(2, pseudo);
            set_user_like.executeUpdate();
        }
    }

    /*incrementer le champs nombre de message envoye dans utilisateur*/
    public  void UpdateUserMsg(String pseudo) throws SQLException {
        get_user_total.setString(1, pseudo);
        ResultSet rset = get_user_total.executeQuery();
        if(rset.next()){
            int ccount = rset.getInt(1);
            ccount++;
            set_user_total.setInt(1, ccount);
            set_user_total.setString(2, pseudo);
            set_user_total.executeUpdate();
        }
    }

    public Set<Utilisateur> getAllProfile()throws SQLException{
        Set<Utilisateur> userList= new HashSet<>();
        ResultSet rset = get_user.executeQuery();
        while(rset.next()){
            Utilisateur user = new Utilisateur();
            user.setNom(rset.getString("nom"));
            user.setPrenom(rset.getString("prenom"));
            user.setImg_path(rset.getString("img_path"));
            user.setPseudo(rset.getString("pseudo"));
            user.setLikedmsg(rset.getInt("likedmsg"));
            user.setTotalmsg(rset.getInt("totalmsg"));
            userList.add(user);
        }
        return userList;
    }

    private Utilisateur GenProfile(ResultSet rset) throws SQLException{

        if(rset.next()){
            Utilisateur user = new Utilisateur();
            user.setNom(rset.getString("nom"));
            user.setPrenom(rset.getString("prenom"));
            user.setImg_path(rset.getString("img_path"));
            user.setPseudo(rset.getString("pseudo"));
            user.setLikedmsg(rset.getInt("likedmsg"));
            user.setTotalmsg(rset.getInt("totalmsg"));
            return user;
        }
        return null;
    }
    public Utilisateur GetProfile(String pseudo) throws SQLException{
        select_user_pseudo.setString(1, pseudo);
        return GenProfile(select_user_pseudo.executeQuery());
    }

    public Utilisateur GetProfile(int id) throws SQLException{
        select_user_id.setInt(1, id);
        return GenProfile(select_user_id.executeQuery());
    }

    public void CloseDatabase() throws SQLException{
        select_user_id.close();
        select_user_pseudo.close();
        connect_user.close();
        connection.close();
    }
}
