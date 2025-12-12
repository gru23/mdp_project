package org.unibl.etf.invoice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.unibl.etf.appointment.enums.AppointmentType;
import org.unibl.etf.client.AccountStatus;
import org.unibl.etf.client.Client;
import org.unibl.etf.parts.PartEntity;
import org.unibl.etf.util.EmailSender;
import org.unibl.etf.vehicle.VehicleEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class InvoiceService {
	private Invoice invoice;
	
	public InvoiceService() {
		super();
	}

	public static void main(String[] args) {
		PartEntity p1 = new PartEntity();
		p1.setCode("BRK-001");
		p1.setName("Brake pads");
		p1.setManufacturer("Bosch");
		p1.setPrice(49.99);
		p1.setQuantity(1);
		p1.setDescription("Front brake pads set");

		PartEntity p2 = new PartEntity();
		p2.setCode("OIL-023");
		p2.setName("Engine oil 5W-30");
		p2.setManufacturer("Castrol");
		p2.setPrice(12.50);
		p2.setQuantity(5);
		p2.setDescription("Premium long-life engine oil");

		Client client = new Client();
		client.setId("CL-1001");
		client.setFirstName("Marko");
		client.setLastName("Marković");
		client.setUsername("marko123");
		client.setAddress("Bulevar Cara Dušana 12, Banja Luka");
		client.setPhoneNumber("+38765123456");
		client.setEmail("grujo23@gmail.com");
		client.setStatus(AccountStatus.APPROVED);

		VehicleEntity vehicle = new VehicleEntity();
		vehicle.setId("VH-2001");
		vehicle.setManufacturer("BMW");
		vehicle.setModel("320d");
		vehicle.setYear(2010);
		vehicle.setRegistrationPlate("BL-123-AB");
		vehicle.setClientId(client.getId());

		ArrayList<PartEntity> parts = new ArrayList<>();
		parts.add(p1);
		parts.add(p2);

		Invoice invoice = new Invoice(
		        parts,
		        AppointmentType.REGULAR_SERVICE,
		        client,
		        vehicle
		);

//		InvoicePDFGenerator generator = new InvoicePDFGenerator(invoice);
//		generator.generateInvoice("C:\\Users\\Administrator\\Desktop\\invoice_test.pdf");
	

		new InvoiceService().writeAndEmailInvoice(invoice);
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
        
//        String dir = "C:\\Users\\Administrator\\Desktop\\invoice";

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
