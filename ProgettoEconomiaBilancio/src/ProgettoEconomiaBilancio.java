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
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTable;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.Sizes;
import net.miginfocom.swing.MigLayout;
import vocibilancio.VociBilancioAttivo;
import vocibilancio.VociBilancioContoEconomico;
import vocibilancio.VociBilancioPassivo;

import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import java.awt.Dimension;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;

public class ProgettoEconomiaBilancio {

	private JFrame frame;
	private JComboBox<String> comboAzienda;
	private JComboBox<String> comboBoxBilancio;
	private JTable table;
	private JTextPane textNote;
	private JComboBox<String> comboBoxVociBilancio;
	private JSpinner spinnerValore;
	private JRadioButton rdbtnAttivo;
	private JRadioButton rdbtnPassivo;
	private JRadioButton rdbtnDare;
	private JRadioButton rdbtnAvere;

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
				comboAzienda.setSelectedItem(rs.getString("Nome"));
			}
			comboAzienda.setSelectedIndex(0);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Aggiorna la combo bilancio con tutti i bilanci dell'azienda in questione e
	 * setta come attivo l'ultimo bilancio creato
	 * 
	 * @param idAzienda
	 */
	public void aggiornaComboBilancio(int idAzienda) {
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
		frame.setMinimumSize(new Dimension(1000, 500));
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

		comboAzienda = new JComboBox<String>();

		/*
		 * metodo per aggiornare tabella bilanci al cambiare dell'azienda selezionata
		 * TODO aggiornamento dei mastrini relativi all'azienda
		 */
		comboAzienda.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (comboAzienda.getItemCount() != 0 && comboAzienda.getSelectedItem().toString() != null) {
					if (comboAzienda.getSelectedItem().toString() == "*Azienda non selezionata!*") {
						String azienda = comboAzienda.getSelectedItem().toString();
						comboBoxBilancio.removeAllItems();
					} else {
						String azienda = comboAzienda.getSelectedItem().toString();
						String sql = "SELECT id FROM Aziende WHERE Nome = '" + azienda + "' ";
						try (Connection conn = Globs.connect();
								Statement stmt = conn.createStatement();
								ResultSet rs = stmt.executeQuery(sql)) {
							if (rs.next()) {
								aggiornaComboBilancio(rs.getInt("id"));
							}
						} catch (SQLException e) {
							System.out.println(e.getMessage());
						}
					}

					// aggiornaTabella();
				}
			}

		});
		toolBar.add(comboAzienda);

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

		comboBoxBilancio = new JComboBox<String>();
		panel.add(comboBoxBilancio, "flowx,cell 1 0,growx");

		JButton btnCreaBilancio = new JButton("Inserisci Bilancio");
		btnCreaBilancio.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
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
		table.setMinimumSize(new Dimension(400, 0));
		table.setMaximumSize(new Dimension(1000000, 1000000));
		table.setSize(new Dimension(0, 500));
		panel_1.add(table, "cell 0 0,aligny baseline");

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, "cell 1 0,grow");
		panel_2.setLayout(new MigLayout("", "[grow]", "[100px:n:100px][grow][grow]"));

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, "cell 0 0,grow");
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));

		Box verticalBox_2 = Box.createVerticalBox();
		panel_3.add(verticalBox_2);

		rdbtnAttivo = new JRadioButton("Attivo");
		verticalBox_2.add(rdbtnAttivo);
		/*
		 * Creazione gruppo di radiobuttons affinchè sia possibile selezionare un solo
		 * radiobutton per gruppo alla volta
		 * 
		 */
		ButtonGroup gruppoAttivoPassivoContoEconomico = new ButtonGroup();

		rdbtnPassivo = new JRadioButton("Passivo");
		verticalBox_2.add(rdbtnPassivo);
		gruppoAttivoPassivoContoEconomico.add(rdbtnPassivo);

		JRadioButton rdbtnContoEconomico = new JRadioButton("Conto Economico");
		rdbtnContoEconomico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBoxVociBilancio.removeAllItems();
				aggiornaComboVociBilancio(comboBoxVociBilancio, 2);
			}
		});
		verticalBox_2.add(rdbtnContoEconomico);
		gruppoAttivoPassivoContoEconomico.add(rdbtnContoEconomico);

		rdbtnPassivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBoxVociBilancio.removeAllItems();
				aggiornaComboVociBilancio(comboBoxVociBilancio, 1);
			}
		});

		rdbtnAttivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBoxVociBilancio.removeAllItems();
				aggiornaComboVociBilancio(comboBoxVociBilancio, 0);
			}
		});
		gruppoAttivoPassivoContoEconomico.add(rdbtnAttivo);

		Box verticalBox = Box.createVerticalBox();
		panel_3.add(verticalBox);

		rdbtnDare = new JRadioButton("Dare");
		verticalBox.add(rdbtnDare);

		rdbtnAvere = new JRadioButton("Avere");
		rdbtnAvere.setMinimumSize(new Dimension(61, 23));
		rdbtnAvere.setMaximumSize(new Dimension(61, 23));
		verticalBox.add(rdbtnAvere);

		ButtonGroup gruppoDareAvere = new ButtonGroup();
		gruppoDareAvere.add(rdbtnDare);
		gruppoDareAvere.add(rdbtnAvere);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panel_3.add(horizontalStrut_1);

		Box horizontalBox = Box.createHorizontalBox();
		panel_3.add(horizontalBox);

		JLabel lblValore = new JLabel("Valore:");
		horizontalBox.add(lblValore);

		spinnerValore = new JSpinner();
		spinnerValore.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(1)));
		spinnerValore.setMinimumSize(new Dimension(120, 20));
		spinnerValore.setMaximumSize(new Dimension(120, 20));
		horizontalBox.add(spinnerValore);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel_3.add(horizontalStrut);

		Box horizontalBox_1 = Box.createHorizontalBox();
		panel_3.add(horizontalBox_1);

		JLabel lblCodice = new JLabel("Voce bilancio:");
		horizontalBox_1.add(lblCodice);

		comboBoxVociBilancio = new JComboBox<String>();
		comboBoxVociBilancio.setMinimumSize(new Dimension(100, 22));
		comboBoxVociBilancio.setMaximumSize(new Dimension(150, 22));
		horizontalBox_1.add(comboBoxVociBilancio);

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue_1);

		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, "cell 0 1,grow");

		Box verticalBox_1 = Box.createVerticalBox();
		panel_4.add(verticalBox_1);

		JLabel lblDescrizione = new JLabel("Descrizione:");
		verticalBox_1.add(lblDescrizione);

		textNote = new JTextPane();
		verticalBox_1.add(textNote);

		JPanel panel_5 = new JPanel();
		panel_2.add(panel_5, "cell 0 2,grow");

		JButton btnInserisciMastrino = new JButton("Inserisci Mastrino");
		btnInserisciMastrino.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/*
				 * Inserito controllo su azienda e anno bilancio quando si aggiunge il mastrino
				 */
				if (comboAzienda.getSelectedItem().toString() != "*Azienda non selezionata!*"
						&& comboBoxBilancio.getSelectedItem().toString().length() > 0)
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

		/* Inserisce nella comboAzienda le aziende che sono state inserite */
		aggiornaComboAzienda();
	}

	/**
	 * Metodo per aggiornare la combo che cotiene le voci del bilancio in base alla
	 * selezione dei radio button corripondenti
	 * 
	 * @author Matteo
	 * @param type 0 ->attivo, 1 ->passivo, 2->conto economico
	 */

	private void aggiornaComboVociBilancio(JComboBox<String> comboBoxVociBilancio, int type) {
		/*
		 * Aggiunta alla combo delle voci del bilancio
		 */
		if (type == 0) {
			for (VociBilancioAttivo voce : VociBilancioAttivo.values()) {
				comboBoxVociBilancio.addItem(voce.toString());
			}
		} else if (type == 1) {
			for (VociBilancioPassivo voce : VociBilancioPassivo.values()) {
				comboBoxVociBilancio.addItem(voce.toString());
			}
		} else if (type == 2) {
			for (VociBilancioContoEconomico voce : VociBilancioContoEconomico.values()) {
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
		try (Connection conn = Globs.connect()) {
			int idAzienda = 0;
			int idBilancio = 0;
			Statement stmt = conn.createStatement();
			String qry1 = "SELECT id FROM Aziende WHERE Nome = '" + comboAzienda.getSelectedItem().toString() + "';";
			ResultSet rs1 = stmt.executeQuery(qry1);
			if (rs1.next())
				idAzienda = rs1.getInt("id");

			String qry2 = "SELECT Reference FROM Bilanci WHERE id = " + idAzienda + " && Anno = "
					+ Integer.valueOf(comboBoxBilancio.getSelectedItem().toString()) + ";";
			ResultSet rs2 = stmt.executeQuery(qry2);
			if (rs2.next())
				idBilancio = rs2.getInt("id");

			String dare_avere = "Dare";
			if (rdbtnAvere.isSelected())
				dare_avere = "Avere";

			String attivo_passivo = "Passivo";
			if (rdbtnAttivo.isSelected())
				attivo_passivo = "Attivo";

			String qry = "INSERT INTO Mastrini (id, Anno, Voce, Euro, InOut, Attivo, Note) VALUES (idBilancio, '"
					+ Integer.valueOf(comboBoxBilancio.getSelectedItem().toString()) + "', '"
					+ comboBoxVociBilancio.getSelectedItem().toString() + "', " + spinnerValore.getValue() + ", '"
					+ dare_avere + "', '" + attivo_passivo + "', '" + textNote.getText() + "' )";

			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		aggiornaTabella();
	}

	/**
	 * @author Davide qui bisognerà andare a cercare tutti i mastrini dell'azienda
	 *         selezionata e dell'anno selezionato ed andare a inserirli nella
	 *         tabella popolandola con tutti i campi
	 */
	private void aggiornaTabella() {
		try (Connection conn = Globs.connect()) {
			int idAzienda = 0;
			int idBilancio = 0;
			Statement stmt = conn.createStatement();
			String qry1 = "SELECT id FROM Aziende WHERE Nome = '" + comboAzienda.getSelectedItem().toString() + "';";
			ResultSet rs1 = stmt.executeQuery(qry1);
			if (rs1.next()) {
				idAzienda = rs1.getInt("id");
			}
			String qry2 = "SELECT Reference FROM Bilanci WHERE id = " + idAzienda + " && Anno = "
					+ Integer.valueOf(comboBoxBilancio.getSelectedItem().toString()) + ";";
			ResultSet rs2 = stmt.executeQuery(qry2);
			if (rs2.next()) {
				idBilancio = rs2.getInt("id");
			}

			String query = "SELECT Voce, Euro, InOut, Note FROM Mastrini WHERE id = " + idBilancio + ";";
			ResultSet rs = stmt.executeQuery(query);

			table.setModel(buildTableModel(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}

//        Vector<String> columnNames;
//		columnNames.add(" ");
//        columnNames.add("Column 1");
//        columnNames.add("Column 2");
//        DefaultTableModel model = new DefaultTableModel(buildTableModel(rs), columnNames);
	}

	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}

		return new DefaultTableModel(data, columnNames);

	}

	/**
	 * @author Davide Qui viene creato l'header della tabella
	 */
	private void creaHeaderTabella() {

	}

	/**
	 * @author Matteo Metodo utilizzato dalla classe inserisci Bilancio per
	 *         verificare che non sia già sttao inserito un bilancio con per lo
	 *         stesso anno
	 * @param anno
	 * @return false->inserito true -> non inserito
	 */

	public boolean getAnnoBilanci(int anno) {
		int size = comboBoxBilancio.getItemCount();
		for (int i = 0; i < size; i++) {
			if (anno == Integer.parseInt(comboBoxBilancio.getItemAt(i)))
				return false;
		}
		return true;
	}
}
