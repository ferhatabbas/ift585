import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class Inscription extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldNomDutilisateur;
	private JTextField textFieldPrnomDutilisateur;
	private JTextField textFieldPseudonyme;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField textFieldAvatar;

	/**
	 * Create the frame.
	 */
	public Inscription() {
		setTitle("Inscription     -Nutella -");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 517, 516);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNomDutilisateur = new JLabel("Nom d'utilisateur :");
		lblNomDutilisateur.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblPrnomDutilisateur = new JLabel("Pr\u00E9nom d'utilisateur :");
		lblPrnomDutilisateur.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblAvatar = new JLabel("Avatar :");
		lblAvatar.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblInscription = new JLabel("Inscription  ( Nutella est gratuit et ouvert \u00E0 tous !)");
		lblInscription.setForeground(new Color(160, 82, 45));
		lblInscription.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		JLabel lblCestGratuitEt = new JLabel("Qui \u00EAtes vous ?");
		lblCestGratuitEt.setForeground(Color.BLUE);
		lblCestGratuitEt.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblMotDePasse = new JLabel("Cr\u00E9er un mot de passe :");
		lblMotDePasse.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblRetaperLeMot = new JLabel("Retaper le Mot de passe :");
		lblRetaperLeMot.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JButton btnInscription = new JButton("Inscription");
		btnInscription.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//enregistrer un nouveau utilisateur dans la base de donn�es
				
				//donne lui accees a se connecter 
				Connexion fenConnexion= null;
				try {
					fenConnexion = new Connexion();
					fenConnexion.setVisible(true);
					setVisible (false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnInscription.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JButton buttonEffacer = new JButton("R\u00E9initialiser");
		buttonEffacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Effacer les champs de textfield
				textFieldNomDutilisateur.setText("");
				textFieldPrnomDutilisateur.setText("");
				textFieldPseudonyme.setText("");
				passwordField.setText("");
				passwordField_1.setText("");
			}
		});
		buttonEffacer.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		textFieldNomDutilisateur = new JTextField();
		textFieldNomDutilisateur.setColumns(10);
		
		textFieldPrnomDutilisateur = new JTextField();
		textFieldPrnomDutilisateur.setColumns(10);
		
		textFieldPseudonyme = new JTextField();
		textFieldPseudonyme.setColumns(10);
		
		passwordField = new JPasswordField();
		
		passwordField_1 = new JPasswordField();
		
		JLabel label_1 = new JLabel("Nutella ");
		label_1.setForeground(new Color(160, 82, 45));
		label_1.setFont(new Font("Viner Hand ITC", Font.BOLD, 20));
		
		JLabel label_2 = new JLabel("Pseudonyme :");
		label_2.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		textFieldAvatar = new JTextField();
		textFieldAvatar.setText("Provisoir");
		textFieldAvatar.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(174)
							.addComponent(buttonEffacer, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
							.addComponent(btnInscription))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(34)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblRetaperLeMot)
								.addComponent(lblMotDePasse, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
								.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblAvatar, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPrnomDutilisateur, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNomDutilisateur))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(passwordField, Alignment.LEADING)
									.addComponent(passwordField_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textFieldPseudonyme, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textFieldAvatar, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textFieldPrnomDutilisateur, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)))
								.addComponent(textFieldNomDutilisateur, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)))
						.addComponent(lblCestGratuitEt, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(57, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblInscription, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(89, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblInscription, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(14)
							.addComponent(lblCestGratuitEt, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblNomDutilisateur))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldNomDutilisateur, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(29)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPrnomDutilisateur, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldPrnomDutilisateur, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblAvatar, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldAvatar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldPseudonyme, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMotDePasse, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblRetaperLeMot, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonEffacer, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnInscription))
					.addGap(63))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
