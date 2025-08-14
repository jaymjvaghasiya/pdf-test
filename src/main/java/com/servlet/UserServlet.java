package com.servlet;

import java.io.File;
import java.io.IOException;

import com.controller.UserController;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static final String LICENSE_FILE = "src/main/resources/itextkey.json";
	private static final String LICENSE_FILE = "F:\\2.Royal\\Advance JAVA\\pdf_test\\src\\main\\resources\\itextkey.json";

	public UserServlet() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Set response headers BEFORE generating PDF
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=report.pdf");
		response.setHeader("Cache-Control", "no-cache");

		// Generate PDF directly to response
		UserController controller = new UserController();
		try {
			controller.createPdf4(response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			// Reset response and send error
			response.reset();
			response.setContentType("text/html");
			response.getWriter().println("<h1>Error generating PDF: " + e.getMessage() + "</h1>");
			response.getWriter().println("<pre>");
			e.printStackTrace(response.getWriter());
			response.getWriter().println("</pre>");
		}

		// Flush the output stream
		response.getOutputStream().flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

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

}
