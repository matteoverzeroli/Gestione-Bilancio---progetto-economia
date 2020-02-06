package pdfcreator;
import java.io.FileNotFoundException;

import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;

import vocibilancio.VociBilancioAttivo;
import vocibilancio.VociBilancioContoEconomico;
import vocibilancio.VociBilancioPassivo;

public class PdfCreator {

	public static void main(String[] args) throws FileNotFoundException {

		// Creating a PdfWriter
		String destinazione = "generatedpdf\\sample.pdf";
		PdfWriter writer = new PdfWriter(destinazione);

		// Creating a PdfDocument
		PdfDocument pdfDoc = new PdfDocument(writer);

		// Adding a new page
		pdfDoc.addNewPage();

		// Creating a Document
		Document document = new Document(pdfDoc);

		// Creating paragraphs
		String para1 = "BILANCIO AZIENDA PIPPO 2020";

		Paragraph paragraph1 = new Paragraph(para1);

		// Adding paragraph
		document.add(paragraph1);

		titoliTabella1("ATTIVO",document);
		
		float[] pointColumnWidths = {400F, 123F};
		Table table = new Table(pointColumnWidths);
		
		for (VociBilancioAttivo voce : VociBilancioAttivo.values()) {
			titoliTabella2(voce.toString(),"1", table);
		}	
		
		document.add(table);
		
		document.add(new AreaBreak());

		titoliTabella1("PASSIVO",document);
		
		table = new Table(pointColumnWidths);
		
		for (VociBilancioPassivo voce : VociBilancioPassivo.values()) {
			titoliTabella2(voce.toString(),"1", table);
		}
		
		document.add(table);
		
		document.add(new AreaBreak());
		
		titoliTabella1("CONTO ECONOMICO",document);
		
		table = new Table(pointColumnWidths);
		
		for (VociBilancioContoEconomico voce : VociBilancioContoEconomico.values()) {
			titoliTabella2(voce.toString(),"1", table);
		}
		
		document.add(table);
		
		// Closing the document
		document.close();
		System.out.println("PDF Created");
	}
	
	private static void titoliTabella1(String cellcontent,Document document) {
		float[] pointColumnWidths = {523};
		Table table = new Table(pointColumnWidths);
		
		Cell cell = new Cell();
		cell.add(new Paragraph(cellcontent));
		cell.setBackgroundColor(new DeviceGray(0.75f));
		cell.setTextAlignment(TextAlignment.CENTER);
	
		table.addCell(cell);

		document.add(table);
	}
	
	private static void titoliTabella2(String cellcontent1,String cellcontent2,Table table) {
				
		Cell cell1 = new Cell();
		cell1.add(new Paragraph(cellcontent1));
		table.addCell(cell1.setWidth(50));
		
		Cell cell2 = new Cell();
		cell2.add(new Paragraph(cellcontent2));
	
		table.addCell(cell2);
	}

}
