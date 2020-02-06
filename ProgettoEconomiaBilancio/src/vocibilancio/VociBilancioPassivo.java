package vocibilancio;
/**
 * Classe enum per la definizione delle voci di bilancio
 * 
 * @author Matteo
 *
 */

public enum VociBilancioPassivo {
	/*
	 * Vengono aggiunti tre spazi per identazione dei sottogruppi
	 */
	A("A-PATRIMONIO NETTO"),
	AI("   I-CAPITALE SOCIALE"),
	AII("   II-RISERVA SOVRAPPREZZO AZIONI"),
	AIII("   III-RISERVA DI VALUTAZIONE"),
	AIV("   IV-RISERVA LEGALE"),
	AV("   V-RISERVA PER AZIONI PROPRIE"),
	AVI("   VI-RISERVE STATUARIE"),
	AVII("   VII-ALTRE RISERVE"),
	AVIII("   VIII-UTILI PORTATI A NUOVO"),
	AIX("   IX-UTILI DELL'ESERCIZIO"),
	B("B-FONDO PER RISCHI E ONERI"),
	BI("   I-PER TRATTAMENTO DI QUIESCENZA"),
	BII("   II-PER IMPOSTE"),
	BIII("   III-ALTRI FONDI"),
	C("C-FONDO TFR"),
	D("D-DEBITI"),
	DI("   I-OBBLIGAZIONI"),
	DII("   II-OBBLIGAZIONI CONVERTIBILI"),
	DIII("   III-DEBITI VERSO BANCHE"),
	DIV("   IV-DEBITI VERSO ALTRI FINANZIATORI"),
	DV("   V-ACCONTI"),
	DVI("   VI-DEBITI VERSO FORNITORI"),
	DVII("   VII-DEBITI RAPPRESENTATI DA TITOLI DI CREDITO"),
	DVIII("   VIII-DEBITI VERSO IMPRESE CONTROLLATE,COLLEGATE O CONTROLLANTI"),
	DXI("   XI-DEBITI TRIBUTARI"),
	DXII("   XII-DEBITI VERSO INSTITUTI DI PREVIDENZA E SICUREZZA SOCIALE"),
	DXIII("   XIII-ALTRI DEBITI"),
	E("E-RATEI E RISCONTI PASSIVI");
	
	private String name;
	private VociBilancioPassivo(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
