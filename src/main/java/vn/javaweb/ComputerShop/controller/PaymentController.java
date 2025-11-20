package vn.javaweb.ComputerShop.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.InfoOrderRqDTO;
import vn.javaweb.ComputerShop.domain.dto.request.momo.MomoRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponseT;
import vn.javaweb.ComputerShop.domain.dto.response.PaymentDto;
import vn.javaweb.ComputerShop.service.cart.CartService;
import vn.javaweb.ComputerShop.service.order.OrderService;
import vn.javaweb.ComputerShop.component.MomoPayment;
import vn.javaweb.ComputerShop.service.product.ProductService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix.payment}")
public class PaymentController {
    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final MomoPayment momoPayment;
    private final MessageComponent messageComponent;

    @PostMapping("")
    public ResponseEntity<ApiResponseT<Map<String, String>>> handlePayment(Locale locale, @Valid @RequestBody InfoOrderRqDTO infoOrderRqDTO) {
        Map<String, String> data = new HashMap<>();
        data.put("typePayment", infoOrderRqDTO.getPaymentMethod());
        PaymentDto paymentDto = this.cartService.handlePayment(infoOrderRqDTO, locale);
        if ("MOMO".equals(paymentDto.getTypePayment())) {
            MomoRpDTO momoRpDTO = (MomoRpDTO) paymentDto.getData();
            data.put("paymentUrl", momoRpDTO.getPayUrl());
            return ResponseEntity.ok().body(
                    ApiResponseT.<Map<String, String>>builder()
                            .status(HttpStatus.OK.value())
                            .message(messageComponent.getLocalizedMessage("client.cart.checkout.momo.directUrl", locale))
                            .data(data)
                            .build()
            );
        } else {
            return ResponseEntity.ok().body(
                    ApiResponseT.<Map<String, String>>builder()
                            .status(HttpStatus.OK.value())
                            .message(messageComponent.getLocalizedMessage("client.cart.payment.cod.success", locale))
                            .data(data)
                            .build()
            );
        }
    }

//    @GetMapping(value = "/momo-endpoint , produces = MediaType.TEXT_HTML_VALUE")
//    @ResponseBody
//    public ResponseEntity<ApiResponseT<?>> getMomoCheckout (// Dùng @RequestParam để đọc dữ liệu từ URL
//                                                            @RequestParam(name = "resultCode", required = false) String resultCode,
//                                                            @RequestParam(name = "orderId", required = false) String orderId,
//                                                            // (Bạn có thể thêm các @RequestParam khác nếu cần)
//                                                            Locale locale ){
//        MomoRpDTO momoRpDTO = new MomoRpDTO();
//        momoRpDTO.setResultCode(resultCode != null ? Integer.parseInt(resultCode) : -1);
//        momoRpDTO.setOrderId(orderId);
//
//       this.orderService.handleMomoCheckout (momoRpDTO , locale );
//        return ResponseEntity.ok().body(
//                ApiResponseT.<Map<String, String>>builder()
//                        .status(HttpStatus.OK.value())
//                        .message(messageComponent.getLocalizedMessage("client.cart.payment.momo.success", locale))
//                        .build()
//        );
//    }


    @GetMapping(value = "/momo-endpoint", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getMomoCheckout(
            // Dùng @RequestParam để đọc dữ liệu từ URL
            @RequestParam(name = "resultCode", required = false) String resultCode,
            @RequestParam(name = "orderId", required = false) String orderId,
            // (Bạn có thể thêm các @RequestParam khác nếu cần)
            Locale locale
    ) {
        MomoRpDTO momoRpDTO = new MomoRpDTO();
        momoRpDTO.setResultCode(resultCode != null ? Integer.parseInt(resultCode) : -1);
        momoRpDTO.setOrderId(orderId);

        String notificationMessage;
        boolean isSuccess;

        try {
            this.orderService.handleMomoCheckout(momoRpDTO, locale);
            notificationMessage = messageComponent.getLocalizedMessage("client.cart.payment.momo.success", locale);
            isSuccess = true;
        } catch (Exception e) {
            notificationMessage = e.getMessage();
            isSuccess = false;
        }

        // Tạo trang HTML để trả về
        String htmlPage;
        if (isSuccess) {
            htmlPage = "<html><body style='font-family: sans-serif; text-align: center; padding-top: 50px;'>" +
                    "<h1 style='color: #4CAF50;'>Thanh Toán Thành Công</h1>" +
                    "<p>" + notificationMessage + "</p>" +
                    "<p>Đơn hàng của bạn đang được xử lý.</p>" +
                    "</body></html>";
        } else {
            htmlPage = "<html><body style='font-family: sans-serif; text-align: center; padding-top: 50px;'>" +
                    "<h1 style='color: #F44336;'>Thanh Toán Thất Bại</h1>" +
                    "<p>" + notificationMessage + "</p>" +
                    "<p>Vui lòng thử lại.</p>" +
                    "</body></html>";
        }

        return htmlPage;
    }
}
