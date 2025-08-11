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
		
//		loadLicense();
		
		new UserController().getAllUsersData(request);
//		JasperPrint print = new UserController().getAllUsersData2(request);

//		response.setContentType("application/pdf");
//		response.setHeader("Content-Disposition", "inline; filename=report.pdf");
//
//
//		ServletOutputStream outStream = response.getOutputStream();
//		try {
//			JasperExportManager.exportReportToPdfStream(print, outStream);
//		} catch (JRException e) {
//			e.printStackTrace();
//		}
//		outStream.flush();
//		outStream.close();

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
