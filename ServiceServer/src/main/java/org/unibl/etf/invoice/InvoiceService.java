package org.unibl.etf.invoice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.unibl.etf.util.EmailSender;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InvoiceService {
	private Invoice invoice;
	
	public InvoiceService() {
		super();
	}
	
	public void writeAndEmailInvoice(Invoice invoice) {
        this.invoice = invoice;

        String pdfPath = getInvoicePdfPath();
        String zipPath = pdfPath.replace(".pdf", ".zip");

        InvoicePDFGenerator generator = new InvoicePDFGenerator(invoice);
        generator.generateInvoice(pdfPath);
        serializeInvoice(pdfPath);

        zipFile(pdfPath, zipPath);
        
        EmailSender.sendEmail(
        		invoice.getClient().getEmail(), 
        		"Invoice from MDP Service", 
        		"Dear " + invoice.getClient().getFirstName() + ",\r\n"
        		+ "We are sending you this invoice for servicing your vehicle " + 
        				invoice.getVehicle().getManufacturer() + ", " + invoice.getVehicle().getModel()
        				+ " at MDP service.", 
        		zipPath
        );

        System.out.println("Generated PDF: " + pdfPath);
        System.out.println("Generated ZIP: " + zipPath);
    }	
	
	private void serializeInvoice(String path) {
		String jsonPath = path.replace(".pdf", ".json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try (FileWriter writer = new FileWriter(jsonPath)) {
	        gson.toJson(this.invoice, writer);
	        System.out.println("Invoice serijalizovan u JSON: " + jsonPath);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void zipFile(String sourceFilePath, String zipPath) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath));
             FileInputStream fis = new FileInputStream(sourceFilePath)) {

            ZipEntry zipEntry = new ZipEntry(new File(sourceFilePath).getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private String getInvoicePdfPath() {
        String base = System.getProperty("catalina.base");

        String dir = base + File.separator + "wtpwebapps"
                + File.separator + "ServiceServer"
                + File.separator + "WEB-INF"
                + File.separator + "invoice";

        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }

        return dir + File.separator + generateInvoiceFileName();
    }
	
	private String generateInvoiceFileName() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        return invoice.getClient().getFirstName() + "_"
                + invoice.getClient().getLastName() + "_"
                + timestamp + ".pdf";
    }
}
