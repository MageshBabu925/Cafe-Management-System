package com.tl.serviceimpl;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tl.Constents.CafeConstants;
import com.tl.DAO.BillDao;
import com.tl.JWT.Jwtfilter;
import com.tl.POJO.Bill;
import com.tl.service.Billservice;
import com.tl.utils.CafeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Billserviceimpl implements Billservice {

    @Autowired
    Jwtfilter jfilter;

    @Autowired
    BillDao bdao;

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<String> generate(Map<String, Object> requestmap) {
        log.info("Inside generate report");
        try {
            String fileName;
            if (Validatemap(requestmap)) {
                if (requestmap.containsKey("isGenerate") && !(boolean) requestmap.get("isGenerate")) {
                    fileName = (String) requestmap.get("uuid");
                } else {
                    fileName = CafeUtils.getUUID();
                    requestmap.put("uuid", fileName);
                    insertbill(requestmap);
                }

                String data = "Name:" + requestmap.get("name") + "\n" +
                        "ContactNumber:" + requestmap.get("contactNumber") + "\n" +
                        "Email:" + requestmap.get("email") + "\n" +
                        "PaymentMethod:" + requestmap.get("paymentMethod") + "\n" +
                        "TotalAmount:" + requestmap.get("totalAmount");

                Document doc = new Document();
                PdfWriter.getInstance(doc, new FileOutputStream(CafeConstants.Stored_Location + "\\" + fileName + ".pdf"));
                doc.open();
                setRectangularInPdf(doc);

                Paragraph pgraph = new Paragraph("Cafe Management System", getfont("Header"));
                pgraph.setAlignment(Element.ALIGN_CENTER);
                doc.add(pgraph);

                Paragraph p = new Paragraph(data + "\n\n", getfont("Data"));
                doc.add(p);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addtableheader(table);

                Object productDetails = requestmap.get("productDetails");
                if (productDetails instanceof String) {
                    JSONArray jsonArray = CafeUtils.getJsonArray((String) productDetails);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addrows(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
                    }
                } else if (productDetails instanceof List) {
                    List<?> productList = (List<?>) productDetails;
                    for (Object obj : productList) {
                        if (obj instanceof Map) {
                            addrows(table, (Map<String, Object>) obj);
                        } else {
                            log.error("Unexpected type in productDetails list: " + obj.getClass().getName());
                        }
                    }
                } else {
                    log.error("Unexpected type for productDetails: " + productDetails.getClass().getName());
                }

                doc.add(table);

                Paragraph footer = new Paragraph("Total: " + requestmap.get("totalAmount") + "\n" +
                        "Thank you for visiting. Please Visit Again!!", getfont("Data"));
                footer.setAlignment(Element.ALIGN_CENTER);
                doc.add(footer);
                doc.close();

                String jsonResponse = String.format("{\"uuid\":\"%s\"}", fileName);
                return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

            }
            return CafeUtils.getResponseEntity("Required data not found", HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void insertbill(Map<String, Object> requestmap) {
        try {
            Bill bl = new Bill();
            bl.setUuid((String) requestmap.get("uuid"));
            bl.setName((String) requestmap.get("name"));
            bl.setEmail((String) requestmap.get("email"));
            bl.setContactNumber(Long.parseLong((String) requestmap.get("contactNumber")));
            bl.setPaymentMethod((String) requestmap.get("paymentMethod"));

            if (requestmap.get("totalAmount") instanceof Double) {
                bl.setTotalAmount(((Double) requestmap.get("totalAmount")).intValue());
            } else {
                bl.setTotalAmount(Integer.parseInt(requestmap.get("totalAmount").toString()));
            }

            Object productDetails = requestmap.get("productDetails");
            if (productDetails instanceof String) {
                bl.setProductDetails((String) productDetails);
            } else if (productDetails instanceof List) {
                bl.setProductDetails(new JSONArray((List<?>) productDetails).toString());
            } else {
                throw new IllegalArgumentException("Unexpected type for productDetails: " + productDetails.getClass().getName());
            }

            bl.setCreatedBy(jfilter.getCurrentUser());
            bdao.save(bl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRectangularInPdf(Document doc) throws DocumentException {
        log.info("Inside setRectangularInPdf");
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBackgroundColor(BaseColor.WHITE);
        rect.setBorderWidth(1);
        doc.add(rect);
    }

    private Font getfont(String type) {
        log.info("Inside getfont");
        switch (type) {
            case "Header":
                return FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 22, BaseColor.BLACK);
            case "Data":
                return FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, BaseColor.BLACK);
            default:
                return FontFactory.getFont(FontFactory.HELVETICA, 13, BaseColor.BLACK);
        }
    }

    private boolean Validatemap(Map<String, Object> requestmap) {
        return requestmap.containsKey("name") &&
                requestmap.containsKey("contactNumber") &&
                requestmap.containsKey("email") &&
                requestmap.containsKey("paymentMethod") &&
                requestmap.containsKey("productDetails") &&
                requestmap.containsKey("totalAmount");
    }

    private void addtableheader(PdfPTable table) {
        log.info("Inside addtableheader");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub_Total")
                .forEach(columntitle -> {
                    PdfPCell cell = new PdfPCell();
                    cell.setBackgroundColor(BaseColor.BLACK);
                    cell.setBorderWidth(2);
                    cell.setPhrase(new Phrase(columntitle));
                    cell.setBackgroundColor(BaseColor.YELLOW);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                });
    }

    private void addrows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addrows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell(data.get("quantity").toString());
        table.addCell(data.get("price").toString());
        table.addCell(data.get("total").toString());
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if (jfilter.isAdmin()) {
            list = bdao.getAllBills();
        } else {
            list = bdao.getBillByUser(jfilter.getCurrentUser());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestmap) {
        log.info("Inside getPdf requestmap: {}", requestmap);
        try {
            byte[] barray = new byte[0];
            if (!requestmap.containsKey("uuid") && Validatemap(requestmap))
                return new ResponseEntity<>(barray, HttpStatus.BAD_REQUEST);

            String filePath = CafeConstants.Stored_Location + "\\" + (String) requestmap.get("uuid") + ".pdf";
            if (CafeUtils.isFileExistornot(filePath)) {
                barray = getByteArray(filePath);
                return new ResponseEntity<>(barray, HttpStatus.OK);
            } else {
                requestmap.put("isGenerate", false);
                return new ResponseEntity<>(barray, HttpStatus.EXPECTATION_FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByteArray(String filePath) {
        try (InputStream inputStream = new FileInputStream(new File(filePath))) {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
	public ResponseEntity<String> deleteBillbyId(Integer id) {
		try {
			Optional<Bill> optional= bdao.findById(id);
			if(!optional.isEmpty()) {
				bdao.deleteById(id);
				return CafeUtils.getResponseEntity("Bill id deleted based on id", HttpStatus.OK);
			}
			return CafeUtils.getResponseEntity("Bill id does not exist", HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SomethingWentWrong, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
