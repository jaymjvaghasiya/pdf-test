package com.controller;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bean.UserBean;
import com.dao.UserDao;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.font.FontSet;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class UserController {

	private static final String LICENSE_FILE = "F:\\2.Royal\\Advance JAVA\\pdf_test\\src\\main\\resources\\itextkey.json";
	private static final String FONTS_DIR = "F:\\2.Royal\\Advance JAVA\\pdf_test\\src\\main\\resources\\fonts\\";
	private static final String DEST = "output/multilingual_document.pdf";
	private static final String XML_DIR = "F:/2.Royal/Advance JAVA/pdf_test/src/main/webapp/MyReports/";
	private static final String IMAGES_DIR = "F:/2.Royal/Advance JAVA/pdf_test/src/main/webapp/images/";
    
	
	private void loadLicense() {
		try {
			File licenseFile = new File(LICENSE_FILE);
			System.out.println("licenseFile.exists() : " + licenseFile.exists());
			if (licenseFile.exists()) {
				// Method 1: Load from JSON file
				System.out.println("loading from file");
				com.itextpdf.licensing.base.LicenseKey.loadLicenseFile(licenseFile);
				System.out.println("License loaded successfully from: " + LICENSE_FILE);
			}
		} catch (Exception e) {
			System.err.println("Failed to load license: " + e.getMessage());
			System.err.println("Running in unlicensed mode - watermarks will appear");
		}

	}

	public void getAllUsersData(HttpServletRequest request) {
		ResultSet rsLocal = new UserDao().displayAllRecord2();

		String xmlReportPath = request.getServletContext().getRealPath("/MyReports/demo.jrxml");
		String outputPath = "F:\\2.Royal\\Advance JAVA\\pdf_test\\src\\main\\webapp\\MyReports\\users_report.pdf";

		loadLicense();

		JRResultSetDataSource dataSource = new JRResultSetDataSource(rsLocal);
		JasperPrint print = null;
		try {

			JasperReport jasperReport = JasperCompileManager.compileReport(xmlReportPath);

			String compiledReportPath = request.getServletContext().getRealPath("/MyReports/demo.jasper");
			JasperCompileManager.compileReportToFile(xmlReportPath, compiledReportPath);

			print = JasperFillManager.fillReport(compiledReportPath, null, dataSource);
			JasperExportManager.exportReportToPdfFile(print, outputPath);
			System.out.println("PDF report saved successfully at: " + outputPath);

		} catch (JRException e) {
			e.printStackTrace();
		}

	}

	public void createPdf() throws Exception {
		ArrayList<UserBean> users = new UserDao().displayAllRecord();

		loadLicense();

		new File("output").mkdirs();

		String demoText = "મારું નામ જય વઘાસીયા છે.";
		String demoText2 = "હેલ્લો દોસ્તો કેમ છો";

		final PdfWriter writer = new PdfWriter(DEST);
		final PdfDocument pdfDocument = new PdfDocument(writer);
		final Document document = new Document(pdfDocument);

		final FontSet set = new FontSet();

		set.addFont(FONTS_DIR + "NotoNaskhArabic-Regular.ttf");
		set.addFont(FONTS_DIR + "NotoSansTamil-Regular.ttf");
		set.addFont(FONTS_DIR + "NotoSansDevanagari-Regular.ttf");
		set.addFont(FONTS_DIR + "NotoSansGujarati-Regular.ttf");
		set.addFont(FONTS_DIR + "FreeSans.ttf");
		set.addFont(FONTS_DIR + "arial.ttf");

		document.setFontProvider(new FontProvider(set));
		document.setProperty(Property.FONT, new String[] { "MyFontFamilyName" });

		// Add title
		Paragraph title = new Paragraph("Users List").setFontSize(18).setTextAlignment(TextAlignment.CENTER)
				.setMarginBottom(20);
		document.add(title);

		Table table = new Table(UnitValue.createPercentArray(new float[] { 33.33f, 33.33f, 33.33f }))
				.useAllAvailableWidth();

		// Add table headers
		table.addHeaderCell(new Cell().add(new Paragraph("Name (English)")));
		table.addHeaderCell(new Cell().add(new Paragraph("Name (Hindi)")));
		table.addHeaderCell(new Cell().add(new Paragraph("Name (Gujarati)")));

		for (UserBean user : users) {
			Cell nameCell = new Cell().add(new Paragraph(user.getName() != null ? user.getName() : ""));
			table.addCell(nameCell);

			Cell nameHinCell = new Cell().add(new Paragraph(user.getName_hin() != null ? user.getName_hin() : ""));
			table.addCell(nameHinCell);

			Cell nameGujCell = new Cell().add(new Paragraph(user.getName_guj() != null ? user.getName_guj() : ""));
			table.addCell(nameGujCell);
		}

		document.add(table);

		Paragraph footer = new Paragraph("Total Users: " + users.size()).setTextAlignment(TextAlignment.RIGHT)
				.setMarginTop(20).setFontSize(10);
		document.add(footer);

		document.close();
		pdfDocument.close();

	}
	
	public void createPdf2() throws Exception {
	    ArrayList<UserBean> users = new UserDao().displayAllRecord();
	    
	    loadLicense();
	    
	    new File("output").mkdirs();
	    
	    final String[] sources = { "demo.jrxml" }; // Your JasperReports XML file
	    
	    final PdfWriter writer = new PdfWriter(DEST);
	    final PdfDocument pdfDocument = new PdfDocument(writer);
	    final Document document = new Document(pdfDocument);
	    
	    final FontSet set = new FontSet();
	    set.addFont(FONTS_DIR + "NotoNaskhArabic-Regular.ttf");
	    set.addFont(FONTS_DIR + "NotoSansTamil-Regular.ttf");
	    set.addFont(FONTS_DIR + "NotoSansDevanagari-Regular.ttf");
	    set.addFont(FONTS_DIR + "NotoSansGujarati-Regular.ttf");
	    set.addFont(FONTS_DIR + "FreeSans.ttf");
	    set.addFont(FONTS_DIR + "arial.ttf");
	    
	    document.setFontProvider(new FontProvider(set));
	    document.setProperty(Property.FONT, new String[]{"MyFontFamilyName"});
	    
	    // Parse JasperReports XML and extract title
	    for (final String source : sources) {
	        final File xmlFile = new File(XML_DIR + source);
	        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        final DocumentBuilder builder = factory.newDocumentBuilder();
	        final org.w3c.dom.Document doc = builder.parse(xmlFile);
	        
	        // Extract title from XML
	        final NodeList titleTextNodes = doc.getElementsByTagName("text");
	        for (int i = 0; i < titleTextNodes.getLength(); i++) {
	            Node textNode = titleTextNodes.item(i);
	            // Check if this text node is in the title section
	            Node parent = textNode.getParentNode();
	            while (parent != null) {
	                if (parent.getNodeName().equals("title")) {
	                    final Paragraph titleParagraph = new Paragraph();
	                    titleParagraph.add(textNode.getTextContent())
	                        .setFontSize(20)
	                        .setTextAlignment(TextAlignment.CENTER)
	                        .setMarginBottom(20);
	                    document.add(titleParagraph);
	                    break;
	                }
	                parent = parent.getParentNode();
	            }
	        }
	    }
	    
	    // Create user data table
	    Table table = new Table(UnitValue.createPercentArray(new float[]{20f, 27f, 27f, 26f}))
	        .useAllAvailableWidth();
	    
	    // Add headers
	    table.addHeaderCell(new Cell().add(new Paragraph("ID")));
	    table.addHeaderCell(new Cell().add(new Paragraph("Name")));
	    table.addHeaderCell(new Cell().add(new Paragraph("नाम (हिंदी)")));
	    table.addHeaderCell(new Cell().add(new Paragraph("નામ (ગુજરાતી)")));
	    
	    // Add user data
	    for (UserBean user : users) {
	        table.addCell(new Cell().add(new Paragraph(user.getId() != null ? user.getId().toString() : "")));
	        table.addCell(new Cell().add(new Paragraph(user.getName() != null ? user.getName() : "")));
	        table.addCell(new Cell().add(new Paragraph(user.getName_hin() != null ? user.getName_hin() : "")));
	        table.addCell(new Cell().add(new Paragraph(user.getName_guj() != null ? user.getName_guj() : "")));
	    }
	    
	    document.add(table);
	    
	    // Add footer
	    Paragraph footer = new Paragraph("Total Users: " + users.size())
	        .setTextAlignment(TextAlignment.RIGHT)
	        .setMarginTop(20);
	    document.add(footer);
	    
	    document.close();
	    pdfDocument.close();
	}
	
	public void createPdf3() throws Exception {
	    ArrayList<UserBean> users = new UserDao().displayAllRecord();
	    
	    loadLicense();
	    
	    new File("output").mkdirs();
	    
	    final PdfWriter writer = new PdfWriter(DEST);
	    final PdfDocument pdfDocument = new PdfDocument(writer);
	    final Document document = new Document(pdfDocument);
	    
	    final FontSet set = new FontSet();
	    set.addFont(FONTS_DIR + "NotoNaskhArabic-Regular.ttf");
	    set.addFont(FONTS_DIR + "NotoSansTamil-Regular.ttf");
	    set.addFont(FONTS_DIR + "NotoSansDevanagari-Regular.ttf");
	    set.addFont(FONTS_DIR + "NotoSansGujarati-Regular.ttf"); // Added Gujarati font
	    set.addFont(FONTS_DIR + "FreeSans.ttf");
	    set.addFont(FONTS_DIR + "arial.ttf");
	    
	    document.setFontProvider(new FontProvider(set));
	    document.setProperty(Property.FONT, new String[]{"MyFontFamilyName"});
	    
	    
	    // Parse JasperReports XML file
	    final File xmlFile = new File(XML_DIR + "demo.jrxml");
	    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    final DocumentBuilder builder = factory.newDocumentBuilder();
	    final org.w3c.dom.Document doc = builder.parse(xmlFile);
	    
	    // Extract title from XML
	    NodeList titleElements = doc.getElementsByTagName("staticText");
	    String titleText = "User List"; // Default title
	    
	    // Find title in the title band
	    for (int i = 0; i < titleElements.getLength(); i++) {
	        Node staticTextNode = titleElements.item(i);
	        Node parentNode = staticTextNode.getParentNode();
	        if (parentNode.getNodeName().equals("band")) {
	            Node bandParent = parentNode.getParentNode();
	            if (bandParent.getNodeName().equals("title")) {
	                NodeList textNodes = staticTextNode.getChildNodes();
	                for (int j = 0; j < textNodes.getLength(); j++) {
	                    if (textNodes.item(j).getNodeName().equals("text")) {
	                        titleText = textNodes.item(j).getTextContent().trim();
	                        break;
	                    }
	                }
	                break;
	            }
	        }
	    }
	    
	    // Add title to PDF
	    Paragraph title = new Paragraph(titleText)
	        .setFontSize(18)
	        .setTextAlignment(TextAlignment.CENTER)
	        .setMarginBottom(20);
	    document.add(title);
	    
	    // Extract column headers from XML
	    List<String> headers = new ArrayList<>();
	    NodeList columnHeaderBands = doc.getElementsByTagName("columnHeader");
	    
	    if (columnHeaderBands.getLength() > 0) {
	        Node columnHeaderBand = columnHeaderBands.item(0);
	        NodeList staticTexts = ((Element) columnHeaderBand).getElementsByTagName("staticText");
	        
	        for (int i = 0; i < staticTexts.getLength(); i++) {
	            Node staticTextNode = staticTexts.item(i);
	            NodeList textNodes = ((Element) staticTextNode).getElementsByTagName("text");
	            if (textNodes.getLength() > 0) {
	                String headerText = textNodes.item(0).getTextContent().trim();
	                headers.add(headerText);
	            }
	        }
	    }
	    
	    // Create table with dynamic column count
	    int columnCount = Math.max(headers.size(), 4); // Default to 4 if no headers found
	    float[] columnWidths = new float[columnCount];
	    Arrays.fill(columnWidths, 100f / columnCount); // Equal width columns
	    
	    Table table = new Table(UnitValue.createPercentArray(columnWidths))
	        .useAllAvailableWidth();
	    
	    // Add headers to table
	    if (headers.isEmpty()) {
	        // Default headers if XML parsing failed
	        table.addHeaderCell(new Cell().add(new Paragraph("ID")));
	        table.addHeaderCell(new Cell().add(new Paragraph("Name")));
	        table.addHeaderCell(new Cell().add(new Paragraph("Name (Hindi)")));
	        table.addHeaderCell(new Cell().add(new Paragraph("Name (Gujarati)")));
	    } else {
	        for (String header : headers) {
	            Cell headerCell = new Cell().add(new Paragraph(header));
	            
	            // Apply appropriate font based on content
	            if (containsHindi(header)) {
	                headerCell.setFont(PdfFontFactory.createFont(FONTS_DIR + "NotoSansDevanagari-Regular.ttf", PdfEncodings.IDENTITY_H));
	            } else if (containsGujarati(header)) {
	                headerCell.setFont(PdfFontFactory.createFont(FONTS_DIR + "NotoSansGujarati-Regular.ttf", PdfEncodings.IDENTITY_H));
	            }
	            
	            table.addHeaderCell(headerCell);
	        }
	    }
	    
	    // Add user data to table
	    for (UserBean user : users) {
	        // Add cells based on available data
	        if (user.getId() != null) {
	            table.addCell(new Cell().add(new Paragraph(user.getId().toString())));
	        } else {
	            table.addCell(new Cell().add(new Paragraph("")));
	        }
	        
	        // Name (English)
	        table.addCell(new Cell().add(new Paragraph(user.getName() != null ? user.getName() : "")));
	        
	        // Name (Hindi) - with appropriate font
	        Cell hindiCell = new Cell().add(new Paragraph(user.getName_hin() != null ? user.getName_hin() : ""));
	        try {
	            hindiCell.setFont(PdfFontFactory.createFont(FONTS_DIR + "NotoSansDevanagari-Regular.ttf", PdfEncodings.IDENTITY_H));
	        } catch (Exception e) {
	            System.err.println("Failed to set Hindi font: " + e.getMessage());
	        }
	        table.addCell(hindiCell);
	        
	        // Name (Gujarati) - with appropriate font
	        Cell gujaratiCell = new Cell().add(new Paragraph(user.getName_guj() != null ? user.getName_guj() : ""));
	        try {
	            gujaratiCell.setFont(PdfFontFactory.createFont(FONTS_DIR + "NotoSansGujarati-Regular.ttf", PdfEncodings.IDENTITY_H));
	        } catch (Exception e) {
	            System.err.println("Failed to set Gujarati font: " + e.getMessage());
	        }
	        table.addCell(gujaratiCell);
	    }
	    
	    // Add table to document
	    document.add(table);
	    
	    // Add footer with total count
	    Paragraph footer = new Paragraph("Total Users: " + users.size())
	        .setTextAlignment(TextAlignment.RIGHT)
	        .setMarginTop(20)
	        .setFontSize(10);
	    document.add(footer);
	    
	    addBackgroundImageAfterContent(pdfDocument);
	    
	    document.close();
	    pdfDocument.close();
	}

	private void addBackgroundImageAfterContent(PdfDocument pdfDocument) {
	    try {
	        ImageData imageData = ImageDataFactory.create(IMAGES_DIR + "background.png");
	        
	        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
	            Image backgroundImage = new Image(imageData);
	            
	            // Add to background layer (behind content)
	            PdfCanvas canvas = new PdfCanvas(pdfDocument.getPage(i).newContentStreamBefore(), 
	                                           pdfDocument.getPage(i).getResources(), 
	                                           pdfDocument);
	            Canvas layoutCanvas = new Canvas(canvas, new Rectangle(0, 0, 595, 842));
	            
	            backgroundImage.setFixedPosition(0, 0);
	            backgroundImage.scaleToFit(595, 842);
	            backgroundImage.setOpacity(0.1f);
	            
	            layoutCanvas.add(backgroundImage);
	            layoutCanvas.close();
	        }
	        
	        System.out.println("Background image added to " + pdfDocument.getNumberOfPages() + " pages");
	    } catch (Exception e) {
	        System.err.println("Failed to add background image: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	
	// Helper methods for font detection
	private boolean containsHindi(String text) {
	    return text.matches(".*[\\u0900-\\u097F].*");
	}

	private boolean containsGujarati(String text) {
	    return text.matches(".*[\\u0A80-\\u0AFF].*");
	}
	
	
	public static void main(String[] args) {
		UserController uc = new UserController();
		try {
			uc.createPdf3();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
