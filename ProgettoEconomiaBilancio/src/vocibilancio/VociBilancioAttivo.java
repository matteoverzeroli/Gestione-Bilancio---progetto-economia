package vocibilancio;

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
	CI("   I- RIMANENTE"),
	CII("   II-CREDITI"),
	CIII("   III-ATTIVITA' FINANZIARIE NON IMMOBILIZZATE"),
	CIV("   IV-DISPONIBILITA' LIQUIDE"),
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
