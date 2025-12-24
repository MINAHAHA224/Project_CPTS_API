package vn.javaweb.ComputerShop.service;

import vn.javaweb.ComputerShop.domain.dto.request.OrderUpdateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.request.momo.MomoRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.OrderRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;

import java.util.List;
import java.util.Locale;

public interface OrderService {

     List<OrderRpDTO> handleGetDataOrderOfUser() ;
     void handleMomoCheckout(MomoRpDTO momoRpDTO , Locale locale);
}
