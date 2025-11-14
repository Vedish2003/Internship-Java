package MEDISCAN.MEDI.service;

import MEDISCAN.MEDI.model.QRReport;
import MEDISCAN.MEDI.repository.QRReportRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class QRReportService {

    @Autowired
    private QRReportRepository qrReportRepository;

    public QRReport getReportByUserId(String userId) {
        return qrReportRepository.findByUserId(userId).orElse(null);
    }

    public List<QRReport> getAllReports() {
        return qrReportRepository.findAll();
    }

    public String generateQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
