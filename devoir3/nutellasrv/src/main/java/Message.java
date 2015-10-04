import java.util.Comparator;

/**
 * Created by clement on 3/26/15.
 */
public class Message  {
    private int id;
    private String text;
    private String pseudo;

    public Message(){}
    public Message( String p, String t)
    {
        setText(t);
        setPseudo(p);
    }

    public String getText()
    {
        return text;
    }

    public void setText(String t)
    {
        text = t;
    }

    public String getPseudo()
    {
        return pseudo;
    }

    public void setPseudo(String p)
    {
        pseudo = p;
    }


}

