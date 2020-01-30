import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTable;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.Sizes;
import net.miginfocom.swing.MigLayout;
import vocibilancio.VociBilancioAttivo;
import vocibilancio.VociBilancioPassivo;

import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import java.awt.Dimension;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProgettoEconomiaBilancio {

	private JFrame frame;
	private JComboBox comboAzienda;
	private JComboBox comboBoxBilancio;
	private JTable table;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProgettoEconomiaBilancio window = new ProgettoEconomiaBilancio();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ProgettoEconomiaBilancio() {
		Globs.createNewDatabase("Test.db");
		initialize();
	}

	/**
	 * Funzione per l'inserimento delle aziende dal database alla comboAzienda
	 */
	public void aggiornaComboAzienda() {
		comboAzienda.removeAllItems();
		String sql = "SELECT id, Nome, Descrizione FROM Aziende";
		try (Connection conn = Globs.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			comboAzienda.addItem("*Azienda non selezionata!*");
			// loop through the result set
			while (rs.next()) {
				comboAzienda.addItem(rs.getString("Nome"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Aggiorna la combo bilancio con tutti i bilanci dell'azienda in questione e
	 * setta come attivo l'ultimo bilancio creato
	 * 
	 * @param anno
	 * @param idAzienda
	 */
	public void aggiornaComboBilancio(int anno, int idAzienda) {
		comboBoxBilancio.removeAllItems();
		String sql = "SELECT Anno FROM Bilanci WHERE id = " + idAzienda + " ";
		try (Connection conn = Globs.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				comboBoxBilancio.addItem(rs.getString("Anno"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Globs.setHomeWindow(this); // setta nella classe Globs il riferimento a questa finestra

		frame = new JFrame();
		frame.setMinimumSize(new Dimension(700, 500));
		frame.getContentPane().setBackground(Color.BLACK);
		frame.getContentPane()
				.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("438px:grow"), },
						new RowSpec[] { RowSpec.decode("24px"), FormSpecs.RELATED_GAP_ROWSPEC,
								new RowSpec(RowSpec.CENTER,
										Sizes.bounded(Sizes.DEFAULT, Sizes.constant("100dlu", false),
												Sizes.constant("100dlu", false)),
										0),
								FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, "1, 1, fill, top");

		JLabel lblAzienda = new JLabel("Azienda:");
		toolBar.add(lblAzienda);

		comboAzienda = new JComboBox();
		toolBar.add(comboAzienda);
		/* Inserisce nella comboAzienda le aziende che sono state inserite */
		aggiornaComboAzienda();

		JButton btnAggiungiAzienda = new JButton("Aggiungi Azienda");
		btnAggiungiAzienda.addMouseListener(new MouseAdapter() {
			@Override
			/**
			 * @author Davide Evento di click sul pulsante btnAggiungiAzienda, serve per
			 *         aprire il pannello in cui vengono inseriti i dati di una nuova
			 *         azienda.
			 */
			public void mouseClicked(MouseEvent e) {
				apriPannelloAzienda();
			}
		});
		toolBar.add(btnAggiungiAzienda);

		JButton btnCancellaAzienda = new JButton("Cancella Azienda");

		btnCancellaAzienda.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cancellaAziendaDalDB();
			}
		});

		btnCancellaAzienda.setForeground(Color.RED);
		toolBar.add(btnCancellaAzienda);

		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, "1, 3, fill, fill");
		panel.setLayout(new MigLayout("", "[][grow]", "[][]"));

		JLabel lblBilancio = new JLabel("Bilancio:");
		panel.add(lblBilancio, "cell 0 0,alignx trailing");

		comboBoxBilancio = new JComboBox();
		panel.add(comboBoxBilancio, "flowx,cell 1 0,growx");

		JButton btnCreaBilancio = new JButton("Inserisci Bilancio");
		btnCreaBilancio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				apriPannelloBilancio();
			}
		});
		panel.add(btnCreaBilancio, "cell 1 0");

		JButton btnRimuoviBilancio = new JButton("Rimuovi Bilancio");
		panel.add(btnRimuoviBilancio, "cell 1 0");

		JButton btnImportaBilancio = new JButton("Importa Bilancio");
		panel.add(btnImportaBilancio, "flowx,cell 1 1");

		JButton btnEsportaBilancio = new JButton("Esporta Bilancio");
		panel.add(btnEsportaBilancio, "cell 1 1");

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, "1, 5, fill, fill");
		panel_1.setLayout(new MigLayout("", "[210px][grow]", "[65px,grow]"));

		table = new JTable();
		table.setMaximumSize(new Dimension(1000000, 1000000));
		table.setSize(new Dimension(0, 500));
		panel_1.add(table, "cell 0 0,grow");

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, "cell 1 0,grow");
		panel_2.setLayout(new MigLayout("", "[grow]", "[100px:n:100px][grow][grow]"));

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, "cell 0 0,grow");
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));

		JRadioButton rdbtnPassivo = new JRadioButton("Passivo");
		panel_3.add(rdbtnPassivo);

		JRadioButton rdbtnAttivo = new JRadioButton("Attivo");
		panel_3.add(rdbtnAttivo);

		Box verticalBox = Box.createVerticalBox();
		panel_3.add(verticalBox);

		JRadioButton rdbtnDare = new JRadioButton("Dare");
		verticalBox.add(rdbtnDare);

		JRadioButton rdbtnAvere = new JRadioButton("Avere");
		rdbtnAvere.setMinimumSize(new Dimension(61, 23));
		rdbtnAvere.setMaximumSize(new Dimension(61, 23));
		verticalBox.add(rdbtnAvere);

		/*
		 * Creazione gruppo di radiobuttons affinchè sia possibile selezionare un solo
		 * radiobutton per gruppo alla volta
		 * 
		 */
		ButtonGroup gruppoAttivoPassivo = new ButtonGroup();
		gruppoAttivoPassivo.add(rdbtnAttivo);
		gruppoAttivoPassivo.add(rdbtnPassivo);

		ButtonGroup gruppoDareAvere = new ButtonGroup();
		gruppoDareAvere.add(rdbtnDare);
		gruppoDareAvere.add(rdbtnAvere);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panel_3.add(horizontalStrut_1);

		Box horizontalBox = Box.createHorizontalBox();
		panel_3.add(horizontalBox);

		JLabel lblValore = new JLabel("Valore:");
		horizontalBox.add(lblValore);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(1)));
		spinner.setMinimumSize(new Dimension(120, 20));
		spinner.setMaximumSize(new Dimension(120, 20));
		horizontalBox.add(spinner);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel_3.add(horizontalStrut);

		Box horizontalBox_1 = Box.createHorizontalBox();
		panel_3.add(horizontalBox_1);

		JLabel lblCodice = new JLabel("Voce bilancio:");
		horizontalBox_1.add(lblCodice);

		JComboBox comboBoxVociBilancio = new JComboBox();
		comboBoxVociBilancio.setMinimumSize(new Dimension(150, 22));
		comboBoxVociBilancio.setMaximumSize(new Dimension(150, 22));
		horizontalBox_1.add(comboBoxVociBilancio);

		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, "cell 0 1,grow");

		Box verticalBox_1 = Box.createVerticalBox();
		panel_4.add(verticalBox_1);

		JLabel lblDescrizione = new JLabel("Descrizione:");
		verticalBox_1.add(lblDescrizione);

		JTextPane textPane = new JTextPane();
		verticalBox_1.add(textPane);

		JPanel panel_5 = new JPanel();
		panel_2.add(panel_5, "cell 0 2,grow");

		JButton btnInserisciMastrino = new JButton("Inserisci Mastrino");
		btnInserisciMastrino.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				aggiungiMastrinoAlDB();
			}
		});
		panel_5.add(btnInserisciMastrino);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		/**
		 * Metodi attivati al click su radio button
		 * 
		 * @author Matteo
		 */

		rdbtnAttivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBoxVociBilancio.removeAllItems();
				aggiornaComboVociBilancio(comboBoxVociBilancio, true);
			}
		});

		rdbtnPassivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBoxVociBilancio.removeAllItems();
				aggiornaComboVociBilancio(comboBoxVociBilancio, false);
			}
		});

	}

	/**
	 * Metodo per aggiornare la combo che cotiene le voci del bilancio in base alla
	 * selezione dei radio button corripondenti
	 * 
	 * @author Matteo
	 * @param type true ->attivo, false ->passivo
	 */

	private void aggiornaComboVociBilancio(JComboBox comboBoxVociBilancio, boolean type) {
		/*
		 * Aggiunta alla combo delle voci del bilancio
		 */
		if (type) {
			for (VociBilancioAttivo voce : VociBilancioAttivo.values()) {
				comboBoxVociBilancio.addItem(voce.toString());
			}
		} else {
			for (VociBilancioPassivo voce : VociBilancioPassivo.values()) {
				comboBoxVociBilancio.addItem(voce.toString());
			}
		}

	}

	/**
	 * @author Matteo Apre il pannello aggiungi azienda
	 *
	 */
	private void apriPannelloAzienda() {
		new AggiungiAzienda();
	}

	/**
	 * @author Davide Apre il pannello inserisci bilancio
	 */
	private void apriPannelloBilancio() {
		String azienda = comboAzienda.getSelectedItem().toString();
		String sql = "SELECT id FROM Aziende WHERE Nome = '" + azienda + "' ";
		try (Connection conn = Globs.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				InserisciBilancio bilancio = new InserisciBilancio();
				bilancio.setIdAzienda(rs.getInt("id"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Metodo per cancellare una azienda dal db
	 * 
	 * @author Matteo
	 */
	private void cancellaAziendaDalDB() {
		Object[] possibilities = { "Azienda non selezionata!" };

		String sql = "SELECT id, Nome, Descrizione FROM Aziende";
		try (Connection conn = Globs.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			int i = 0;
			while (rs.next()) {
				/*
				 * autore matteo : poco elegante solo provvisorio
				 */
				Object[] temp = new Object[i + 1];
				temp = possibilities;
				possibilities = new Object[i + 2];
				for (int k = 0; k < i + 1; k++) {
					possibilities[k] = temp[k];
				}
				possibilities[i + 1] = rs.getString("Nome");
				i++;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		String aziendaselezionata = (String) JOptionPane.showInputDialog(frame,
				"Seleziona il nome dell'azienda che vuoi cancellare", "Cancellazione Azienda",
				JOptionPane.PLAIN_MESSAGE, null, possibilities, "ham");
		while (aziendaselezionata != null && aziendaselezionata.compareTo("Azienda non selezionata!") == 0) {
			JFrame frame = new JFrame("Show Message Box");
			JOptionPane.showMessageDialog(frame, "Selezionare una azienda!", "ERRORE", JOptionPane.ERROR_MESSAGE);
			aziendaselezionata = (String) JOptionPane.showInputDialog(frame,
					"Seleziona il nome dell'azienda che vuoi cancellare", "Cancellazione Azienda",
					JOptionPane.PLAIN_MESSAGE, null, possibilities, "ham");
		}
		
		String qry = "DELETE FROM Aziende WHERE Nome = '" + aziendaselezionata + "'";
		try (Connection conn = Globs.connect(); PreparedStatement pstmt = conn.prepareStatement(qry)) {
			pstmt.executeUpdate();
		} catch (SQLException p) {
			System.out.println(p.getMessage());
		}
		
		aggiornaComboAzienda();

	}

	/**
	 * @author Davide Qua dentro bisognerà estrarre tutte le informazioni della
	 *         pagina e inserirle all'interno del DB
	 */
	private void aggiungiMastrinoAlDB() {
		aggiornaTabella();
	}

	/**
	 * @author Davide qui bisognerà andare a cercare tutti i mastrini dell'azienda
	 *         selezionata e dell'anno selezionato ed andare a inserirli nella
	 *         tabella popolandola con tutti i campi
	 */
	private void aggiornaTabella() {

	}
}
