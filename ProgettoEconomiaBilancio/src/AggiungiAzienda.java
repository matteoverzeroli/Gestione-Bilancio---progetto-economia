import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AggiungiAzienda extends JFrame {
	// CAMPI
	private ProgettoEconomiaBilancio homeWindow; // campo che contiene il riferimento alla finestra pannello bilancio

	private JPanel contentPane;
	private JTextField textNome;
	private ProgettoEconomiaBilancio progetto;

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

		homeWindow = Globs.getHomeWindow(); // setta homeWindows con il riferimento alla finestra
											// ProgettoEconomiaBilancio

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setForeground(Color.WHITE);
		lblNome.setBounds(10, 11, 48, 14);
		contentPane.add(lblNome);

		textNome = new JTextField();
		textNome.setBounds(50, 8, 96, 20);
		contentPane.add(textNome);
		textNome.setColumns(10);

		JButton btnCancella = new JButton("Cancella");
		btnCancella.setBounds(10, 231, 89, 23);
		contentPane.add(btnCancella);

		JButton btnOk = new JButton("OK");
	
		btnOk.addActionListener(e -> {
			if (controlloDatiInseriti()) {
				aggiungiAziendaAlDB(); //aggiungo azienza inserita al db dopo aver controllato validità dei dati
			}
		});

		btnOk.setBounds(339, 231, 89, 23);
		contentPane.add(btnOk);
		setVisible(true);
	}

	/**
	 * @author Matteo Metodo per controllare se dati inseriti dall'utente sono validi
	 * 
	 * @return true se dati inseriti sono giusti - false se i dati inseriti non sono giusti
	 *
	 */

	private boolean controlloDatiInseriti() {
		String nomeinserito;
		try {
			nomeinserito = textNome.getText();
		} catch (NullPointerException exception) {
			finestraErrore("Dati inseriti in \"Nome\" non validi!");
			return false;
		}
		if (nomeinserito.isEmpty()) {
			finestraErrore("Dati inseriti in \"Nome\" non validi!\n" + "Scrivere un nome");
			return false;
		}
		return true;

	}

	private void finestraErrore(String errore) {
		JFrame frame = new JFrame("Show Message Box");
		JOptionPane.showMessageDialog(frame,errore,"ERRORE",JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @author Davide Aggiunge i dati inseriti nel DB, aggiorna la comboAzienda
	 *         chiude la finestra
	 */
	private void aggiungiAziendaAlDB() {
		String name = textNome.getText();
		String qry = "INSERT INTO Aziende (Nome) VALUES ('" + name + "')";
		try (Connection conn = Globs.connect(); PreparedStatement pstmt = conn.prepareStatement(qry)) {
			pstmt.executeUpdate();
		} catch (SQLException p) {
			System.out.println(p.getMessage());
		}

		homeWindow.aggiornaComboAzienda();// aggiorna il contenuto della combo

		this.dispose();
	}
}
