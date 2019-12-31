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

public class ProgettoEconomiaBilancio {

	private JFrame frame;
	private JComboBox comboAzienda;
	private JTable table;

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
		initialize();
		Globs.createNewDatabase("Test.db");
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
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Globs.setHomeWindow(this); // setta nella classe Globs il riferimento a questa finestra

		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.getContentPane()
				.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("438px:grow"), },
						new RowSpec[] { RowSpec.decode("24px"), FormSpecs.RELATED_GAP_ROWSPEC,
								RowSpec.decode("default:grow"), FormSpecs.RELATED_GAP_ROWSPEC,
								RowSpec.decode("default:grow"), }));

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

		JButton btnCreaBilancio = new JButton("Crea Bilancio");
		toolBar.add(btnCreaBilancio);

		JButton btnModificaBilancio = new JButton("Modifica Bilancio");
		toolBar.add(btnModificaBilancio);

		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, "1, 3, fill, fill");

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, "1, 5, fill, fill");
		panel_1.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, "2, 2, fill, fill");
		panel_2.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		JButton btnAggiungiMastrino = new JButton("Aggiungi Mastrino");
		panel_2.add(btnAggiungiMastrino, "2, 2");

		table = new JTable();
		panel_1.add(table, "4, 2, fill, fill");
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

}
