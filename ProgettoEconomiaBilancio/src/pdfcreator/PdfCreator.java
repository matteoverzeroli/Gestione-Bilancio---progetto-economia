package pdfcreator;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import com.itextpdf.layout.property.TextAlignment;

import home.Globs;
import home.ProgettoEconomiaBilancio;
import vocibilancio.VociBilancioAttivo;
import vocibilancio.VociBilancioContoEconomico;
import vocibilancio.VociBilancioPassivo;

public class PdfCreator {
	private static ProgettoEconomiaBilancio homewindow;

	public static void creaPdfBilancio() {
		homewindow = Globs.getHomeWindow();

		if (homewindow.getAziendaSelected() != null && homewindow.getBilancioSelected() != -1) {
			try {
				// Creating a PdfWriter
				String destinazione = "generatedpdf\\" + homewindow.getAziendaSelected() + "_"
						+ homewindow.getBilancioSelected() + ".pdf";
				PdfWriter writer = new PdfWriter(destinazione);

				// Creating a PdfDocument
				PdfDocument pdfDoc = new PdfDocument(writer);

				// Adding a new page
				pdfDoc.addNewPage();

				// Creating a Document
				Document document = new Document(pdfDoc);

				// Creating paragraphs
				String para1 = "BILANCIO AZIENDA " + homewindow.getAziendaSelected() + " "
						+ homewindow.getBilancioSelected();

				Paragraph paragraph1 = new Paragraph(para1);

				// Adding paragraph
				document.add(paragraph1);

				titoliTabella1("ATTIVO", document);

				float[] pointColumnWidths = { 400F, 123F }; // massimo somma 523f
				Table tableattivo = new Table(pointColumnWidths);
				Table tablepassivo = new Table(pointColumnWidths);
				Table tablecontoeconomico = new Table(pointColumnWidths);

				ArrayList<String> voce = new ArrayList<String>();
				ArrayList<String> attivo = new ArrayList<String>();
				ArrayList<Double> euro = new ArrayList<Double>();
				ArrayList<String> dareavere = new ArrayList<String>();
				// TODO le note?

				try (Connection conn = Globs.connect()) {
					int idAzienda = 0;
					int idBilancio = 0;
					Statement stmt = conn.createStatement();
					String qry1 = "SELECT id FROM Aziende WHERE Nome = '" + homewindow.getAziendaSelected() + "';";
					ResultSet rs1 = stmt.executeQuery(qry1);
					if (rs1.next()) {
						idAzienda = rs1.getInt("id");
					}
					String qry2 = "SELECT Reference FROM Bilanci WHERE id = " + idAzienda + " AND Anno = "
							+ String.valueOf(homewindow.getBilancioSelected()) + ";";
					ResultSet rs2 = stmt.executeQuery(qry2);
					if (rs2.next()) {
						idBilancio = rs2.getInt("Reference");
					}

					String query = "SELECT Voce, Euro, InOut, Attivo, Note FROM Mastrini WHERE id = " + idBilancio
							+ ";";
					ResultSet rs = stmt.executeQuery(query);

					while (rs.next()) {
						voce.add(rs.getString("Voce"));
						attivo.add(rs.getString("Attivo"));
						euro.add(rs.getDouble("Euro"));
						dareavere.add(rs.getString("InOut"));
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}

				sommaMastriniConUgualeVoce(voce, attivo, euro, dareavere, tableattivo, tablepassivo,
						tablecontoeconomico);

				document.add(tableattivo);
				titoliTabella3("TOTALE ATTIVO", String.valueOf(RisultatiMastrini.getTotaleAttivo()), document);

				document.add(new AreaBreak());
				titoliTabella1("PASSIVO", document);
				document.add(tablepassivo);
				titoliTabella3("TOTALE PASSIVO", String.valueOf(RisultatiMastrini.getTotalePassivo()), document);

				document.add(new AreaBreak());
				titoliTabella1("CONTO ECONOMICO", document);
				document.add(tablecontoeconomico);

				// Closing the document
				document.close();
				JFrame frame = new JFrame("Show Message Box");
				JOptionPane.showMessageDialog(frame, homewindow.getAziendaSelected() + "_"
						+ homewindow.getBilancioSelected() + ".pdf" + " salvato!!!", "Informazione",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (FileNotFoundException e) {
				// TODO: handle exception
			}

			resetAllRisultatoMastrini();
		}
	}

	/**
	 * @author Matteo
	 * 
	 *         Aggiunge titoli alle tabelle
	 * @param cellcontent
	 * @param document
	 */

	private static void titoliTabella1(String cellcontent, Document document) {
		float[] pointColumnWidths = { 523 };
		Table table = new Table(pointColumnWidths);

		Cell cell = new Cell();
		cell.add(new Paragraph(cellcontent));
		cell.setBackgroundColor(new DeviceGray(0.75f));
		cell.setTextAlignment(TextAlignment.CENTER);

		table.addCell(cell);

		document.add(table);
	}

	/**
	 * Aggiunge alla tabella le celle contente i mastrini
	 * 
	 * @param cellcontent1
	 * @param cellcontent2
	 * @param table
	 */

	private static void titoliTabella2(String cellcontent1, String cellcontent2, Table table) {

		Cell cell1 = new Cell();
		cell1.add(new Paragraph(cellcontent1));

		table.addCell(cell1);

		Cell cell2 = new Cell();
		cell2.add(new Paragraph(cellcontent2));

		table.addCell(cell2);
	}

	/**
	 * Aggiunge alla tabella le celle contente il totale mastrini
	 * 
	 * @param cellcontent1
	 * @param cellcontent2
	 * @param table
	 */

	private static void titoliTabella3(String cellcontent1, String cellcontent2, Document document) {
		float[] pointColumnWidths = { 400F, 123F };
		Table table = new Table(pointColumnWidths);

		Cell cell1 = new Cell();
		cell1.add(new Paragraph(cellcontent1));
		cell1.setBackgroundColor(new DeviceGray(0.75f));
		table.addCell(cell1);

		Cell cell2 = new Cell();
		cell2.add(new Paragraph(cellcontent2));

		table.addCell(cell2);

		document.add(table);
	}

	/**
	 * Aggiunge alla tabella le celle contente i mastrini
	 * 
	 * @param cellcontent1
	 * @param cellcontent2
	 * @param table
	 */

	private static void titoliTabella4(String cellcontent1, String cellcontent2, Table table) {
		Cell cell1 = new Cell();
		cell1.add(new Paragraph(cellcontent1));
		cell1.setBackgroundColor(new ColorConstants().CYAN);

		table.addCell(cell1);

		Cell cell2 = new Cell();
		cell2.add(new Paragraph(cellcontent2));
		cell2.setBackgroundColor(new ColorConstants().CYAN);

		table.addCell(cell2);
	}

	/**
	 * somma i mastrini sotto la stessa voce tenendo conto se sono in dare o avere
	 */

	private static void sommaMastriniConUgualeVoce(ArrayList<String> voce, ArrayList<String> attivo,
			ArrayList<Double> euro, ArrayList<String> dareavere, Table tableattivo, Table tablepassivo,
			Table tablecontoeconomico) {
		int[] indicimastriniattivo = new int[voce.size()];
		resetIndici(indicimastriniattivo);
		int[] indicimastrinipassivo = new int[voce.size()];
		resetIndici(indicimastrinipassivo);

		int[] indicimastrinicontoeconomico = new int[voce.size()];
		resetIndici(indicimastrinicontoeconomico);

		for (int i = 0; i < attivo.size(); i++) {
			if (attivo.get(i).compareTo("Attivo") == 0) {
				indicimastriniattivo[i] = 1;

			} else if (attivo.get(i).compareTo("Passivo") == 0) {
				indicimastrinipassivo[i] = 1;
			} else {
				indicimastrinicontoeconomico[i] = 1;
			}
		}

		boolean flag = false; // variabile flag per tenere in considerazione solo che voci del quali si sono
								// inseriti mastrini
		double totalemastrini = 0;

		for (VociBilancioAttivo vociattivo : VociBilancioAttivo.values()) {
			double sommamastrino = 0;
			for (int i = 0; i < attivo.size(); i++) {
				if (indicimastriniattivo[i] == 1) {
					if (voce.get(i).compareTo(vociattivo.toString()) == 0) {
						flag = true;
						if (dareavere.get(i).compareTo("Dare") == 0) {
							sommamastrino += euro.get(i);
						} else {
							sommamastrino -= euro.get(i);
						}
					}
				}
			}
			if (flag) {
				titoliTabella2(vociattivo.toString(), String.valueOf(sommamastrino), tableattivo);
				totalemastrini += sommamastrino;
			}
			flag = false;

		}

		RisultatiMastrini.setTotaleAttivo(totalemastrini);

		/**
		 * ATTENZIONE DA
		 * CONTROLLAREEEEEEEEEEEEEEEEEEEEEEEEE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */
		totalemastrini = 0;
		int partecontoeconomicochange = 0; // variabile flag per vedere in che parte del conto economico ci troviamo
		int partecontoeconomicochangepre = 0;
		for (VociBilancioContoEconomico vocicontoeconomico : VociBilancioContoEconomico.values()) {
			double sommamastrino = 0;
			for (int i = 0; i < attivo.size(); i++) {
				if (indicimastrinicontoeconomico[i] == 1) {
					if (voce.get(i).compareTo(vocicontoeconomico.toString()) == 0) {
						flag = true;
						// se voce di bilancio è nei punti A
						if (vocicontoeconomico.compareTo(VociBilancioContoEconomico.B) < 0) {
							partecontoeconomicochange = 0;
							if (dareavere.get(i).compareTo("Dare") == 0) {
								sommamastrino -= euro.get(i);

								RisultatiMastrini
										.setValoreProduzione(RisultatiMastrini.getValoreProduzione() - euro.get(i));
							} else {
								sommamastrino += euro.get(i);
								RisultatiMastrini
										.setValoreProduzione(RisultatiMastrini.getValoreProduzione() + euro.get(i));
							}
						} else if (vocicontoeconomico.compareTo(VociBilancioContoEconomico.B) >= 0
								&& vocicontoeconomico.compareTo(VociBilancioContoEconomico.C) < 0) {
							partecontoeconomicochange = 1;
							if (dareavere.get(i).compareTo("Dare") == 0) {
								sommamastrino += euro.get(i);
								RisultatiMastrini
										.setCostiProduzione(RisultatiMastrini.getCostiProduzione() + euro.get(i));

							} else {
								sommamastrino -= euro.get(i);
								RisultatiMastrini
										.setCostiProduzione(RisultatiMastrini.getCostiProduzione() - euro.get(i));
							}

						} else if (vocicontoeconomico.compareTo(VociBilancioContoEconomico.C) >= 0
								&& vocicontoeconomico.compareTo(VociBilancioContoEconomico.D) < 0) {
							partecontoeconomicochange = 2;

							if (dareavere.get(i).compareTo("Dare") == 0) {
								sommamastrino -= euro.get(i);
								RisultatiMastrini.setTotaliProventiEOneriFinanziari(
										RisultatiMastrini.getTotalEProventiEOneriFinanziari() - euro.get(i));

							} else {
								sommamastrino += euro.get(i);

								RisultatiMastrini.setTotaliProventiEOneriFinanziari(
										RisultatiMastrini.getTotalEProventiEOneriFinanziari() + euro.get(i));
							}

						} else {
							partecontoeconomicochange = 3;
							if (dareavere.get(i).compareTo("Dare") == 0) {
								sommamastrino -= euro.get(i);
								RisultatiMastrini.setTotaliRettificheDiValoreEDiAttivitaFinanziarie(
										RisultatiMastrini.getTotaliRettificheDiValoreEDiAttivitaFinanziarie()
												- euro.get(i));

							} else {
								sommamastrino += euro.get(i);

								RisultatiMastrini.setTotaliRettificheDiValoreEDiAttivitaFinanziarie(
										RisultatiMastrini.getTotaliRettificheDiValoreEDiAttivitaFinanziarie()
												+ euro.get(i));

							}
						}
					}
				}
			}

			if (partecontoeconomicochange != partecontoeconomicochangepre) {
				aggiungiSubTotaleAllaTabella(partecontoeconomicochange, tablecontoeconomico);
			}

			if (flag) {
				titoliTabella2(vocicontoeconomico.toString(), String.valueOf(sommamastrino), tablecontoeconomico);
			}

			flag = false;
			partecontoeconomicochangepre = partecontoeconomicochange;
		}
		// se non ci sono voci successivi al subtotale attuale

		while (partecontoeconomicochange < 4) {
			partecontoeconomicochange++;
			aggiungiSubTotaleAllaTabella(partecontoeconomicochange, tablecontoeconomico);
		}

		totalemastrini = 0;

		for (VociBilancioPassivo vocipassivo : VociBilancioPassivo.values()) {
			double sommamastrino = 0;

			if ("   IX-UTILI DELL'ESERCIZIO".compareTo(vocipassivo.toString()) == 0) {
				titoliTabella2(vocipassivo.toString(), String.valueOf(RisultatiMastrini.getUtile()), tablepassivo);
				totalemastrini += RisultatiMastrini.getUtile();
				flag = false;
			}
			for (int i = 0; i < attivo.size(); i++) {
				if (indicimastrinipassivo[i] == 1) {
					if (voce.get(i).compareTo(vocipassivo.toString()) == 0) {
						flag = true;

						if (dareavere.get(i).compareTo("Dare") == 0) {
							sommamastrino -= euro.get(i);
						} else {
							sommamastrino += euro.get(i);
						}
					}
				}
			}
			if (flag) {
				titoliTabella2(vocipassivo.toString(), String.valueOf(sommamastrino), tablepassivo);
				totalemastrini += sommamastrino;
			}
			flag = false;

		}

		RisultatiMastrini.setTotalePassivo(totalemastrini);
	}

	private static void resetIndici(int[] indici) {
		for (int i = 0; i < indici.length; i++) {
			indici[i] = 0;
		}
	}

	private static void resetAllRisultatoMastrini() {
		RisultatiMastrini.setTotaleAttivo(0);
		RisultatiMastrini.setTotalePassivo(0);
		RisultatiMastrini.setValoreProduzione(0);
		RisultatiMastrini.setCostiProduzione(0);
		RisultatiMastrini.setTotaliProventiEOneriFinanziari(0);
		RisultatiMastrini.setTotaliRettificheDiValoreEDiAttivitaFinanziarie(0);
	}

	private static void aggiungiSubTotaleAllaTabella(int partecontoeconomicochange, Table tablecontoeconomico) {
		switch (partecontoeconomicochange) {
		case 1:
			titoliTabella4("VALORE PRODUZIONE", String.valueOf(RisultatiMastrini.getValoreProduzione()),
					tablecontoeconomico);
			break;
		case 2:
			titoliTabella4("COSTI PRODUZIONE", String.valueOf(RisultatiMastrini.getCostiProduzione()),
					tablecontoeconomico);
			titoliTabella4("EBIT", String.valueOf(RisultatiMastrini.getEBIT()), tablecontoeconomico);
			break;
		case 3:
			titoliTabella4("TOTALE PROVENTI E ONERI FINANZIARI",
					String.valueOf(RisultatiMastrini.getTotalEProventiEOneriFinanziari()), tablecontoeconomico);
			break;
		case 4:
			titoliTabella4("TOTALE RETTIFICHE DI VALORE DI ATTIVITA' FINANZIARIE",
					String.valueOf(RisultatiMastrini.getTotaliRettificheDiValoreEDiAttivitaFinanziarie()),
					tablecontoeconomico);
			titoliTabella4("UTILE ANTE IMPOSTE", String.valueOf(RisultatiMastrini.getUtileAnteImposte()),
					tablecontoeconomico);
			titoliTabella4("TOTALE IMPOSTE", String.valueOf(RisultatiMastrini.getTotaleImposte()), tablecontoeconomico);
			titoliTabella4("UTILE ESERCIZIO", String.valueOf(RisultatiMastrini.getUtile()), tablecontoeconomico);
			break;
		default:
			;
		}
	}

}
