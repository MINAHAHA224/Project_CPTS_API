    package vn.javaweb.ComputerShop.service.cart;

    import vn.javaweb.ComputerShop.domain.dto.request.InfoOrderRqDTO;
    import vn.javaweb.ComputerShop.domain.dto.response.CartRpDTO;
    import vn.javaweb.ComputerShop.domain.dto.response.PaymentDto;
    import vn.javaweb.ComputerShop.domain.dto.response.CheckoutRpDTO;

    import java.util.Locale;

     public interface CartService {

         Integer handleGetTotalQuantityInCart (String emailUser);
         void handleAddOneProductToCart( Long productId );
         void handleAddProductDetailToCart(Long productId,  Long quantity);


         CartRpDTO handleGetCartDetail ();
         void handleDeleteProductInCart(Long id);
         void handleDeleteOneProductInCart(Long id);

         CheckoutRpDTO handleShowDataAfterCheckout ();


         PaymentDto handlePayment(InfoOrderRqDTO infoOrderRqDTO , Locale locale) ;
    }
