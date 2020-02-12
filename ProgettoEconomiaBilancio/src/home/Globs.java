package home;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;

import java.sql.SQLException;

/**
 * 
 * @author Davide Classe da utilizzare come contenitore di variabili globali che
 *         devono essere viste e utilizzate in tutto il progetto. Qua dentro ci
 *         vanno solo metodi e variabili static.
 */
public class Globs {

	/**
	 * La tabella Aziende e la tabella Mastrini sono collegate tra di loro tramite
	 * un id. In sostanza quando creo un mastrino esso avrà lo stesso id
	 * dell'azienda a cui è associato. In questo modo si sa quali mastrini
	 * appartengono a quale azienda e per ottenerli basta fare una query al database
	 * come segue: "SELECT FROM Mastrini WHERE id = " + idAzienda + ";" Se c'è
	 * bisogno di aggiungere dell tabelle non c'è problema, se invece si vogliono
	 * introdurre nuove colonne al db bisogna eliminarlo ogni volta, quindi meglio
	 * se riusciamo a definirle bene subito.
	 * 
	 * Questo è il link da cui ho preso le informazioni per il db:
	 * https://www.sqlitetutorial.net/sqlite-java/
	 */

	// CAMPI
	private static ProgettoEconomiaBilancio homeWindow; // campo che contiene riferimento alla finestra
	private static String databasename; // contiene il nome del file del database // ProgettoBilancioEconomia

	protected static final int lunghezzaMaxNomeAzienda = 30;
	protected static final int lunghezzaMaxDescrizioneAzienda = 200;

	/**
	 * @author Matteo
	 * @param window riferiemento alla finestra ProgettoEconomiaBilancio
	 * 
	 *               Copia il riferimento passato come parametro nella variabile
	 *               homeWindow in modo tale che è possibile sempre avere un
	 *               riferimento ad essa
	 */
	public static void setHomeWindow(ProgettoEconomiaBilancio window) {
		homeWindow = window;
	}

	/**
	 * @author Matteo
	 * @return riferimento alla finestra principale
	 * 
	 *         Metodo che restituisce il riferimento alla finestra principale
	 */

	public static ProgettoEconomiaBilancio getHomeWindow() {
		return homeWindow;
	}

	/**
	 * Funzione per connettersi al database
	 *
	 * @return the Connection object
	 */
	public static Connection connect() {
		// SQLite connection string
		String url = "jdbc:sqlite:" + databasename;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	/**
	 * Funzione per creare il database
	 *
	 * @param fileName the database file name
	 */
	public static void createNewDatabase(String databasename) {
		Globs.databasename = databasename;

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			// TODO: handle exception
		}

		String url = "jdbc:sqlite:" + Globs.databasename;

		String sql1 = "CREATE TABLE IF NOT EXISTS Aziende ( " + "    id		 	INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "    Nome 	 	CHAR (" + String.valueOf(lunghezzaMaxNomeAzienda) + ") NOT NULL, "
				+ "    Descrizione  CHAR (" + String.valueOf(lunghezzaMaxNomeAzienda) + ") " + ");";
		/*
		 * a ogni azienda può essere associato più di un bilancio: id - è l'intero con
		 * cui un bilancio è associato all'azienda Reference - è un identificativo
		 * univoco del bilancio in questione
		 */
		String sql3 = "CREATE TABLE IF NOT EXISTS Bilanci ( " + "	   id           INTEGER REFERENCES Aziende (id), "
				+ "    Reference    INTEGER PRIMARY KEY AUTOINCREMENT, " + "    Anno         INTEGER, "
				+ "    Note         CHAR (300) " + ");";

		/*
		 * ATTENZIONE ALLA GESTIONE ENTRATE/USCITE -> ATTIVO / PASSIVO
		 */

		/*
		 * la variabile del database InOut indica se è un entrata o un uscita quindi + o
		 * - nel conto id - è connesso a un bilancio tramite la variabile Reference
		 */
		String sql2 = "CREATE TABLE IF NOT EXISTS Mastrini ( "
				+ "    id    		INTEGER REFERENCES Bilanci (Reference), "
				+ "    idMastrino    INTEGER PRIMARY KEY AUTOINCREMENT, " + "    Anno         INTEGER, "
				+ "    Voce         CHAR (100), " + "    Euro         DOUBLE, " + "    InOut        CHAR (10), "
				+ "    Attivo       CHAR (10), " + "    Note         CHAR (300) " + ");";

		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");
				stmt.execute(sql1);
				stmt.execute(sql3);
				stmt.execute(sql2);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
