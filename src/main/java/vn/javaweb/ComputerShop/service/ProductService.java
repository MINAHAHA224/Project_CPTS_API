package vn.javaweb.ComputerShop.service;

import org.springframework.web.multipart.MultipartFile;
import vn.javaweb.ComputerShop.domain.dto.request.CartDetailsListDTO;
import vn.javaweb.ComputerShop.domain.dto.request.ProductCreateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.request.ProductFilterDTO;
import vn.javaweb.ComputerShop.domain.dto.request.ProductUpdateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.response.*;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;

import java.util.List;
import java.util.Optional;

 public interface ProductService {
     void handleConfirmCheckout(CartDetailsListDTO cartDetailsListDTO);

     List<String> getAllFactories ();

     List<String> getAllTargets ();

     List<ProductRpDTO> getFeaturedProducts ( int limit );

     List<ProductRpDTO> getNewestProducts ( int limit );

     ProductFilterRpDTO handleShowDataProductFilter(ProductFilterDTO productFilterDTO);
     ProductUpdateRqDTO handleGetProductUpdate (Long id);
     List<ProductRpDTO> getAllProductView(String search);
     ProductDetailRpDTO handleGetProductDetail(long id);
}
