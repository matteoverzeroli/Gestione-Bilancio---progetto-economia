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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnChiudi = new JButton("Chiudi");
		btnChiudi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				buttonChiudiClicked();
			}
		});
		btnChiudi.setBounds(10, 231, 91, 23);
		contentPane.add(btnChiudi);

		JButton btnOK = new JButton("OK");
		btnOK.setBounds(365, 231, 63, 23);
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

		spinnerAnno.setModel(new SpinnerNumberModel(getCurrentYear(), null, getCurrentYear(), new Integer(1)));
		spinnerAnno.setBounds(68, 8, 133, 20);

		/*
		 * le seguenti righe di codice tolgono il punto delle migliaia
		 */

		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinnerAnno, "#");
		spinnerAnno.setEditor(editor);

		contentPane.add(spinnerAnno);
		setVisible(true);
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

	/**
	 * ATTENZIONE: Bisogna inserire il controllo per cui uno non puo scegliere un
	 * anno di bilancio che ha gia inserito
	 */
	private void buttonOkClicked() {
		int anno = (int) spinnerAnno.getValue();
		String qry = "INSERT INTO Bilanci (id, Anno) VALUES (" + idAzienda + ", " + anno + ")";
		try (Connection conn = Globs.connect(); PreparedStatement pstmt = conn.prepareStatement(qry)) {
			pstmt.executeUpdate();
		} catch (SQLException p) {
			System.out.println(p.getMessage());
		}

		homeWindow.aggiornaComboBilancio(anno, idAzienda);
		this.dispose();
	}
	
	/**
	 * @author Matteo
	 * Metodo che chiede se l'utente è sicuro di uscire
	 */

	private void buttonChiudiClicked() {
		JFrame frame = new JFrame("Show Message Box");
		Object[] options = { "Si", "No"};
		int n = JOptionPane.showOptionDialog(frame, "Sei sicuro di voler chiudere?",
				"Attenzione", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				options[1]);
		if(n == 0) {
			this.dispose(); //chiudi la finestra 
		} else {
			//non fare nulla
		}
	}
}
