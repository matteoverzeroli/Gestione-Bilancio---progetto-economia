package pdfcreator;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
				document.add(new AreaBreak());
				titoliTabella1("PASSIVO", document);
				document.add(tablepassivo);
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
		table.addCell(cell1.setWidth(50));

		Cell cell2 = new Cell();
		cell2.add(new Paragraph(cellcontent2));

		table.addCell(cell2);
	}

	/**
	 * somma i mastrini sotto la stessa voce tenendo conto se sono in dare o avere
	 */

	private static void sommaMastriniConUgualeVoce(ArrayList<String> voce, ArrayList<String> attivo,
			ArrayList<Double> euro, ArrayList<String> dareavere, Table tableattivo, Table tablepassivo,
			Table tablecontoeconoico) {
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

		boolean flag = false;

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
			if (flag)
				titoliTabella2(vociattivo.toString(), String.valueOf(sommamastrino), tableattivo);
			flag = false;

		}

		for (VociBilancioPassivo vocipassivo : VociBilancioPassivo.values()) {
			double sommamastrino = 0;
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
			if (flag)
				titoliTabella2(vocipassivo.toString(), String.valueOf(sommamastrino), tablepassivo);
			flag = false;

		}
		/**
		 * ATTENZIONE DA
		 * CONTROLLAREEEEEEEEEEEEEEEEEEEEEEEEE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */

		for (VociBilancioContoEconomico vocicontoeconomico : VociBilancioContoEconomico.values()) {
			double sommamastrino = 0;
			for (int i = 0; i < attivo.size(); i++) {
				if (indicimastrinicontoeconomico[i] == 1) {
					if (voce.get(i).compareTo(vocicontoeconomico.toString()) == 0) {
						flag = true;
						if (dareavere.get(i).compareTo("Dare") == 0) {
							sommamastrino -= euro.get(i);
						} else {
							sommamastrino += euro.get(i);
						}
					}
				}
			}
			if (flag)
				titoliTabella2(vocicontoeconomico.toString(), String.valueOf(sommamastrino), tablecontoeconoico);
			flag = false;

		}
	}

	private static void resetIndici(int[] indici) {
		for (int i = 0; i < indici.length; i++) {
			indici[i] = 0;
		}
	}

}
