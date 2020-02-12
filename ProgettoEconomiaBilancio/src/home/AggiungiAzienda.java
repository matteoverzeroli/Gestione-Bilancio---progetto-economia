package home;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.SwingConstants;
import java.awt.Font;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class AggiungiAzienda extends JFrame {
	// CAMPI

	private ProgettoEconomiaBilancio homeWindow; // campo che contiene il riferimento alla finestra pannello bilancio

	private JPanel contentPane;
	private JTextField textNome;
	private JTextArea textDescrizione;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AggiungiAzienda frame = new AggiungiAzienda();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AggiungiAzienda() {
		setResizable(false);
		setTitle("Aggiungi azienda\r\n");

		homeWindow = Globs.getHomeWindow(); // setta homeWindows con il riferimento alla finestra
											// ProgettoEconomiaBilancio

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 276, 283);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblNome = new JLabel("Nome azienda:");
		lblNome.setForeground(Color.BLACK);

		textNome = new JTextField();
		textNome.setFocusTraversalKeysEnabled(false);
		textNome.setColumns(10);

		Component horizontalStrut = Box.createHorizontalStrut(20);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);

		JLabel label_1 = new JLabel("");

		JLabel label_2 = new JLabel("");

		JLabel label_3 = new JLabel("");

		JLabel label_4 = new JLabel("");

		JButton btnOk = new JButton("OK");

		btnOk.addActionListener(e -> {
			if (controlloDatiInseriti()) {
				aggiungiAziendaAlDB(); // aggiungo azienza inserita al db dopo aver controllato validit� dei dati
			}
		});

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);

		JLabel lblH = DefaultComponentFactory.getInstance().createLabel("Descrizione azienda:");
		lblH.setForeground(Color.BLACK);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
						.createSequentialGroup().addGap(6)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNome, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
								.addComponent(textNome, 186, 186, 186).addComponent(lblH)))
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(191)
								.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)))
				.addGap(469)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(horizontalStrut, GroupLayout.PREFERRED_SIZE, 70,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(horizontalStrut_1, GroupLayout.PREFERRED_SIZE, 70,
										GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addGap(84)
								.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)))));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(5)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
						.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(horizontalStrut, GroupLayout.PREFERRED_SIZE, 125,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(horizontalStrut_1, GroupLayout.PREFERRED_SIZE, 125,
										GroupLayout.PREFERRED_SIZE))
						.addGap(0, 0, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_3,
												GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
								.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(lblNome, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
								.addGap(3)
								.addComponent(textNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblH)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, Short.MAX_VALUE)))
				.addContainerGap()));

		Box verticalBox = Box.createVerticalBox();
		panel.add(verticalBox);

		textDescrizione = new JTextArea();
		verticalBox.add(textDescrizione);
		textDescrizione.setSize(new Dimension(100, 50));
		JScrollPane scroll = new JScrollPane(textDescrizione);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		verticalBox.setPreferredSize(new Dimension(240, 120));
		verticalBox.add(scroll);

		textDescrizione.setWrapStyleWord(true);
		textDescrizione.setLineWrap(true);
		textDescrizione.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textDescrizione.setForeground(Color.BLACK);
		textDescrizione.setColumns(10);
		contentPane.setLayout(gl_contentPane);
		setVisible(true);
		setLocationRelativeTo(null);

	}

	/**
	 * @author Matteo Metodo per controllare se dati inseriti dall'utente sono
	 *         validi
	 * 
	 * @return true se dati inseriti sono giusti - false se i dati inseriti non sono
	 *         giusti
	 *
	 */

	private boolean controlloDatiInseriti() {
		String nomeinserito;
		String descrizioneinserita;
		/*
		 * Controlli sul nome inserito
		 */
		try {
			nomeinserito = textNome.getText();
			textDescrizione.setEnabled(true);
		} catch (NullPointerException exception) {
			finestraErrore("Dati inseriti in \"Nome\" non validi!");
			return false;
		}
		if (nomeinserito.isEmpty()) {
			finestraErrore("Dati inseriti in \"Nome\" non validi!\n" + "Scrivere un nome");
			return false;
		}

		if (nomeinserito.length() > Globs.lunghezzaMaxNomeAzienda) {
			finestraErrore("Dati inseriti in \"Nome\" non validi!\n" + "Troppi caratteri! Max "
					+ String.valueOf(Globs.lunghezzaMaxNomeAzienda));
			return false;
		}

		/*
		 * Controlli sulla descrizione inserita
		 */

		try {
			descrizioneinserita = textDescrizione.getText();
		} catch (NullPointerException exception) {
			finestraErrore("Dati inseriti in \"Descrizione\" non validi!");
			return false;
		}

		if (descrizioneinserita.length() > Globs.lunghezzaMaxDescrizioneAzienda) {
			finestraErrore("Dati inseriti in \"Descrizione\" non validi!\n" + "Troppi caratteri! Max "
					+ String.valueOf(Globs.lunghezzaMaxNomeAzienda));
			return false;
		}

		return true;

	}

	private void finestraErrore(String errore) {
		JFrame frame = new JFrame("Show Message Box");
		JOptionPane.showMessageDialog(frame, errore, "ERRORE", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @author Davide Aggiunge i dati inseriti nel DB, aggiorna la comboAzienda
	 *         chiude la finestra
	 */
	private void aggiungiAziendaAlDB() {
		String name = textNome.getText();
		String descrizione =  textDescrizione.getText();
		String qry = "INSERT INTO Aziende (Nome,Descrizione) VALUES ('" + name + "','" + descrizione + "');";
		try (Connection conn = Globs.connect(); PreparedStatement pstmt = conn.prepareStatement(qry)) {
			pstmt.executeUpdate();
		} catch (SQLException p) {
			System.out.println(p.getMessage());
		}

		homeWindow.aggiornaComboAzienda();// aggiorna il contenuto della combo

		this.dispose();
	}
}
