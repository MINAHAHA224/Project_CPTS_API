package vn.javaweb.ComputerShop.service;

import vn.javaweb.ComputerShop.domain.dto.request.OrderUpdateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;
import vn.javaweb.ComputerShop.domain.dto.response.OrderRpDTO;

import java.util.List;

public interface AdminOrderService {
    void handleUpdateOrder(OrderUpdateRqDTO orderUpdateRqDTO);
    void handleDeleteOrder(Long id);


    List<OrderRpDTO> handleGetOrders();
    OrderRpDTO handeGetOrder(Long id);

}
