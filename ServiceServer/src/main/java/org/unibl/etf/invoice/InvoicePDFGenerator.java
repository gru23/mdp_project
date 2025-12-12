package org.unibl.etf.invoice;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

import java.awt.Color;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import org.unibl.etf.appointment.enums.AppointmentType;
import org.unibl.etf.client.Client;
import org.unibl.etf.parts.PartEntity;
import org.unibl.etf.vehicle.VehicleEntity;

public class InvoicePDFGenerator {

    private ArrayList<PartEntity> changedParts;
    private AppointmentType typeOfService;
    private Client client;
    private VehicleEntity vehicle;

    private static class FooterHandler extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfPTable footer = new PdfPTable(1);
                footer.setTotalWidth(500);
                footer.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell = new PdfPCell(new Phrase(
                        "Thank you for choosing MDP Service!",
                        FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10)
                ));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                footer.addCell(cell);

                footer.writeSelectedRows(0, -1, 
                        (document.right() - document.left() - 500) / 2 + document.leftMargin(),
                        document.bottomMargin() - 5,
                        writer.getDirectContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public InvoicePDFGenerator() {}

    public InvoicePDFGenerator(Invoice invoice) {
        this.changedParts = invoice.getChangedParts();
        this.typeOfService = invoice.getTypeOfService();
        this.client = invoice.getClient();
        this.vehicle = invoice.getVehicle();
    }

    public void generateInvoice(String path) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

            writer.setPageEvent(new FooterHandler());

            document.open();

            // TITLE
            Paragraph title = new Paragraph(
                    "MDP SERVICE",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24)
            );
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph(
                    "Vehicle service invoice",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)
            );
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            // Separator
            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // CLIENT INFO
            document.add(new Paragraph(client.getFirstName() + " " + client.getLastName()));
            document.add(new Paragraph(client.getAddress()));
            document.add(new Paragraph(client.getEmail()));
            document.add(new Paragraph(client.getPhoneNumber()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Vehicle information:"));
            document.add(new Paragraph(vehicle.getManufacturer() + ", " + vehicle.getModel()));
            document.add(new Paragraph("Year production: " + vehicle.getYear()));
            document.add(new Paragraph("Type of service: " + typeOfService));
            document.add(new Paragraph(" "));

            // TABLE
            if (changedParts == null || changedParts.isEmpty()) {
                document.add(new Paragraph("No parts were changed during service."));
                document.close();
                return;
            }

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2f, 4f, 3f, 2f, 2f, 2f});

            // Header row
            String[] headers = {"Code", "Name", "Manufacturer", "Price (€)", "Quantity", "Total (€)"};
            for (String h : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(
                        h,
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)
                ));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(Color.LIGHT_GRAY);
                table.addCell(headerCell);
            }

            // Data rows
            for (PartEntity p : changedParts) {
                table.addCell(centerCell(p.getCode()));
                table.addCell(centerCell(p.getName()));
                table.addCell(centerCell(p.getManufacturer()));
                table.addCell(centerCell(String.format("%.2f", p.getPrice())));
                table.addCell(centerCell(String.valueOf(p.getQuantity())));
                double sum = p.getPrice() * p.getQuantity();
                table.addCell(centerCell(String.format("%.2f", sum)));
            }

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total: " + String.format("%.2f", totalSum()) + " €"));
            document.add(new Paragraph(" "));

            // DATE
            document.add(new Paragraph(
                    LocalDate.now().format(
                            DateTimeFormatter.ofPattern("dd. MMM yyyy.", Locale.ENGLISH)
                    )
            ));

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell centerCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private double totalSum() {
        return changedParts
                .stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
    }
}
