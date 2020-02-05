import java.io.FileNotFoundException;

import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
	
	      // Creating a PdfWriter       
	      String dest = "C:\\Users\\matte\\Desktop\\sample.pdf";       
	      PdfWriter writer = new PdfWriter(dest); 
	   
	      // Creating a PdfDocument       
	      PdfDocument pdfDoc = new PdfDocument(writer);              
	   
	      // Adding a new page 
	      pdfDoc.addNewPage();               
	   
	      // Creating a Document        
	      Document document = new Document(pdfDoc);               
	      
	      //Creating paragraphs
	      String para1 = "ciao amine";
	      String para2 ="come stai ? ";
	      String para3 = "BILANCIO";
	      
	      Paragraph paragraph1 = new Paragraph(para1);
	      Paragraph paragraph2 = new Paragraph(para2);
	      Paragraph paragraph3 = new Paragraph(para3);

	      
	      //Adding paragraph
	      document.add(paragraph1);
	      document.add(paragraph2);
	      document.add(paragraph3);

	      
	      //Adding list
	      Paragraph paragraph = new Paragraph("Make a list");
	      
	      List list = new List();
	      list.add("Mara");
	      list.add("Malighetti");
	      
	      document.add(list);
	      
	      //Create table
	      
	      float[] pointColumnWidths = {150F,150F,150F};
	      Table table = new Table(pointColumnWidths);	      
	      Cell cell = new Cell(1, 3).add(new Paragraph("This is a header"));
	               
	        table.addHeaderCell(cell);
	        for (int i = 0; i < 2; i++) {
	            Cell[] headerFooter = new Cell[]{
	                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("#")),
	                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Key")),
	                    new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Value"))
	            };
	            for (Cell hfCell : headerFooter) {
	                if (i == 0) {
	                    table.addHeaderCell(hfCell);
	                } else {
	                    table.addFooterCell(hfCell);
	                }
	            }
	        }
	        for (int counter = 1; counter < 101; counter++) {
	            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(counter))));
	            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("key " + counter)));
	            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("value " + counter)));
	        }
	        document.add(table);
	        document.close();
	        
	      // Closing the document   
	      document.close();              
	      System.out.println("PDF Created");    
	   }

}
