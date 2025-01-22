package com.ohgiraffers.washplan.reservation.model.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {
    
    @Value("${app.baseUrl}")
    private String baseUrl;
    
    // 예약 페이지용 QR 코드 생성
    public byte[] generateQRCode(String content, int width, int height) throws WriterException, IOException {
        System.out.println("Using baseUrl: " + baseUrl);
        
        // 예약 페이지용 URL 패턴
        String qrContent = baseUrl + "/reservation/qr-scan/" + content;
        System.out.println("Generated QR content: " + qrContent);
        
        return generateQRCodeImage(qrContent, width, height);
    }

    // 마이페이지용 QR 코드 생성
    public byte[] generateMyPageQRCode(String content, int width, int height) throws WriterException, IOException {
        System.out.println("Using baseUrl: " + baseUrl);
        
        // 마이페이지용 URL 패턴
        String qrContent = baseUrl + "/mypage/reservations/scan/" + content;
        System.out.println("Generated MyPage QR content: " + qrContent);
        
        return generateQRCodeImage(qrContent, width, height);
    }

    // 실제 QR 코드 이미지 생성 로직
    private byte[] generateQRCodeImage(String content, int width, int height) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.MARGIN, 2);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        return outputStream.toByteArray();
    }
}