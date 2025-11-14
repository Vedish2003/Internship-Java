package MEDISCAN.MEDI.controller;

import MEDISCAN.MEDI.model.QRReport;
import MEDISCAN.MEDI.service.QRReportService;
import MEDISCAN.MEDI.service.PdfService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Controller
public class QRReportController {

    @Autowired
    private QRReportService qrReportService;

    @Autowired
    private PdfService pdfService;

    // ❌ REMOVE or rename this mapping
    // @GetMapping("/")
    // public String home() {
    //     return "home";
    // }

    // Show QR form
    @GetMapping("/my-qr")
    public String showQRForm(Model model) {
        model.addAttribute("error", "");
        return "my-qr";
    }

    // Handle QR request from user
    @PostMapping("/my-qr")
    public String getReport(@RequestParam String userId, Model model) throws WriterException, IOException {
        if (userId == null || userId.trim().isEmpty()) {
            model.addAttribute("error", "Please enter a valid User ID");
            return "my-qr";
        }

        QRReport report = qrReportService.getReportByUserId(userId.trim());
        if (report == null) {
            model.addAttribute("error", "Report not found for ID: " + userId);
            return "my-qr";
        }

        // LAN-accessible URL for QR code scanning
        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "localhost";
        }

        String qrUrl = "http://" + hostAddress + ":8050/download/" + report.getUserId();
        String qrCodeBase64 = qrReportService.generateQRCode(qrUrl, 200, 200);

        model.addAttribute("report", report);
        model.addAttribute("qrCode", qrCodeBase64);
        return "report"; 
    }

    // Admin dashboard
    @GetMapping("/admin")
    public String adminDashboard(Model model) throws WriterException, IOException {
        return "admin";
    }

    // Admin page showing all QR reports
    @GetMapping("/admin/qr-reports")
    public String adminQrReports(Model model) throws WriterException, IOException {
        List<QRReport> reports = qrReportService.getAllReports();

        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "localhost";
        }

        for (QRReport report : reports) {
            String qrUrl = "http://" + hostAddress + ":8050/download/" + report.getUserId();
            String qrBase64 = qrReportService.generateQRCode(qrUrl, 150, 150);
            report.setQrCode(qrBase64);
        }

        model.addAttribute("reports", reports);
        return "admin-qr-reports";
    }

    // PDF download
    @GetMapping("/download/{userId}")
    public void downloadPdf(@PathVariable String userId, HttpServletResponse response) throws IOException {
        QRReport report = qrReportService.getReportByUserId(userId);
        if (report == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Report not found");
            return;
        }

        byte[] pdfBytes = pdfService.generatePdfForReport(report);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + report.getName() + "_report.pdf");
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
}
