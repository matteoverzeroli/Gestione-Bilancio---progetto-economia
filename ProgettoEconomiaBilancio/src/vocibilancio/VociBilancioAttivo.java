package vocibilancio;
/**
 * Classe enum per la definizione delle voci di bilancio
 * 
 * @author Matteo
 *
 */
public enum VociBilancioAttivo {
	/*
	 * Vengono aggiunti tre spazi per identazione dei sottogruppi
	 */
	A("A-CREDITI VERSO SOCI PER VERSAMENTI ANCORA DOVUTI"),
	B("B-IMMOBILIZZAZIONI"),
	BI("   I-IMMOBILIZZAZIONI IMMATERIALI"),
	BII("   II-IMMOBILIZZAZIONI MATERIALI"),
	BIII("   III- IMMOBILIZZAZIONI FINANZIARIE"),
	C("C-ATTIVO CIRCOLANTE"),
	CI("   I-RIMANENZE"),
	CII("   II-CREDITI"),
	CIII("   III-ATTIVITÀ FINANZIARIE NON IMMOBILIZZATE"),
	CIV("   IV-DISPONIBILITÀ LIQUIDE"),
	D("D-RATEI E RISCONTI ATTIVI");
	
	
	
	private String name;
	
	private VociBilancioAttivo(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}
}
