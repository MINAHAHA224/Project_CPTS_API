package vn.javaweb.ComputerShop.service.product;

import org.springframework.web.multipart.MultipartFile;
import vn.javaweb.ComputerShop.domain.dto.request.CartDetailsListDTO;
import vn.javaweb.ComputerShop.domain.dto.request.ProductCreateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.request.ProductFilterDTO;
import vn.javaweb.ComputerShop.domain.dto.request.ProductUpdateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.response.*;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

 public interface ProductService {
     void handleConfirmCheckout(CartDetailsListDTO cartDetailsListDTO);

     List<String> getAllFactories ();

     List<String> getAllTargets ();

     List<ProductRpDTO> getFeaturedProducts ( int limit );

     List<ProductRpDTO> getNewestProducts ( int limit );

     ProductFilterRpDTO handleShowDataProductFilter(ProductFilterDTO productFilterDTO);
     ProductFilterAdRpDTO handleShowDataProductAdmin(Optional<String> pageOptional);
     ProductUpdateRqDTO handleGetProductUpdate (Long id);
     ApiResponse handleCreateProduct (ProductCreateRqDTO productCreateRqDTO , MultipartFile file);
     ApiResponse handleUpdateProduct (ProductUpdateRqDTO productUpdateRqDTO , MultipartFile file);
     List<ProductRpDTO> getAllProductView();
     ApiResponse handleDeleteProduct (Long id);
     ProductDetailRpDTO handleGetProductRpAdmin(Long id);
     ProductDetailRpDTO handleGetProductDetail(long id);
}
