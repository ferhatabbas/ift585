import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class NouvelleSalle extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldNomSalle;
	private JButton btnCreerSalle;
	private JButton buttonEffacerChamps;
	private JTextArea textAreaDescriptionSalle;
	private ServeurConnexion refCon;

	/**
	 * Create the frame.
	 * @param srvCon
	 * @param parent
	 */
	public NouvelleSalle(ServeurConnexion srvCon, final SalleDeDiscussion parent) {
		refCon = srvCon;
		setForeground(new Color(230, 230, 250));
		setTitle("Nouvelle salle   -Nutella -");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNomSalle = new JLabel("Nom de la salle :");
		lblNomSalle.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel label_1 = new JLabel("Nutella ");
		label_1.setForeground(new Color(160, 82, 45));
		label_1.setFont(new Font("Viner Hand ITC", Font.BOLD, 20));
		
		textFieldNomSalle = new JTextField();
		textFieldNomSalle.setForeground(new Color(0, 0, 128));
		textFieldNomSalle.setBackground(new Color(255, 255, 224));
		textFieldNomSalle.setColumns(10);
		
		JLabel lblDescriptionSalle = new JLabel("Description de la salle :");
		lblDescriptionSalle.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		btnCreerSalle = new JButton("Cr\u00E9er votre salle  ");
		btnCreerSalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// enregitrer la nouvelle salle dans la base de donn�es 
				// mettre a jour le contenue de la salle de discussion
				try {
					refCon.requestCreerSalle(parent.currentUser, new Salle(
                            textFieldNomSalle.getText(),
                            textAreaDescriptionSalle.getText()
                    ));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				parent.setVisible(true);
				setVisible (false);
				
			}
		});
		btnCreerSalle.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		buttonEffacerChamps = new JButton("R\u00E9initialiser");
		buttonEffacerChamps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Effacer les champs 
				textFieldNomSalle.setText("");
				textAreaDescriptionSalle.setText("");
			}
		});
		buttonEffacerChamps.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		textAreaDescriptionSalle = new JTextArea();
		textAreaDescriptionSalle.setForeground(new Color(0, 0, 205));
		textAreaDescriptionSalle.setBackground(new Color(255, 255, 224));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(38)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDescriptionSalle, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblNomSalle, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
									.addGap(26)
									.addComponent(textFieldNomSalle, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(163)
									.addComponent(buttonEffacerChamps, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(btnCreerSalle, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(textAreaDescriptionSalle, GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(19)
					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(33)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNomSalle, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldNomSalle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addComponent(lblDescriptionSalle, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textAreaDescriptionSalle, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonEffacerChamps, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCreerSalle, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}

}
