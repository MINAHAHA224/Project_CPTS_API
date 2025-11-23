package vn.javaweb.ComputerShop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import vn.javaweb.ComputerShop.domain.dto.request.ProductFilterDTO;
import vn.javaweb.ComputerShop.domain.dto.response.*;
import vn.javaweb.ComputerShop.service.cart.CartService;
import vn.javaweb.ComputerShop.service.order.OrderService;
import vn.javaweb.ComputerShop.service.product.ProductService;

import vn.javaweb.ComputerShop.component.MomoPayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("${api.prefix.products}")
public class ProductController {

    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;
    private final MomoPayment momoPayment;

    @GetMapping("")
    public ResponseEntity<ApiResponseT< List<ProductRpDTO>>> getProducts(@RequestParam( name = "search" , required = false) String search) {
        List<ProductRpDTO> listResult = this.productService.getAllProductView(search);
        return ResponseEntity.ok().body(ApiResponseT.<List<ProductRpDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(listResult)
                .build());
    }



    @GetMapping("/home")
    public ResponseEntity<ApiResponseT<Map<String, Object>>> getHomePageData() {
        // Lấy 8 sản phẩm bán chạy nhất (featured)
        List<ProductRpDTO> featuredProducts = productService.getFeaturedProducts(8);
        // Lấy 8 sản phẩm mới nhất (new arrivals)
        List<ProductRpDTO> newProducts = productService.getNewestProducts(8);

        Map<String, Object> homeData = new HashMap<>();
        homeData.put("featuredProducts", featuredProducts);
        homeData.put("newProducts", newProducts);

        return ResponseEntity.ok().body(ApiResponseT.<Map<String, Object>>builder()
                .status(HttpStatus.OK.value())
                .message("Home page data fetched successfully")
                .data(homeData)
                .build());
    }
    // Trong ProductController.java
    @GetMapping("/filter-options")
    public ResponseEntity<ApiResponseT<Map<String, List<String>>>> getFilterOptions() {
        List<String> factories = productService.getAllFactories();
        List<String> targets = productService.getAllTargets();

        Map<String, List<String>> filterOptions = new HashMap<>();
        filterOptions.put("factories", factories);
        filterOptions.put("targets", targets);

        return ResponseEntity.ok().body(ApiResponseT.<Map<String, List<String>>>builder()
                .status(HttpStatus.OK.value())
                .message("Filter options fetched successfully")
                .data(filterOptions)
                .build());
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponseT<Map<String, Object>>> getProductsPage(ProductFilterDTO productFilterDTO, HttpServletRequest request) {
        ProductFilterRpDTO result = this.productService.handleShowDataProductFilter(productFilterDTO);
        String qs = request.getQueryString();
        if (qs != null && !qs.isBlank()) {
            // remove page
            qs = qs.replace("page=" + result.getPage(), "");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("params", qs);
        data.put("products", result.getListProduct());
        data.put("currentPage", result.getPage());
        data.put("totalPages", result.getTotalPage());

        return ResponseEntity.ok().body(ApiResponseT.<Map<String, Object>>builder()
                .status(HttpStatus.OK.value())
                .message("Get product filter successfully")
                .data(data)
                .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseT<ProductDetailRpDTO>> getProductDetail(@PathVariable Long id) {
        ProductDetailRpDTO productDetail = this.productService.handleGetProductDetail(id);
        return ResponseEntity.ok().body(ApiResponseT.<ProductDetailRpDTO>builder()
                .status(HttpStatus.OK.value())
                .data(productDetail)
                .build());
    }


}
