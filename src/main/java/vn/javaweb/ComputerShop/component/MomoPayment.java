package vn.javaweb.ComputerShop.component;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import vn.javaweb.ComputerShop.domain.dto.request.momo.*;
import vn.javaweb.ComputerShop.domain.entity.OrderEntity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public  class MomoPayment {
    private  final RestTemplate restTemplate = new RestTemplate();
    @Value("${momo.partner-code}")
    private  String PARTNER_CODE;

    @Value("${momo.access-key}")
    private  String ACCESS_KEY;

    @Value("${momo.secret-key}")
    private  String SECRET_KEY;

    @Value("${momo.return-url}")
    private  String REDIRECT_URL;

    @Value("${momo.ipn-url}")
    private  String IPN_URL;

    @Value("${momo.request-type}")
    private  String REQUEST_TYPE; //

    // Sửa hàm này, có thể trả về MomoRpDTO hoặc payUrl
    public  MomoRpDTO generateMomoPayment(OrderEntity order) { // Hoặc String nếu chỉ trả về payUrl

        String requestId = UUID.randomUUID().toString();
        String orderId = String.valueOf(order.getId()) + UUID.randomUUID(); // ID đơn hàng của bạn
//        (long) order.getTotalPrice()
        Long amount =  10000L ;
        String orderInfo = "Thanh toan don hang LaptopShop " + orderId;
        String extraData = "";

        // 1. Chuẩn bị các tham số để tạo signature
        // Chỉ bao gồm các tham số ở cấp độ gốc của JSON request theo tài liệu Momo
        Map<String, String> signatureParams = new TreeMap<>(); // TreeMap tự động sắp xếp key theo alphabet
        signatureParams.put("accessKey", ACCESS_KEY);
        signatureParams.put("amount", String.valueOf(amount));
        signatureParams.put("extraData", extraData); // CHỈ thêm nếu Momo yêu cầu extraData phải được ký. Thường là KHÔNG.
        signatureParams.put("ipnUrl", IPN_URL);
        signatureParams.put("orderId", orderId);
        signatureParams.put("orderInfo", orderInfo);
        signatureParams.put("partnerCode", PARTNER_CODE);
        signatureParams.put("redirectUrl", REDIRECT_URL);
        signatureParams.put("requestId", requestId);
        signatureParams.put("requestType", REQUEST_TYPE);

        // 2. Tạo chuỗi rawSignature từ các tham số đã sắp xếp
        StringBuilder rawSignatureBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : signatureParams.entrySet()) {
            if (rawSignatureBuilder.length() > 0) {
                rawSignatureBuilder.append("&");
            }
            rawSignatureBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        String rawSignature = rawSignatureBuilder.toString();
        System.out.println("Momo Raw Signature String: " + rawSignature); // Log để debug

        String generatedSignature = "";
        try {
            generatedSignature = signHmacSHA256(rawSignature, SECRET_KEY);
            System.out.println("Momo Generated Signature: " + generatedSignature); // Log để debug
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo Momo signature: " + e.getMessage());
            e.printStackTrace();
            // Xử lý lỗi, có thể throw exception hoặc trả về null
            return null;
        }

        // 3. Chuẩn bị request body (MomoRqDTO)
        // KHÔNG bao gồm items, userInfo, deliveryInfo nếu requestType="captureWallet" phiên bản cũ
        // hoặc nếu chúng không được yêu cầu cho requestType của bạn. Kiểm tra tài liệu!
        // Nếu requestType của bạn yêu cầu (ví dụ, các API mới hơn hoặc payWithMethod), thì thêm vào.
        MomoRqDTO.MomoRqDTOBuilder momoRequestBuilder = MomoRqDTO.builder()
                .partnerCode(PARTNER_CODE)
                .requestId(requestId)
                .amount(amount)
                .orderId(orderId)
                .orderInfo(orderInfo)
                .redirectUrl(REDIRECT_URL)
                .ipnUrl(IPN_URL)
                .requestType(REQUEST_TYPE)
                .extraData(extraData) // Gửi extraData trong body
                .lang("vi")
                .signature(generatedSignature);



        MomoRqDTO momoRequest = momoRequestBuilder.build();

        // 4. Gọi API Momo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MomoRqDTO> requestEntity = new HttpEntity<>(momoRequest, headers);
        String momoFullApiUrl =   "https://test-payment.momo.vn/v2/gateway/api/create"; // URL đầy đủ của API tạo thanh toán
        try {
            ResponseEntity<MomoRpDTO> responseEntity = restTemplate.postForEntity(
                    momoFullApiUrl,
                    requestEntity,
                    MomoRpDTO.class
            );

            MomoRpDTO result = responseEntity.getBody();
            return result;

        } catch (WebClientResponseException e) {
            // Lỗi đã được log ở onStatus, ở đây có thể làm thêm nếu cần
            System.err.println("Caught WebClientResponseException in outer catch block. Status: " + e.getStatusCode() + ", Body: " + e.getResponseBodyAsString());
            return null; // Hoặc throw exception
        } catch (Exception e) { // Bắt các lỗi khác không phải WebClientResponseException
            System.err.println("Lỗi không xác định khi gọi Momo API: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public  String signHmacSHA256(String data, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
