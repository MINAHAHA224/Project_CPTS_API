package vn.javaweb.ComputerShop.controller.admin;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.ProductCreateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.request.ProductUpdateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponseT;
import vn.javaweb.ComputerShop.domain.dto.response.ProductDetailRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ProductFilterAdRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;
import vn.javaweb.ComputerShop.service.AdminProductService;
import vn.javaweb.ComputerShop.service.ProductService;

import vn.javaweb.ComputerShop.utils.ConstantVariable;
import vn.javaweb.ComputerShop.utils.SecurityUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "${api.prefix.admin.product}")
@Slf4j
public class AdminProductController {
    private final ProductService productService;
    private final MessageComponent messageComponent;
    private final AdminProductService adminProductService;


    @GetMapping("/")
    public ResponseEntity<ApiResponseT<ProductFilterAdRpDTO>> getProducts(Locale locale, @RequestParam("page") Optional<String> pageOptional) {
        log.info(ConstantVariable.ADMIN_PRODUCT + "Start getAllProduct at {} ", SecurityUtils.currentTime);
        ProductFilterAdRpDTO result = this.adminProductService.handleGetProducts(pageOptional);
        log.info(ConstantVariable.ADMIN_PRODUCT + "End getAllProduct at {} ", SecurityUtils.currentTime);
        return ResponseEntity.ok().body(ApiResponseT.<ProductFilterAdRpDTO>builder()
                .status(200)
                .message(messageComponent.getLocalizedMessage("admin.product.get.all.success", locale))
                .data(result)
                .build());
    }


    @PostMapping("")
    public ResponseEntity<ApiResponseT<?>> createProduct(Locale locale, @Valid @RequestBody ProductCreateRqDTO productCreateRqDTO,
                                                         @RequestParam("avatarFile") MultipartFile file) {
        log.info(ConstantVariable.ADMIN_PRODUCT + "Start postCreateProduct at {} ", SecurityUtils.currentTime);
        this.adminProductService.handleCreateProduct(productCreateRqDTO, file, locale);
        return ResponseEntity.ok().body(ApiResponseT.<ProductFilterAdRpDTO>builder()
                .status(200)
                .message(messageComponent.getLocalizedMessage("admin.product.create.success", locale))
                .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseT<ProductDetailRpDTO>> getProduct(Locale locale, @PathVariable("id") Long id) {
        log.info(ConstantVariable.ADMIN_PRODUCT + "Start getProductDetail at {} - with productId : {} ", SecurityUtils.currentTime, id);
        ProductDetailRpDTO productDetail = this.adminProductService.handleGetProduct(id);
        log.info(ConstantVariable.ADMIN_PRODUCT + "End getProductDetail at {} ", SecurityUtils.currentTime);
        return ResponseEntity.ok().body(ApiResponseT.<ProductDetailRpDTO>builder()
                .status(200)
                .message(messageComponent.getLocalizedMessage("admin.product.get.detail.success", locale))
                .data(productDetail)
                .build());
    }

    // product update


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseT<?>> updateProduct(Locale locale, @Valid @RequestBody ProductUpdateRqDTO productUpdateRqDTO,
                                                         @RequestParam("avatarFile") MultipartFile file) {
        log.info(ConstantVariable.ADMIN_PRODUCT + "Start postProductUpdate at {} - with productId : {} ", SecurityUtils.currentTime, productUpdateRqDTO.getId());
        this.adminProductService.handleUpdateProduct(productUpdateRqDTO, file, locale);
        return ResponseEntity.ok().body(ApiResponseT.<ProductDetailRpDTO>builder()
                .status(200)
                .message(messageComponent.getLocalizedMessage("admin.product.update.success", locale))
                .build());

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseT<?>> getDeleteProduct(Locale locale, @PathVariable("id") Long id) {
        log.info(ConstantVariable.ADMIN_PRODUCT + "Start getDeleteProduct at {} - with productId : {} ", SecurityUtils.currentTime, id);
        this.adminProductService.handleDeleteProduct(id);
        return ResponseEntity.ok().body(ApiResponseT.<ProductDetailRpDTO>builder()
                .status(200)
                .message(messageComponent.getLocalizedMessage("admin.product.delete.success", locale))
                .build());
    }


}
