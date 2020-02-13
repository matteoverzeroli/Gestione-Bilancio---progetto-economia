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
	BIA("      a)COSTI DI IMPIANTO E DI AMPLIAMENTO"),
	BIB("      b)DIRITTI DI BREVETTO INDUSTRIALE"),
	BIC("      c)CONCESSIONI,LICENZE,MARCHI E DIRITTI SIMILI"),
	BID("      d)AVVIAMENTO"),
	BIE("      e)IMMOBILIZZAZIONI IN CORSO E ACCONTI"),
	BIF("      f)ALTRE IMMOBILIZZAZIONI IMMATERIALI"),
	BII("   II-IMMOBILIZZAZIONI MATERIALI"),
	BIIA("      a)TERRENI E FABBRICATI"),
	BIIB("      b)IMPIANTI E MACCHINARI"),
	BIIC("      c)ATTREZZATURE INDUSTRIALI E COMMERCIALI"),
	BIID("      d)ALTRI BENI"),
	BIIE("      e)IMMOBILIZZAZIONI IN CORSO"),
	BIII("   III- IMMOBILIZZAZIONI FINANZIARIE"),
	BIIIA("      a)PARTECIPAZIONI AZIONARIE"),
	BIIIB("      b)CREDITI FINANZIARI"),
	BIIIC("      c)ALTRI TITOLI"),
	BIIID("      d)AZIONI PROPRIE"),
	C("C-ATTIVO CIRCOLANTE"),
	CI("   I-RIMANENZE"),
	CIA("      a)MATERIE PRIME"),
	CIB("      b)PRODOTTI IN CORSO DI LAVORAZIONE"),
	CIC("      c)LAVORI IN CORSO SU ORDINAZIONE"),
	CID("      d)PRODOTTI FINITI E MERCI"),
	CIE("      e)ACCONTI"),
	CII("   II-CREDITI"),
	CIIA("      a)VERSO CLIENTI"),
	CIIB("      b)VERSO IMPRESE CONTROLLATE"),
	CIIC("      c)VERSO ALTRI ENTI"),
	CIII("   III-ATTIVITÀ FINANZIARIE NON IMMOBILIZZATE"),
	CIIIA("      1)PARTECIPAZIONI IN IMPRESE CONTROLLATE"),
	CIIIB("      2)AZIONI PROPRIE"),
	CIIIC("      3)ALTRI TITOLI"),
	CIV("   IV-DISPONIBILITÀ LIQUIDE"),
	CIVA("      a)DEPOSITI BANCARI E POSTALI"),
	CIVB("      b)ASSEGNI"),
	CIVC("      c)DENARO E VALORI DI CASSA"),
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
