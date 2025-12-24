package vn.javaweb.ComputerShop.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.javaweb.ComputerShop.component.MailerComponent;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.component.MomoPayment;
import vn.javaweb.ComputerShop.domain.dto.request.InfoOrderRqDTO;
import vn.javaweb.ComputerShop.domain.dto.request.momo.MomoRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.*;
import vn.javaweb.ComputerShop.domain.entity.*;
import vn.javaweb.ComputerShop.domain.enums.CartStatus;
import vn.javaweb.ComputerShop.domain.enums.OrderStatus;
import vn.javaweb.ComputerShop.domain.enums.PaymentStatus;
import vn.javaweb.ComputerShop.handleException.exceptions.AuthException;
import vn.javaweb.ComputerShop.handleException.exceptions.BusinessException;
import vn.javaweb.ComputerShop.handleException.exceptions.CartException;
import vn.javaweb.ComputerShop.handleException.exceptions.NotFoundException;
import vn.javaweb.ComputerShop.repository.CartDetailRepository;
import vn.javaweb.ComputerShop.repository.CartRepository;
import vn.javaweb.ComputerShop.repository.OrderDetailRepository;
import vn.javaweb.ComputerShop.repository.OrderRepository;
import vn.javaweb.ComputerShop.repository.ProductRepository;
import vn.javaweb.ComputerShop.repository.UserRepository;
import vn.javaweb.ComputerShop.service.CartService;
import vn.javaweb.ComputerShop.utils.SecurityUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final MailerComponent mailerComponent;
    private final MessageComponent messageComponent;
    private final MomoPayment momoPayment;

    @Override
    public Integer handleGetTotalQuantityInCart(String emailUser) {
        UserEntity user = this.userRepository.findUserEntityByEmail(emailUser)
                .orElseThrow(() -> new AuthException("User not found"));
        Optional<CartEntity> cartCurrent = this.cartRepository.findCartEntityByUserAndStatus(user, CartStatus.ACTIVE.toString());
        return cartCurrent.map(CartEntity::getSum).orElse(0);
    }

    @Override
    public void handleAddOneProductToCart(Long productId) {
        String email = SecurityUtils.getPrincipal();
        UserEntity user = this.userRepository.findUserEntityByEmail(email)
                .orElseThrow( () -> new AuthException("User not found") );

        Optional<CartEntity> cart = this.cartRepository.findCartEntityByUserAndStatus(user, CartStatus.ACTIVE.toString());
        ProductEntity product = this.productRepository.findProductEntityById(productId);
        if ( product == null ){
            throw new NotFoundException("Product not found");
        }

        if (cart.isEmpty()) {
            CartEntity otherCart = CartEntity.builder()
                    .sum(1)
                    .user(user)
                    .status(CartStatus.ACTIVE.toString())
                    .build();
            CartEntity newCart = this.cartRepository.save(otherCart);

            // set cart detail because first buy but one

            CartDetailEntity cartDetail = CartDetailEntity.builder()
                    .cart(newCart)
                    .product(product)
                    .price(product.getPrice())
                    .quantity(1)
                    .build();
            this.cartDetailRepository.save(cartDetail);
        } else {
            CartEntity cartCurrent = cart.get();
            Optional<CartDetailEntity> oldDetail = this.cartDetailRepository.findByCartAndProduct(cart.get(), product);
            if (oldDetail.isEmpty()) {
                CartDetailEntity cartDetail = CartDetailEntity.builder()
                        .cart(cart.get())
                        .product(product)
                        .price(product.getPrice())
                        .quantity(1)
                        .build();
                this.cartDetailRepository.save(cartDetail);

                // update sum in cart khi ma san pham no ko co thi moi +1 hien thi len gio hang
                int sumCurrentInCart = cartCurrent.getSum() + 1;
                cartCurrent.setSum(sumCurrentInCart);
                this.cartRepository.save(cartCurrent);

            } else {
                // con san pham no co roi thi thoi khong + 1 hien thi len gio hang cho du no add x10 so luon cua san pham do
                CartDetailEntity cartDetailCurrent = oldDetail.get();
                long quantityCurrent = cartDetailCurrent.getQuantity();
                cartDetailCurrent.setQuantity(quantityCurrent + 1);
                this.cartDetailRepository.save(cartDetailCurrent);
            }
        }
    }

    @Override
    public CartRpDTO handleGetCartDetail() {
        String email = SecurityUtils.getPrincipal();
        UserEntity userCurrent = this.userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));
      Optional<CartEntity>   cartEntity = this.cartRepository.findCartEntityByUserAndStatus(userCurrent, CartStatus.ACTIVE.toString());
                if (cartEntity.isEmpty()) {
                    return new CartRpDTO(new ArrayList<>(), 0);
                }
        CartEntity cart = cartEntity.get();
        List<CartDetailEntity> cartDetailsOfUser = cart.getCartDetails();
        List<CartDetailRpDTO> listCardDetailRpDTO = cartDetailsOfUser.stream()
                .map(cd ->  CartDetailRpDTO.builder()
                        .id(cd.getId())
                        .price(cd.getPrice())
                        .quantity(cd.getQuantity())
                        .stockQuantity(cd.getProduct().getQuantity())
                        .productId(cd.getProduct().getId())
                        .productName(cd.getProduct().getName())
                        .productImage(cd.getProduct().getImage())
                        .build()
                )
                .collect(Collectors.toList());
        double totalPrice = cartDetailsOfUser.stream()
                .mapToDouble(cd -> cd.getPrice() * cd.getQuantity())
                .sum();

        return new CartRpDTO(listCardDetailRpDTO, totalPrice);
    }


    @Override
    @Transactional
    public void handleDeleteProductInCart(Long id) {
        String email = SecurityUtils.getPrincipal();
        UserEntity userCurrent = this.userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));
        CartEntity cart = this.cartRepository.findCartEntityByUserAndStatus(userCurrent, CartStatus.ACTIVE.toString())
                .orElseThrow(() -> new CartException("Cart not found"));
        ProductEntity product = this.productRepository.findProductEntityById(id);
        if (product == null) {throw new NotFoundException("Product not found");}
        this.cartDetailRepository.deleteCartDetailEntityByCartAndProduct(cart, product);
        //set lai sum cua cart trong database
        cart.setSum(cart.getSum() > 0 ? cart.getSum() - 1 : 0);
        this.cartRepository.save(cart);
    }

    @Override
    public void handleDeleteOneProductInCart(Long id) {
        String email = SecurityUtils.getPrincipal();
        UserEntity userCurrent = this.userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));
        CartEntity cart = this.cartRepository.findCartEntityByUserAndStatus(userCurrent, CartStatus.ACTIVE.toString())
                .orElseThrow(() -> new CartException("Cart not found"));
        ProductEntity product = this.productRepository.findProductEntityById(id);
        if (product == null) {throw new NotFoundException("Product not found");}
        CartDetailEntity cartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new CartException("Cart detail not found"));
        if ( cartDetail.getQuantity() == 1 ) {
            this.cartDetailRepository.deleteCartDetailEntityByCartAndProduct(cart, product);
            cart.setSum(cart.getSum() > 0 ? cart.getSum() - 1 : 0);

            this.cartRepository.save(cart);
        }else {
            cartDetail.setQuantity(cartDetail.getQuantity() - 1 );
            this.cartDetailRepository.save(cartDetail);
        }
        //set lai sum cua cart trong database
    }


    @Override
    public CheckoutRpDTO handleShowDataAfterCheckout() {
        String email = SecurityUtils.getPrincipal();
        UserEntity currentUser = this.userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));
        CartEntity cart = this.cartRepository.findCartEntityByUserAndStatus(currentUser, CartStatus.ACTIVE.toString())
                .orElseThrow( () -> new CartException("Cart not found"));
        List<CartDetailEntity> cartDetails = cart.getCartDetails();

        List<CartDetailRpDTO> listCardDetailRpDTO = cartDetails.stream().map(
                cd -> CartDetailRpDTO.builder()
                        .id(cd.getId())
                        .price(cd.getPrice())
                        .quantity(cd.getQuantity())

                        .productId(cd.getProduct().getId())
                        .productName(cd.getProduct().getName())
                        .productImage(cd.getProduct().getImage())
                        .build()
        ).collect(Collectors.toList());
        double totalPrice = cartDetails.stream().mapToDouble(
                cd -> cd.getPrice() * cd.getQuantity()).sum();


        // show infor user to order place
        InfoOrderRqDTO infoOrderRqDTO = InfoOrderRqDTO.builder()
                .receiverName(currentUser.getFullName().trim())
                .receiverAddress(currentUser.getAddress() != null ? currentUser.getAddress() : "")
                .receiverPhone(currentUser.getPhone() != null ? currentUser.getPhone() : "")
                .totalPriceToSaveOrder(totalPrice)
                .build();
        return new CheckoutRpDTO(listCardDetailRpDTO, totalPrice, infoOrderRqDTO);
    }

    @Override
    @Transactional
    public PaymentDto handlePayment(InfoOrderRqDTO infoOrderRqDTO , Locale locale ) {
        String email = SecurityUtils.getPrincipal();
        UserEntity user = this.userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));
        String methodPayment = infoOrderRqDTO.getPaymentMethod();
        OrderEntity order = OrderEntity.builder()
                .user(user)
                .receiverName(infoOrderRqDTO.getReceiverName())
                .receiverAddress(infoOrderRqDTO.getReceiverAddress())
                .receiverPhone(infoOrderRqDTO.getReceiverPhone())
                .totalPrice(infoOrderRqDTO.getTotalPriceToSaveOrder())
                .status(OrderStatus.PENDING.toString())
                .time(new Date())
                .typePayment(infoOrderRqDTO.getPaymentMethod())
                .statusPayment(PaymentStatus.UNPAID.toString())
                .build();
        OrderEntity orderNew = this.orderRepository.save(order);
        // create orderDetail

        // step 1: get cart by user
        CartEntity cartCurrent = this.cartRepository.findCartEntityByUserAndStatus(user, CartStatus.ACTIVE.toString())
                .orElseThrow(() -> new CartException("Cart not found"));
        List<CartDetailEntity> cartDetails = cartCurrent.getCartDetails();
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        // step 2: handle update data
        cartDetails.forEach(
                cd -> {
                    OrderDetailEntity orderDetail = new OrderDetailEntity();
                    orderDetail.setOrder(orderNew);
                    orderDetail.setProduct(cd.getProduct());
                    orderDetail.setPrice(cd.getPrice() * cd.getQuantity());
                    orderDetail.setQuantity(cd.getQuantity());
                    orderDetails.add(orderDetail);
                    this.orderDetailRepository.save(orderDetail);
                    // set lai quantity cho product
                    ProductEntity productCurrent = cd.getProduct();
                    productCurrent.setSold(productCurrent.getSold() + cd.getQuantity());
                    this.productRepository.save(productCurrent);
                }
        );
        orderNew.setOrderDetails(orderDetails);
        OrderEntity orderForSendInvoice =    this.orderRepository.save(orderNew);

        if ("COD".equalsIgnoreCase(methodPayment)) {

            cartCurrent.setStatus(CartStatus.ORDERED.toString());
            this.cartRepository.save(cartCurrent);


            mailerComponent.handleSendInvoiceEmail(orderForSendInvoice);

            return  PaymentDto.builder()
                    .typePayment("COD")
                    .data(orderNew)
                    .build();
        }

        if ("MOMO".equalsIgnoreCase(methodPayment)) {
            MomoRpDTO momoResponse = this.momoPayment.generateMomoPayment(orderNew);
            if (momoResponse != null && momoResponse.getPayUrl() != null && momoResponse.getResultCode() == 0) {
                return  PaymentDto.builder()
                        .typePayment("MOMO")
                        .data(momoResponse)
                        .build();
            } else {
                throw  new BusinessException(messageComponent.getLocalizedMessage("client.cart.checkout.momo.errorDirection", locale));

            }
        }
        return null;
    }

    @Override
    public void handleAddProductDetailToCart(Long productId, Long quantity) {
        String email = SecurityUtils.getPrincipal();
        UserEntity user = this.userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));

        Optional<CartEntity> cart = this.cartRepository.findCartEntityByUserAndStatus(user, CartStatus.ACTIVE.toString());
        ProductEntity product = this.productRepository.findProductEntityById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        if (cart.isEmpty()) {
            CartEntity otherCart = new CartEntity();
            otherCart.setSum(1);
            otherCart.setUser(user);
            otherCart.setStatus(CartStatus.ACTIVE.toString());
            CartEntity newCart = this.cartRepository.save(otherCart);

            // set cart detail because first buy but one

            CartDetailEntity cartDetail = new CartDetailEntity();
            cartDetail.setCart(newCart);
            cartDetail.setProduct(product);
            cartDetail.setPrice(product.getPrice());
            cartDetail.setQuantity((int) quantity.longValue());
            this.cartDetailRepository.save(cartDetail);

        } else {
            CartEntity cartCurrent = cart.get();
            Optional<CartDetailEntity> oldDetail = this.cartDetailRepository.findByCartAndProduct(cart.get(), product);
            if (oldDetail.isEmpty()) {
                CartDetailEntity cartDetail = new CartDetailEntity();
                cartDetail.setCart(cart.get());
                cartDetail.setProduct(product);
                cartDetail.setPrice(product.getPrice());
                cartDetail.setQuantity((int) quantity.longValue());
                this.cartDetailRepository.save(cartDetail);

                // update sum in cart khi ma san pham no ko co thi moi +1 hien thi len gio hang
                cartCurrent.setSum(cartCurrent.getSum() + 1);
                this.cartRepository.save(cartCurrent);
            } else {
                // con san pham no co roi thi thoi khong + 1 hien thi len gio hang cho du no add x10 so luon cua san pham do
                CartDetailEntity cartDetailCurrent = oldDetail.get();
                long quantityCurrent = cartDetailCurrent.getQuantity();
                cartDetailCurrent.setQuantity(quantityCurrent + (int) quantity.longValue());
                this.cartDetailRepository.save(cartDetailCurrent);
            }
        }
    }

}
