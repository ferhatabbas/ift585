import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class Connexion extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldPseudonyme;
	private JPasswordField passwordFieldMotPasse;
	private ServeurConnexion srvCon;

	public static String username;

	/**
	 * Create the frame.
	 */
	public Connexion() throws IOException {
		setTitle("Nutella Connexion   -Nutella -");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 507, 246);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnNewButton = new JButton("Connexion");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					srvCon = new ServeurConnexion();
					String username = textFieldPseudonyme.getText();
					String password = new String(passwordFieldMotPasse.getPassword());
					if(!srvCon.requestConnexion(username, password))
						return;
                    //bouton de connexion
                    SalleDeDiscussion fenSalleDeDiscussion= new SalleDeDiscussion(srvCon);
					fenSalleDeDiscussion.currentUser = username;
					fenSalleDeDiscussion.setVisible(true);
					setVisible (false);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblPseudonyme = new JLabel("Pseudonyme :");
		lblPseudonyme.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		textFieldPseudonyme = new JTextField();
		textFieldPseudonyme.setColumns(10);
		
		JLabel lblMotPasse = new JLabel("Mot de passe :");
		lblMotPasse.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		passwordFieldMotPasse = new JPasswordField();
		
                JButton btnCreerCompte = new JButton("Cr\u00E9er un compte");
//		btnCreerCompte.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Inscription fen = new Inscription();
//
//				fen.setVisible(true);
//				setVisible(false);
//			}
//
//
//		});
		btnCreerCompte.setFont(new Font("Times New Roman", Font.BOLD, 14));
                btnCreerCompte.setVisible(false);

		JButton btnReinitialiser = new JButton("R\u00E9initialiser");
		btnReinitialiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldPseudonyme.setText("");
				passwordFieldMotPasse.setText("");
			}
		});
		btnReinitialiser.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblNewLabel = new JLabel("Nutella ");
		lblNewLabel.setForeground(new Color(160, 82, 45));
		lblNewLabel.setFont(new Font("Viner Hand ITC", Font.BOLD, 20));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(94)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblPseudonyme)
								.addComponent(lblMotPasse))
							.addGap(20)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(textFieldPseudonyme, GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
								.addComponent(passwordFieldMotPasse, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
                                                          .addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
                                                          .addContainerGap()
                                                          .addComponent(btnCreerCompte)
                                                          .addPreferredGap(ComponentPlacement.RELATED)
                                                          .addComponent(btnReinitialiser)
                                                          .addPreferredGap(ComponentPlacement.RELATED)
                                                          .addComponent(btnNewButton)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(26)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPseudonyme)
						.addComponent(textFieldPseudonyme, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblMotPasse)
						.addComponent(passwordFieldMotPasse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
                                                  .addComponent(btnReinitialiser)
                                                  .addComponent(btnCreerCompte))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
