package vn.javaweb.ComputerShop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.CartDetailsListDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponseT;
import vn.javaweb.ComputerShop.domain.dto.response.CartRpDTO;
import vn.javaweb.ComputerShop.service.cart.CartService;
import vn.javaweb.ComputerShop.service.user.UserService;
import vn.javaweb.ComputerShop.utils.SecurityUtils;

import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix.cart}")
public class CartController {
    private final CartService cartService;
    private final MessageComponent messageComponent;

    @GetMapping("")
    public ResponseEntity<ApiResponseT<CartRpDTO>> getCart() {
        CartRpDTO result = this.cartService.handleGetCartDetail();
        return ResponseEntity.ok().body(ApiResponseT.<CartRpDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Get cart details successfully")
                .data(result)
                .build());
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponseT<Map<String, Integer>>> getCartSummary() {
        String email = SecurityUtils.getPrincipal();
        Integer cartCount = this.cartService.handleGetTotalQuantityInCart(email);
        return ResponseEntity.ok().body(ApiResponseT.<Map<String, Integer>>builder()
                .status(HttpStatus.OK.value())
                .message("Get cart summary successfully")
                .data(Map.of("cartCount", cartCount))
                .build());
    }


    @PatchMapping("/items/{id}")
    public ResponseEntity<ApiResponseT<?>> addProductToCart(
            @PathVariable("id") Long productId, Locale locale) {
        this.cartService.handleAddOneProductToCart(productId);
        return ResponseEntity.ok().body(
                ApiResponseT.builder()
                        .status(HttpStatus.OK.value())
                        .message(messageComponent.getLocalizedMessage("cart.product.add.success", locale))
                        .data(null)
                        .build()
        );

    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<ApiResponseT<?>> deleteOneCartDetail(@PathVariable("id") Long id,Locale locale) {
        this.cartService.handleDeleteOneProductInCart(id);
        return ResponseEntity.ok().body(
                ApiResponseT.builder()
                        .status(HttpStatus.OK.value())
                        .message(messageComponent.getLocalizedMessage("cart.product.delete.success", locale))
                        .data(null)
                        .build()
        );
    }


    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiResponseT<?>> deleteCartDetail(@PathVariable("id") Long id,Locale locale) {
        this.cartService.handleDeleteProductInCart(id);
        return ResponseEntity.ok().body(
                ApiResponseT.builder()
                        .status(HttpStatus.OK.value())
                        .message(messageComponent.getLocalizedMessage("cart.product.delete.success", locale))
                        .data(null)
                        .build()
        );
    }


    @PostMapping("/items")
    public ResponseEntity<ApiResponseT<?>> handleAddProductFromViewDetail(
            @RequestParam("id") Long id,
            @RequestParam("quantity") Long quantity,
            Locale locale) {
        this.cartService.handleAddProductDetailToCart(id, quantity);
        return ResponseEntity.ok().body(
                ApiResponseT.builder()
                        .status(HttpStatus.OK.value())
                        .message(messageComponent.getLocalizedMessage("cart.product.delete.success", locale))
                        .data(null)
                        .build()
        );
    }


}
