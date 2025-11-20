package vn.javaweb.ComputerShop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.CartDetailsListDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponseT;
import vn.javaweb.ComputerShop.domain.dto.response.CheckoutRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.OrderRpDTO;
import vn.javaweb.ComputerShop.service.cart.CartService;
import vn.javaweb.ComputerShop.service.order.OrderService;
import vn.javaweb.ComputerShop.component.MomoPayment;
import vn.javaweb.ComputerShop.service.product.ProductService;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix.orders}")
public class OrderController {
    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final MessageComponent messageComponent;
    private final MomoPayment momoPayment;

    @PostMapping("/confirm-checkout")
    public ResponseEntity<ApiResponseT<CheckoutRpDTO>> postConfirmCheckout(Locale locale
            , @RequestBody CartDetailsListDTO cartDetailsListDTO) {
        this.productService.handleConfirmCheckout(cartDetailsListDTO);
        CheckoutRpDTO dataCheckout = this.cartService.handleShowDataAfterCheckout();
        return ResponseEntity.ok().body(ApiResponseT.<CheckoutRpDTO>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("cart.checkout.confirm.success", locale))
                .data(dataCheckout)
                .build()
        );
    }
    @GetMapping("/history")
    public ResponseEntity<ApiResponseT<List<OrderRpDTO>>> getOrderHistoryPage() {
        List<OrderRpDTO> orders = this.orderService.handleGetDataOrderOfUser();
        return ResponseEntity.ok().body(ApiResponseT.<List<OrderRpDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Get order history successfully")
                .data(orders)
                .build()
        );
    }

}
