import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JComboBox;

import java.sql.SQLException;

/**
 * 
 * @author Davide
 * Classe da utilizzare come contenitore di variabili globali che devono essere viste e utilizzate in tutto il progetto.
 * Qua dentro ci vanno solo metodi e variabili static.
 */
public class Globs {

	/**
	 * La tabella Aziende e la tabella Mastrini sono collegate tra di loro tramite un id. In sostanza quando creo un mastrino esso
	 * avrà lo stesso id dell'azienda a cui è associato. In questo modo si sa quali mastrini appartengono a quale azienda e per ottenerli basta 
	 * fare una query al database come segue:
	 * "SELECT FROM Mastrini WHERE id = " + idAzienda + ";"
	 * Se c'è bisogno di aggiungere dell tabelle non c'è problema, se invece si vogliono introdurre nuove colonne al db bisogna eliminarlo ogni volta,
	 * quindi meglio se riusciamo a definirle bene subito.
	 * 
	 * Questo è il link da cui ho preso le informazioni per il db:
	 * https://www.sqlitetutorial.net/sqlite-java/
	 */
	
	/**
     * Funzione per connettersi al database
     *
     * @return the Connection object
     */
    public static Connection connect() {
        // SQLite connection string
    	String url = "jdbc:sqlite:test.db";
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
    public static void createNewDatabase(String fileName) {
 
    	try {
    		Class.forName("org.sqlite.JDBC");
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	
        String url = "jdbc:sqlite:test.db";
        /* La sezione bilanci contiene gli anni dei bilanci associati all'azienda, ad esempio se ho creato un 
         * bilancio del 2016 e uno del 2017 allora conterrà 2016;2017 . il separatore è il ;*/
        String sql1 = "CREATE TABLE IF NOT EXISTS Aziende ( "
                + "    id		 	INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "    Nome 	 	CHAR (30) NOT NULL, "
                + "    Descrizione  CHAR (200),"
                + "    Bilanci      CHAR (200) "
                + ");";
        
        /* la variabile del database InOut indica se è un entrata o un uscita quindi + o - nel conto*/
        String sql2 = "CREATE TABLE IF NOT EXISTS Mastrini ( "
                + "    id    		INTEGER REFERENCES Aziende (id), "
                + "    Anno         INTEGER, "
                + "    Voce         INTEGER, "
                + "    Euro         DOUBLE, "
                + "    InOut        INTEGER, "
                + "    Note         CHAR (300) "
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url);
        		Statement stmt = conn.createStatement()) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                stmt.execute(sql1);
                stmt.execute(sql2);
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
