package vn.javaweb.ComputerShop.service.Impl;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.*;
import vn.javaweb.ComputerShop.domain.dto.response.*;
import vn.javaweb.ComputerShop.domain.entity.CartDetailEntity;
import vn.javaweb.ComputerShop.domain.entity.ProductEntity;
import vn.javaweb.ComputerShop.handleException.exceptions.BusinessException;
import vn.javaweb.ComputerShop.handleException.exceptions.NotFoundException;
import vn.javaweb.ComputerShop.repository.CartDetailRepository;
import vn.javaweb.ComputerShop.repository.CartRepository;
import vn.javaweb.ComputerShop.repository.ProductRepository;
import vn.javaweb.ComputerShop.service.AdminProductService;
import vn.javaweb.ComputerShop.service.ProductService;
import vn.javaweb.ComputerShop.service.UploadService;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService , AdminProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UploadService uploadService;
    private final MessageComponent messageComponent;


   @Override
    public ProductFilterRpDTO handleShowDataProductFilter(ProductFilterDTO productFilterDTO) {
        int page = 1;
        try {
            if (!productFilterDTO.getPage().isEmpty()) {
                page = Integer.parseInt(productFilterDTO.getPage());
            } else {
                page = 1;
            }
        } catch (Exception e) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, 7);

        ProductFilterRpDTO result = new ProductFilterRpDTO();
        Page<ProductRpDTO> listProduct = this.productRepository.findProductFilter(productFilterDTO, pageable);
        result.setListProduct(listProduct.getContent());
        result.setPage(page);
        result.setTotalPage(listProduct.getTotalPages());
        return result;
    }


    @Override
    public ProductFilterAdRpDTO handleGetProducts(Optional<String> pageOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            } else {
                page = 1;
            }
        } catch (Exception e) {
            page = 1;
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<ProductAdRpDTO> listProducts = this.productRepository.findProducts(pageable);
        return ProductFilterAdRpDTO.builder()
                .listProduct(listProducts.getContent())
                .page(page)
                .totalPage(listProducts.getTotalPages())
                .build();
    }


    @Override
    public ProductUpdateRqDTO handleGetProductUpdate(Long id) {
        ProductEntity product = this.productRepository.findProductEntityById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return ProductUpdateRqDTO.builder()
                .id(product.getId())
                .image(product.getImage())
                .name(product.getName())
                .factory(product.getFactory())
                .price(product.getPrice())
                .detailDesc(product.getDetailDesc())
                .shortDesc(product.getShortDesc())
                .quantity(product.getQuantity())
                .target(product.getTarget())
                .sold(product.getSold())
                .build();

    }
    @Override
    public void handleCreateProduct (ProductCreateRqDTO productCreateRqDTO , MultipartFile file , Locale locale){
        String avatarProduct = this.uploadService.handleUploadFile(file, "product");
        boolean checkExistName = this.productRepository.existsProductEntityByName(productCreateRqDTO.getName().trim());
        if ( checkExistName){
            throw new BusinessException(messageComponent.getLocalizedMessage("admin.product.create.name.exist", locale));
        }
        ProductEntity newProduct = ProductEntity.builder()
                .name(productCreateRqDTO.getName().trim())
                .price(productCreateRqDTO.getPrice())
                .detailDesc(productCreateRqDTO.getDetailDesc().trim())
                .shortDesc(productCreateRqDTO.getShortDesc().trim())
                .quantity(productCreateRqDTO.getQuantity())
                .factory(productCreateRqDTO.getFactory().trim())
                .target(productCreateRqDTO.getFactory())
                .image(avatarProduct)
                .sold(0)
                .build();
        this.productRepository.save(newProduct);
    }

    @Override
    public void handleUpdateProduct (ProductUpdateRqDTO productUpdateRqDTO , MultipartFile file  , Locale locale){
        ProductEntity currentProduct = this.productRepository.findProductEntityById(productUpdateRqDTO.getId());
        if ( !currentProduct.getName().equals(productUpdateRqDTO.getName())){
            boolean checkExistName = this.productRepository.existsProductEntityByName(productUpdateRqDTO.getName().trim());
            if ( checkExistName){
                throw  new BusinessException(messageComponent.getLocalizedMessage("admin.product.create.name.exist" , locale));
            }
        }

        if (file!=null && !Objects.equals(file.getOriginalFilename(), "")){
            String  avatarProduct = this.uploadService.handleUploadFile(file, "product");
            currentProduct.setImage(avatarProduct);
        }
        currentProduct.setName(productUpdateRqDTO.getName().trim());
        currentProduct.setPrice(productUpdateRqDTO.getPrice());
        currentProduct.setDetailDesc(productUpdateRqDTO.getDetailDesc().trim());
        currentProduct.setShortDesc(productUpdateRqDTO.getShortDesc().trim());
        currentProduct.setQuantity(productUpdateRqDTO.getQuantity());
        currentProduct.setFactory(productUpdateRqDTO.getFactory().trim());
        currentProduct.setTarget(productUpdateRqDTO.getFactory());
        currentProduct.setSold(productUpdateRqDTO.getSold());
        this.productRepository.save(currentProduct);
    }


    @Override
    public List<ProductRpDTO> getAllProductView(String search) {
        List<ProductEntity> allProduct =  new ArrayList<>();
        if ( search == null ||search.isEmpty()){
            allProduct = this.productRepository.findAll();
        }else {
            String searchText = search.trim();
            allProduct = this.productRepository.findAllProductEntityByName(searchText);
        }
        return allProduct.stream().map(
                pd -> ProductRpDTO.builder()
                        .id(pd.getId())
                        .name(pd.getName())
                        .shortDesc(pd.getShortDesc())
                        .price(pd.getPrice())
                        .image(pd.getImage())
                        .build()
        ).collect(Collectors.toList());
    }


    @Override
    public void handleDeleteProduct (Long id){
        ProductEntity product = this.productRepository.findProductEntityById(id);
        if (product == null){
            throw new NotFoundException("Product not found");
        }
        this.productRepository.deleteProductEntityById(id);
    }

    @Override
    public ProductDetailRpDTO handleGetProduct(Long id) {
        ProductEntity product = this.productRepository.findProductEntityById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return ProductDetailRpDTO.builder()
                .id(product.getId())
                .image(product.getImage())
                .name(product.getName())
                .factory(product.getFactory())
                .price(product.getPrice())
                .detailDesc(product.getDetailDesc())
                .shortDesc(product.getShortDesc())
                .quantity(product.getQuantity())
                .target(product.getTarget())
                .sold(product.getSold())
                .build();
    }

    @Override
    public ProductDetailRpDTO handleGetProductDetail(long id) {
        ProductEntity entity = this.productRepository.findProductEntityById(id);
        if (entity == null) {
            throw new NotFoundException("Product not found");
        }
        return ProductDetailRpDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .image(entity.getImage())
                .detailDesc(entity.getDetailDesc())
                .shortDesc(entity.getShortDesc())
                .price(entity.getPrice())
                .factory(entity.getFactory())
                .quantity(entity.getQuantity())
                .build();
    }



    @Override
    @Transactional
    public void handleConfirmCheckout(CartDetailsListDTO cartDetailsListDTO) {
        List<CartDetailOneRqDTO> listResult = cartDetailsListDTO.getCartDetailOne();
        listResult.forEach(cartDetailOneRqDTO -> {
            CartDetailEntity cartDetail = this.cartDetailRepository.findCartDetailEntityById(cartDetailOneRqDTO.getId());
            cartDetail.setQuantity(cartDetailOneRqDTO.getQuantity());
            this.cartDetailRepository.save(cartDetail);
        });

    }

    @Override
    public List<String> getAllFactories() {
        return this.productRepository.findAllFactories();
    }

    @Override
    public List<String> getAllTargets() {
        return this.productRepository.findAllTargets();
    }

    @Override
    public List<ProductRpDTO> getFeaturedProducts(int limit) {
        List<ProductEntity> featuredProducts = this.productRepository.getFeaturedProducts(8);
        return featuredProducts.stream().map(it -> ProductRpDTO.builder()
                .id(it.getId())
                .name(it.getName())
                .image(it.getImage())
                .shortDesc(it.getShortDesc())
                .price(it.getPrice())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<ProductRpDTO> getNewestProducts(int limit) {
       List<ProductEntity> productEntities = this.productRepository.getNewestProduct(8);
       return productEntities.stream().map(it -> ProductRpDTO.builder()
               .id(it.getId())
               .name(it.getName())
               .image(it.getImage())
               .shortDesc(it.getShortDesc())
               .price(it.getPrice())
               .build()).collect(Collectors.toList());
    }

}
