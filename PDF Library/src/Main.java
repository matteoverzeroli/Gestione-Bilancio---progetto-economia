import java.io.FileNotFoundException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
	
	      // Creating a PdfWriter       
	      String dest = "C:\\Users\\matte\\Desktop\\Nuova cartella\\sample.pdf";       
	      PdfWriter writer = new PdfWriter(dest); 
	   
	      // Creating a PdfDocument       
	      PdfDocument pdfDoc = new PdfDocument(writer);              
	   
	      // Adding a new page 
	      pdfDoc.addNewPage();               
	   
	      // Creating a Document        
	      Document document = new Document(pdfDoc);               
	      
	      //Creating paragrapghs
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
	         		  
	  
	      
	      // Closing the document    
	      document.close();              
	      System.out.println("PDF Created");    
	   } 
	
}
