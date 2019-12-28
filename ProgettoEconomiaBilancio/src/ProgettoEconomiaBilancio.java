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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProgettoEconomiaBilancio{

	private JFrame frame;
	private JComboBox comboAzienda;
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
	public void aggiornaComboAzienda()
	{
		comboAzienda.removeAllItems();
		String sql = "SELECT id, Nome, Descrizione FROM Aziende";
        try (Connection conn = Globs.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
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
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		
		JToolBar toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
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
			 * @author Davide
			 * Evento di click sul pulsante btnAggiungiAzienda, serve per aprire il pannello in cui vengono inseriti i dati di una
			 * nuova azienda.
			 */
			public void mouseClicked(MouseEvent e) {
				AggiungiAzienda aggiungiAzienda = new AggiungiAzienda();
			}
		});
		toolBar.add(btnAggiungiAzienda);
		
		JButton btnCreaBilancio = new JButton("Crea Bilancio");
		toolBar.add(btnCreaBilancio);
		
		JButton btnModificaBilancio = new JButton("Modifica Bilancio");
		toolBar.add(btnModificaBilancio);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
	}

}
