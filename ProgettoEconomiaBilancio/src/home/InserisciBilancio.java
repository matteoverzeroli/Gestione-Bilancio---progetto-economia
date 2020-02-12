package home;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.util.Date;
import java.util.Calendar;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InserisciBilancio extends JFrame {

	private ProgettoEconomiaBilancio homeWindow;
	private JPanel contentPane;
	private JSpinner spinnerAnno;

	private int idAzienda = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InserisciBilancio frame = new InserisciBilancio();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setIdAzienda(int idAzienda) {
		this.idAzienda = idAzienda;
	}

	/**
	 * Create the frame.
	 */
	public InserisciBilancio() {

		homeWindow = Globs.getHomeWindow();

		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 225, 201);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setBackground(Color.LIGHT_GRAY);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnChiudi = new JButton("Chiudi");
		btnChiudi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				buttonChiudiClicked();
			}
		});
		btnChiudi.setBounds(10, 135, 91, 23);
		contentPane.add(btnChiudi);

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnOK.setBounds(136, 135, 63, 23);
		btnOK.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				buttonOkClicked();
			}
		});
		contentPane.add(btnOK);

		JLabel lblAnno = new JLabel("Anno:");
		lblAnno.setForeground(Color.WHITE);
		lblAnno.setBounds(10, 11, 48, 14);
		contentPane.add(lblAnno);

		spinnerAnno = new JSpinner();

		spinnerAnno.setModel(new SpinnerNumberModel(getCurrentYear() - 1, null, getCurrentYear() - 1, new Integer(1)));
		spinnerAnno.setBounds(68, 8, 133, 20);

		/*
		 * le seguenti righe di codice tolgono il punto delle migliaia
		 */

		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinnerAnno, "#");
		spinnerAnno.setEditor(editor);

		contentPane.add(spinnerAnno);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	/**
	 * Restitusce l'anno corrente
	 * 
	 * @author Matteo
	 * @return anno corrente
	 */
	private int getCurrentYear() {
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);

		return cal.get(Calendar.YEAR);
	}

	private void buttonOkClicked() {
		int anno = (int) spinnerAnno.getValue();
		if (homeWindow.getAnnoBilanci(anno)) {
			String qry = "INSERT INTO Bilanci (id, Anno) VALUES (" + idAzienda + ", " + anno + ")";
			try (Connection conn = Globs.connect(); PreparedStatement pstmt = conn.prepareStatement(qry)) {
				pstmt.executeUpdate();
			} catch (SQLException p) {
				System.out.println(p.getMessage());
			}

			homeWindow.aggiornaComboBilancio(idAzienda);
			this.dispose();
		} else {
			JFrame frame = new JFrame("Show Message Box");
			JOptionPane.showMessageDialog(frame, "Anno Bilancio già inserito", "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @author Matteo Metodo che chiede se l'utente è sicuro di uscire
	 */

	private void buttonChiudiClicked() {
		JFrame frame = new JFrame("Show Message Box");
		Object[] options = { "Si", "No" };
		int n = JOptionPane.showOptionDialog(frame, "Sei sicuro di voler chiudere?", "Attenzione",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (n == 0) {
			this.dispose(); // chiudi la finestra
		} else {
			// non fare nulla
		}
	}
}
