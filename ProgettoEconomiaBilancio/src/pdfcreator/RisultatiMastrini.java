package pdfcreator;
/**
 * 
 * Classe che conterrà alcuni risultati dei mastrini inseriti
 * @author Matteo
 *
 */

public class RisultatiMastrini {
	//CAMPI
	private static double TOTALEATTIVO;
	private static double TOTALEPASSIVO;
	
	static void setTotaleAttivo(double totale) {
		TOTALEATTIVO = totale;
	}
	static void setTotalePassivo(double totale) {
		TOTALEPASSIVO = totale;
	}
	
	static double getTotaleAttivo() {
		return TOTALEATTIVO;
	}
	static double getTotalePassivo() {
		return TOTALEPASSIVO;
	}
	
	
}
