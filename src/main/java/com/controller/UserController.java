package com.controller;

import java.io.File;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.dao.UserDao;

import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class UserController {

	private static final String LICENSE_FILE = "F:\\2.Royal\\Advance JAVA\\pdf_test\\src\\main\\resources\\itextkey.json";
	private static final String FONTS_DIR = "F:\\2.Royal\\Advance JAVA\\pdf_test\\src\\main\\resources\\fonts\\";

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
		
		String reportPath = request.getServletContext().getRealPath("/MyReports/demo.jasper");
		String outputPath = "F:\\2.Royal\\Advance JAVA\\pdf_test\\src\\main\\webapp\\MyReports\\users_report.pdf";
        
		loadLicense();
        
		JRResultSetDataSource dataSource = new JRResultSetDataSource(rsLocal);
		JasperPrint print = null;
		try {
			
			Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_LOCALE", new Locale("hi", "IN"));
            
			print = JasperFillManager.fillReport(reportPath, null, dataSource);
			JasperExportManager.exportReportToPdfFile(print, outputPath);
	        System.out.println("PDF report saved successfully at: " + outputPath);

		} catch (JRException e) {
			e.printStackTrace();
		}
		
//		JasperViewer.viewReport(print, false);
		
	}
	
	
	public JasperPrint getAllUsersData2(HttpServletRequest request) {
		ResultSet rsLocal = new UserDao().displayAllRecord2();
		
		String reportPath = request.getServletContext().getRealPath("/MyReports/demo.jasper");
		
		JRResultSetDataSource dataSource = new JRResultSetDataSource(rsLocal);
		JasperPrint print = null;
		try {
			print = JasperFillManager.fillReport(reportPath, null, dataSource);
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		return print;
	}
}
