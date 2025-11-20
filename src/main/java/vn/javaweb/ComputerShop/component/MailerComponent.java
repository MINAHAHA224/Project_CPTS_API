package vn.javaweb.ComputerShop.component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockHttpServletRequest; // Quan trọng
import org.springframework.mock.web.MockHttpServletResponse; // Quan trọng
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder; // Quan trọng
import org.springframework.web.context.request.ServletRequestAttributes; // Quan trọng
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import vn.javaweb.ComputerShop.domain.dto.response.OrderDetailRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.OrderInvoiceDTO;
import vn.javaweb.ComputerShop.domain.entity.OrderDetailEntity;
import vn.javaweb.ComputerShop.domain.entity.OrderEntity;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailerComponent {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine springTemplateEngine; // Đảm bảo ViewResolver này được cấu hình đúng

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Value("${spring.mail.from}")
    private String emailFrom;

//    public void handleSendConfirmLink(String emailTo, String OTP) {
//
//        // Gán dữ liệu vào model -> mục đích để chuẩn bị gửi model đi cùng MockRequest tới server
//        Map<String, Object> model = new HashMap<>();
//
//        model.put("OTP", OTP); // Key này phải khớp với ${OTP} trong JSP
//
//        // --- Phần render JSP thành String ---
//        String htmlContent = "";
//        try {
//            // Cố gắng lấy request hiện tại nếu có, hoặc tạo mock request
//            HttpServletRequest request = getCurrentRequest();
//            if (request == null) {
//                request = new MockHttpServletRequest();
//            }
//
//            MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//
//            View view = viewResolver.resolveViewName("client/auth/confirmEmail", Locale.getDefault());
//            if (view == null) {
//                log.error("Không thể resolve view: client/auth/confirmEmail");
//            }
//
//            view.render(model, request, mockResponse); // Truyền request và mockResponse
//                /*
//                Đầu tiên : request của Moc  đưa modell dữ liệu về server vào JSP : client/auth/confirmEmail
//                Thứ hai : Response ra giao diện JSP
//                Cuối dùng : Lấy String từ Response giao diện này
//                => Bởi vi thằng Helper của mailer nó chỉ cho chuyển dưới dạng String HTML , hoặc Thymlead( không hỗ trợ truyền JSP do JSP nó cũ rồi ) ,
//                 Vì vậy phải tìm cách từ JSP sao cho nó render ra HTML khổ nổi là  thằng JSP nó không có hàm nào
//                render ra sẵn HTML, nên phải thông qua Response ( thông qua Response JSP sẽ nhả HTML ra Response )
//                => Từ response mới lấy String HTML ra gắn vô mail được chứ mail nó ko ăn JSP
//                 */
//            htmlContent = mockResponse.getContentAsString();
//
//            if (htmlContent == null || htmlContent.isEmpty()) {
//                log.warn("Rendered HTML content is empty for view: client/auth/confirmEmail. Check JSP and model.");
//            }
//
//            sendConfirmLink(emailTo, htmlContent);
//
//        } catch (IOException | ServletException e) { // Bắt thêm ServletException
//            log.error("Lỗi IO hoặc Servlet khi render JSP: ", e);
//        } catch (Exception e) { // Bắt các lỗi khác từ view.render
//            log.error("Lỗi không xác định khi render JSP: ", e);
//        }
//    }
public void handleSendConfirmLink(String emailTo, String OTP) {

    // Gán dữ liệu vào model -> mục đích để chuẩn bị gửi model đi cùng MockRequest tới server
    Map<String, Object> model = new HashMap<>();

    model.put("OTP", OTP); // Key này phải khớp với ${OTP} trong JSP

    // --- Phần render JSP thành String ---

        String htmlContent = renderThymeleafTemplate("confirm-email-template", model);


    if ( htmlContent == null || htmlContent.isEmpty()){
        log.error("Render HTML cho email xác nhận thất bại.");
    }
    sendConfirmLink(emailTo, htmlContent);

}


    public void sendConfirmLink(String emailTo, String htmlContent) {
        log.info("Begin sending confirming link to user, email={}", emailTo);
        emailTo = "caothaiiop1234@gmail.com";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            // Cấu hình email
            helper.setFrom(emailFrom, "3tTechShop"); // Tên người gửi tùy chỉnh
            helper.setTo(emailTo);
            helper.setSubject("OTP Xác Nhận Email Đặt Lại Mật Khẩu"); // Tiêu đề rõ ràng hơn
            helper.setText(htmlContent, true); // true để gửi dạng HTML

            mailSender.send(message);
            log.info("Confirming link has sent to user, email={}", emailTo);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Lỗi khi chuẩn bị email: {}", e.getMessage(), e);
        } catch (Exception ex) { // Bắt lỗi từ mailSender.send()
            log.error("Error sending mail: {}", ex.getMessage(), ex);
        }
    }

    // Helper method để lấy HttpServletRequest hiện tại (nếu có)
