import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.Timer;


public class SalleDeDiscussion extends JFrame {
    private JTextField messageTF;
    private JComboBox selectSalle;
    private JTextArea descSalle;
    private JTextArea displayProfil;
    private JComboBox selectProfil;
    private JButton envoyerButton;
    private JButton btnAimer;
    private JButton btnCreeSalle;
    private JButton btnConnectSalle;
    private DefaultListModel model;
    private JList<String> listeMessages;

    private ServeurConnexion refCon;
    private Timer t;
    private HashMap<String, Salle> salles;
    private HashMap<String, Utilisateur> users;
    private ArrayList<String> messages;

    public String currentUser;
    public String currentSalle;


    public SalleDeDiscussion(ServeurConnexion con){
        refCon = con;
        InitVar();
        Initialize();
        runTimer();
    }

    public Utilisateur GetProfile(String key){
        return users.get(key);
    }

    private void InitVar(){
        salles = new HashMap<>();
        users = new HashMap<>();
        messages = new ArrayList<>();
        t = new Timer();
    }

    private void runTimer(){
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int idp =0;
                int ids =0;
                try {
                    idp = selectProfil.getSelectedIndex();
                    ids = selectSalle.getSelectedIndex();
                    for(Utilisateur p: refCon.requestAllProfile()){
                        users.put(p.getPseudo(), p);
                    }

                    for(Salle s: refCon.requestSalles()){
                        salles.put(s.getNom(), s);
                    }
                    if(currentSalle != null) {
                        messages.clear();
                        messages = refCon.requestMessages(currentSalle);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                selectProfil.removeAllItems();
                selectSalle.removeAllItems();
                if(currentSalle != null) {
                    model.clear();
                    for (String s : messages) {
                        model.addElement(s);
                    }
                }
                for(Utilisateur p : users.values()){selectProfil.addItem(p.getPseudo());}
                for(Salle s : salles.values()){selectSalle.addItem(s.getNom());}
                selectProfil.setSelectedIndex(idp);
                selectSalle.setSelectedIndex(ids);
            }
        }, 2*100,10*1000);
    }

    private void Initialize(){
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        setContentPane(panel1);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.2;

        c.gridx = c.gridy = 0;
        panel1.add(new JLabel("Nutella"), c);

        /**
         * Salle
         */
        selectSalle = new JComboBox();
        c.gridy = 1;
        panel1.add(selectSalle, c);

        descSalle = new JTextArea();
        c.gridy = 2;
        panel1.add(descSalle, c);

        btnConnectSalle = new JButton("connexion salle");
        c.gridy = 3;
        panel1.add(btnConnectSalle, c);

        btnCreeSalle = new JButton("creer salle");
        c.gridy = 4;
        panel1.add(btnCreeSalle, c);

        /**
         * Messages
         */
        model = new DefaultListModel();
        listeMessages = new JList<>(model);
        listeMessages.setSelectedIndex(0);
        listeMessages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeMessages.setLayoutOrientation(JList.VERTICAL);
        final JScrollPane spane = new JScrollPane();
        spane.setViewportView(listeMessages);
        c.gridx = 1;
        c.gridy = 2;
        panel1.add(spane, c);

        messageTF = new JTextField();
        c.gridy = 3;
        panel1.add(messageTF, c);

        envoyerButton = new JButton("Envoyer");
        c.gridy = 4;
        panel1.add(envoyerButton, c);

        btnAimer = new JButton("Aimer");
        c.gridx = 2;
        c.gridy = 4;
        panel1.add(btnAimer, c);


        /**
         * profil
         */
        selectProfil = new JComboBox();
        c.gridy = 1;
        panel1.add(selectProfil, c);

        displayProfil = new JTextArea();
        c.gridy = 2;
        panel1.add(displayProfil, c);

        EnableActionPerformed();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800,400));
        pack();
    }

    private void EnableActionPerformed(){
        final SalleDeDiscussion parent = this;
        envoyerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String msg = messageTF.getText();
                if(msg == null || msg == "" || currentSalle == null)
                    return;
                try {
                    messageTF.setText("");
                    refCon.requestSendMessage(currentUser, currentSalle, currentUser+": "+msg);
                    model.addElement(currentUser+": "+msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnConnectSalle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String salle = (String)selectSalle.getSelectedItem();
                if(salle == null)
                    return;
                currentSalle = salle;
                try {
                    refCon.requestChangeSalle(currentUser, "", salle);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnAimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String value = listeMessages.getSelectedValue();
                if(value == null)
                    return;
                try {
                    refCon.requestAimeMessage(value.substring(0, value.indexOf(":")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btnCreeSalle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame nsalle = new NouvelleSalle(refCon, parent);
                nsalle.setVisible(true);
                setVisible(false);
            }
        });

        selectProfil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String item = (String)selectProfil.getSelectedItem();
                Utilisateur p = users.get(item);
                if(p==null) return;

                displayProfil.setText("Pseudo: "+p.getPseudo()+"\n" +
                        "Nom: "+p.getNom()+"\n" +
                        "Prenom: "+p.getPrenom()+"\n" +
                        "Statistique: "+p.getLikedmsg()+"/"+p.getTotalmsg()+"\n");
            }
        });

        selectSalle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String item = (String)selectSalle.getSelectedItem();
                Salle s = salles.get(item);
                if(s==null) return;

                descSalle.setText("Nom: " + s.getNom()+"\n" +
                        "Description: " + s.getDescription());
            }
        });
    }
}
