package pdfcreator;

import home.ProgettoEconomiaBilancio;

/**
 * 
 * Classe che conterrà alcuni risultati dei mastrini inseriti
 * 
 * @author Matteo
 *
 */

public class RisultatiMastrini {
	// CAMPI
	private static double TOTALEATTIVO = 0;
	private static double TOTALEPASSIVO = 0;
	private static double VALOREPRODUZIONE = 0;
	private static double COSTIPRODUZIONE = 0;
	private static double TOTALEPROVENTIEONERIFINANZIARI= 0;
	private static double TOTALERETTIFICHEDIVALOREDIATTIVITAFINANZIARIE= 0;
	private static double PERCENTUALEIMPOSTE = 40;




	static void setTotaleAttivo(double totale) {
		TOTALEATTIVO = totale;
	}

	static void setTotalePassivo(double totale) {
		TOTALEPASSIVO = totale;
	}

	static void setValoreProduzione(double valore) {
		VALOREPRODUZIONE = valore;
	}

	static void setCostiProduzione(double valore) {
		COSTIPRODUZIONE = valore;
	}
	static void setTotaliProventiEOneriFinanziari(double valore) {
		TOTALEPROVENTIEONERIFINANZIARI = valore;
	}
	static void setTotaliRettificheDiValoreEDiAttivitaFinanziarie(double valore) {
		TOTALERETTIFICHEDIVALOREDIATTIVITAFINANZIARIE = valore;
	}
	static void setPercentualeImposte(double valore) {
		PERCENTUALEIMPOSTE = valore;
	}


	static double getTotaleAttivo() {
		return TOTALEATTIVO;
	}

	static double getTotalePassivo() {
		return TOTALEPASSIVO;
	}

	static double getValoreProduzione() {
		return VALOREPRODUZIONE;
	}

	static double getCostiProduzione() {
		return COSTIPRODUZIONE;
	}
	
	static double getEBIT() {
		return VALOREPRODUZIONE+COSTIPRODUZIONE;
	}
	
	static double getTotalEProventiEOneriFinanziari() {
		return TOTALEPROVENTIEONERIFINANZIARI;
	}
	static double getUtileAnteImposte() {
		return getEBIT()+getTotalEProventiEOneriFinanziari()+getTotaliRettificheDiValoreEDiAttivitaFinanziarie();
	}
	
	static double getTotaliRettificheDiValoreEDiAttivitaFinanziarie() {
		return TOTALERETTIFICHEDIVALOREDIATTIVITAFINANZIARIE;
	}
	static double getTotaleImposte() {
		return -getUtileAnteImposte()*PERCENTUALEIMPOSTE/100;
	}
	static double getUtile() {
		return getUtileAnteImposte()-(getUtileAnteImposte()*PERCENTUALEIMPOSTE/100);
	}
	
}