//    private HttpServletRequest getCurrentRequest() {
//        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        return (attrs != null) ? attrs.getRequest() : null;
//    }

    public String generateOTP(int length) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return otp.toString();
    }


    OrderInvoiceDTO handleMapOrderEntityToOrderInvoiceDTO(OrderEntity orderEntity) {
        OrderInvoiceDTO result = new OrderInvoiceDTO();
        result.setId(orderEntity.getId());
        result.setTime(orderEntity.getTime());
        result.setTypePayment(orderEntity.getTypePayment());
        result.setStatusPayment(orderEntity.getStatusPayment());
        result.setReceiverName(orderEntity.getReceiverName());
        result.setReceiverAddress(orderEntity.getReceiverAddress());
        result.setReceiverPhone(orderEntity.getReceiverPhone());
        result.setUserEmail(orderEntity.getUser().getEmail());

        List<OrderDetailRpDTO> listOrderDetailDTO = new ArrayList<>();
        for (OrderDetailEntity orderDetailEntity : orderEntity.getOrderDetails()) {
            OrderDetailRpDTO orderDetailDTO = new OrderDetailRpDTO();
            orderDetailDTO.setProductId(orderDetailEntity.getProduct().getId());
            orderDetailDTO.setProductName(orderDetailEntity.getProduct().getName());
            orderDetailDTO.setProductImage(orderDetailEntity.getProduct().getImage());
            orderDetailDTO.setPrice(orderDetailEntity.getProduct().getPrice());
            orderDetailDTO.setProductQuantity(orderDetailEntity.getQuantity());
            listOrderDetailDTO.add(orderDetailDTO);
        }
        result.setOrderDetails(listOrderDetailDTO);

        result.setTotalPrice(orderEntity.getTotalPrice());

        return result;
    }



    public void sendInvoiceEmail(String emailTo, String htmlContent , OrderInvoiceDTO order) {

        emailTo = "caothaiiop1234@gmail.com";
        log.info("Begin sendInvoiceEmail link to user, email={}", emailTo);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            // Cấu hình email
            helper.setFrom(emailFrom, "Laptopshop"); // Tên người gửi
            helper.setTo(emailTo);
            helper.setSubject("Hóa đơn điện tử cho đơn hàng #" + order.getId() + " tại Laptopshop");
            helper.setText(htmlContent, true); // true để gửi dạng HTML

            mailSender.send(message);
            log.info("Đã gửi email hóa đơn cho đơn hàng #{}, email={}", order.getId(), emailTo);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Lỗi khi chuẩn bị email: {}", e.getMessage(), e);
        } catch (Exception ex) { // Bắt lỗi từ mailSender.send()
            log.error("Error sending mail: {}", ex.getMessage(), ex);
        }
    }

    // PHƯƠNG THỨC MỚI ĐỂ GỬI HÓA ĐƠN
    public void handleSendInvoiceEmail(OrderEntity orderEntity) {
        OrderInvoiceDTO order = handleMapOrderEntityToOrderInvoiceDTO(orderEntity);

        if (order == null || order.getUserEmail() == null) {
            log.error("Không thể gửi email hóa đơn: thông tin đơn hàng hoặc email người dùng không hợp lệ.");
        }
        String emailTo = order.getUserEmail();
        log.info("Chuẩn bị xử lý email hóa đơn cho đơn hàng #{}, email={}", order.getId(), emailTo);

        try {

            Map<String, Object> model = new HashMap<>();
            model.put("order", order); // Truyền toàn bộ đối tượng Order vào template

            // Sử dụng view đã tạo: client/mail/invoice-template.jsp
            String htmlContent = renderThymeleafTemplate("invoice-template", model);
            if (htmlContent == null) {
                log.error("Render HTML cho email hóa đơn thất bại.");
            }

            sendInvoiceEmail(emailTo, htmlContent, order);

        } catch (Exception ex) {
            log.error("Lỗi không xác định khi gửi email hóa đơn: {}", ex.getMessage(), ex);
        }
    }

    // Helper method để render JSP thành String, tái sử dụng
//    private String renderViewToString(String viewName, Map<String, Object> model) {
//        try {
//            HttpServletRequest request = getCurrentRequest();
//            if (request == null) {
//                request = new MockHttpServletRequest();
//                // Cần thiết nếu JSP của bạn sử dụng các thuộc tính request mặc định hoặc context path
//                ((MockHttpServletRequest) request).setContextPath(""); // Hoặc giá trị context path của bạn
//            }
//            MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//
//            View view = viewResolver.resolveViewName(viewName, Locale.getDefault());
//            if (view == null) {
//                log.error("Không thể resolve view: {}", viewName);
//                return null;
//            }
//
//            view.render(model, request, mockResponse);
//            String htmlContent = mockResponse.getContentAsString();
//            if (htmlContent == null || htmlContent.isEmpty()) {
//                log.warn("Rendered HTML content is empty for view: {}. Check JSP and model.", viewName);
//            }
//            return htmlContent;
//
//        } catch (IOException | ServletException e) {
//            log.error("Lỗi IO hoặc Servlet khi render JSP '{}': ", viewName, e);
//            return null;
//        } catch (Exception e) {
//            log.error("Lỗi không xác định khi render JSP '{}': ", viewName, e);
//            return null;
//        }
//    }
    private String renderThymeleafTemplate(String templateName, Map<String, Object> model) {
        // Context của Thymeleaf chứa các biến sẽ được truyền vào template
        Context context = new Context(Locale.forLanguageTag("vi-VN")); // Set locale nếu cần
        context.setVariables(model);

        // Dùng templateEngine để xử lý và tạo ra chuỗi HTML
        return springTemplateEngine.process(templateName, context);
    }
}