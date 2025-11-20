package vn.javaweb.ComputerShop.service.order;

import jakarta.servlet.http.HttpSession;
import vn.javaweb.ComputerShop.domain.dto.request.InfoOrderRqDTO;
import vn.javaweb.ComputerShop.domain.dto.request.OrderUpdateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.request.momo.MomoRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.OrderRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;

import java.util.List;
import java.util.Locale;

public interface OrderService {

     List<OrderRpDTO> handleGetDataOrderOfUser() ;
     List<OrderRpDTO> handleGetOrderAd();
     OrderRpDTO handeGetOrderDetailAd(Long id);
     OrderUpdateRqDTO handleGetOrderRqAd(Long id);
     ApiResponse handleUpdateOrderRqAd(OrderUpdateRqDTO orderUpdateRqDTO);
     ApiResponse handleDeleteOrder(Long id);
     void handleMomoCheckout(MomoRpDTO momoRpDTO , Locale locale);
}
