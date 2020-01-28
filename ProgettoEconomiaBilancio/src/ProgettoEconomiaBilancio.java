import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.SpinnerNumberModel;

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

		Box verticalBox = Box.createVerticalBox();
		panel_3.add(verticalBox);

		JRadioButton rdbtnEntrate = new JRadioButton("Entrate");
		verticalBox.add(rdbtnEntrate);

		JRadioButton rdbtnUscite = new JRadioButton("Uscite");
		rdbtnUscite.setMinimumSize(new Dimension(61, 23));
		rdbtnUscite.setMaximumSize(new Dimension(61, 23));
		verticalBox.add(rdbtnUscite);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panel_3.add(horizontalStrut_1);

		Box horizontalBox = Box.createHorizontalBox();
		panel_3.add(horizontalBox);

		JLabel lblValore = new JLabel("Valore:");
		horizontalBox.add(lblValore);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
		spinner.setMinimumSize(new Dimension(120, 20));
		spinner.setMaximumSize(new Dimension(120, 20));
		horizontalBox.add(spinner);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel_3.add(horizontalStrut);

		Box horizontalBox_1 = Box.createHorizontalBox();
		panel_3.add(horizontalBox_1);

		JLabel lblCodice = new JLabel("Codice:");
		horizontalBox_1.add(lblCodice);

		JComboBox comboBox = new JComboBox();
		comboBox.setMinimumSize(new Dimension(150, 22));
		comboBox.setMaximumSize(new Dimension(150, 22));
		horizontalBox_1.add(comboBox);

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
