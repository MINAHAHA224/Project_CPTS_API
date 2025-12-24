package vn.javaweb.ComputerShop.controller.admin;

import java.util.List;
import java.util.Locale;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.OrderUpdateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponseT;
import vn.javaweb.ComputerShop.domain.dto.response.OrderRpDTO;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponse;
import vn.javaweb.ComputerShop.service.AdminOrderService;
import vn.javaweb.ComputerShop.service.OrderService;
import vn.javaweb.ComputerShop.utils.ConstantVariable;
import vn.javaweb.ComputerShop.utils.SecurityUtils;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "${api.prefix.admin.order}")
public class AdminOrderController {

    private final OrderService orderService;
    private final MessageComponent messageComponent;
    private final AdminOrderService adminOrderService;

    @GetMapping("/")
    public ResponseEntity<ApiResponseT<List<OrderRpDTO>>> getOrders(Locale locale) {
        log.info(ConstantVariable.ADMIN_ORDER + "Start getAllOrder at = {} ", SecurityUtils.currentTime);
        List<OrderRpDTO> data = this.adminOrderService.handleGetOrders();
        log.info(ConstantVariable.ADMIN_ORDER + "End getAllOrder at = {} ", SecurityUtils.currentTime);
        return ResponseEntity.ok().body(ApiResponseT.<List<OrderRpDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.order.get.all.success", locale))
                .data(data)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseT<OrderRpDTO>> getOrder(Locale locale, @PathVariable("id") long id) {
        log.info(ConstantVariable.ADMIN_ORDER + "Start getOrderDetail at {} -  with orderId = {}   ", SecurityUtils.currentTime, id);
        OrderRpDTO order = this.adminOrderService.handeGetOrder(id);
        log.info(ConstantVariable.ADMIN_ORDER + "End getOrderDetail at {} ", SecurityUtils.currentTime);
        return ResponseEntity.ok().body(ApiResponseT.<OrderRpDTO>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.order.get.detail.success", locale))
                .data(order)
                .build());
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseT<?>> updateOrder(Locale locale
            , @Valid @RequestBody OrderUpdateRqDTO orderView) {
        this.adminOrderService.handleUpdateOrder(orderView);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.order.update.success", locale))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseT<?>> getDeleteOrderPage(Locale locale, @PathVariable("id") long id) {
        log.info(ConstantVariable.ADMIN_ORDER + "Start getDeleteOrderPage at {} - orderId = {} ", SecurityUtils.currentTime, id);
        this.adminOrderService.handleDeleteOrder(id);
        log.info(ConstantVariable.ADMIN_ORDER + "End getDeleteOrderPage at {} ", SecurityUtils.currentTime);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.order.delete.success", locale))
                .build());
    }

}
