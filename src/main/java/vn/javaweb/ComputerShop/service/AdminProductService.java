package vn.javaweb.ComputerShop.service;

import org.springframework.web.multipart.MultipartFile;
import vn.javaweb.ComputerShop.domain.dto.request.ProductCreateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.request.ProductUpdateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;
import vn.javaweb.ComputerShop.domain.dto.response.ProductDetailRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ProductFilterAdRpDTO;

import java.util.Locale;
import java.util.Optional;

public interface AdminProductService {
    ProductDetailRpDTO handleGetProduct(Long id);
    ProductFilterAdRpDTO handleGetProducts (Optional<String> pageOptional);

    void handleCreateProduct (ProductCreateRqDTO productCreateRqDTO , MultipartFile file  , Locale locale);
    void handleUpdateProduct (ProductUpdateRqDTO productUpdateRqDTO , MultipartFile file  , Locale locale);
    void handleDeleteProduct (Long id);

}
