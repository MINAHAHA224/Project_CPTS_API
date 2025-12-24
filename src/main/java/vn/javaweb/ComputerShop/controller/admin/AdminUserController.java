package vn.javaweb.ComputerShop.controller.admin;

import java.util.List;
import java.util.Locale;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.UserCreateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.response.*;
import vn.javaweb.ComputerShop.service.AdminService;
import vn.javaweb.ComputerShop.service.UserService;
import vn.javaweb.ComputerShop.utils.ConstantVariable;
import vn.javaweb.ComputerShop.utils.SecurityUtils;


@RestController
@RequiredArgsConstructor
@RequestMapping(value ="${api.prefix.admin}")
@Slf4j
public class AccountController {
    private final UserService userService;
    private final AdminService adminService;
    private final MessageComponent messageComponent;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponseT<DashboardDTO>> getDashboard(Locale locale) {
        log.info(ConstantVariable.ADMIN_PAGE + "Start getDashboard at {} " , SecurityUtils.currentTime);
        DashboardDTO result = this.adminService.handleGetDashboard();
        log.info(ConstantVariable.ADMIN_PAGE + "Start getDashboard at {} " , SecurityUtils.currentTime);
        return  ResponseEntity.ok().body(ApiResponseT.<DashboardDTO>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.dashboard.get.success", locale))
                .data(result)
                .build());
    }



    @GetMapping("/accounts")
    public ResponseEntity<ApiResponseT<List<AccountDTO>>>  getAccounts(Locale locale) {
        log.info(ConstantVariable.ADMIN_USER + "Start showAllUserPage at {} ", SecurityUtils.currentTime);
        List<AccountDTO> listAccount = this.adminService.handleGetAccounts();
        log.info(ConstantVariable.ADMIN_USER + "End showAllUserPage at {} ", SecurityUtils.currentTime);
        return ResponseEntity.ok().body(ApiResponseT.<List<AccountDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.user.list.get.success", locale))
                .data(listAccount)
                .build());
    }



    @PostMapping("/account")
    public ResponseEntity<ApiResponseT<?>> createAccount(Locale locale, @Valid @RequestBody UserCreateRqDTO userCreateRqDTO,
                                 @RequestParam("avatarFile") MultipartFile file) {
        log.info(ConstantVariable.ADMIN_USER + "Start postCreateUser at {} ", SecurityUtils.currentTime);
       this.adminService.handleCreateAccount(userCreateRqDTO, file , locale);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.user.create.success", locale))
                .build());

    }


    @GetMapping("/account/{id}")
    public ResponseEntity<ApiResponseT<AccountDetailDTO>> detailAccount(@PathVariable("id") Long id , Locale locale) {
        log.info(ConstantVariable.ADMIN_USER + "Start getDetailUserPage at {} - with userId : {} ", SecurityUtils.currentTime, id);
        AccountDetailDTO accountDetail = this.adminService.handleGetAccountDetail(id);
        log.info(ConstantVariable.ADMIN_USER + "End getDetailUserPage at {} ", SecurityUtils.currentTime);
        return ResponseEntity.ok().body(ApiResponseT.<AccountDetailDTO>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.user.detail.get.success", locale))
                .data(accountDetail)
                .build());
    }


    @PatchMapping ("/account/{id}")
    public ResponseEntity<ApiResponseT<?>> postUpdateUserPage(@Valid @RequestBody AccountDetailDTO updateAccount,
                                     @RequestParam("avatarFile") MultipartFile file , Locale locale) {
        log.info(ConstantVariable.ADMIN_USER + "Start postUpdateUserPage at {} - with userId : {} ", SecurityUtils.currentTime, updateAccount.getId());
        this.adminService.handleUpdateAccount(updateAccount, file , locale);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.user.update.success", locale))
                .build());
    }

    @DeleteMapping ("/account/{id}")
    public ResponseEntity<ApiResponseT<?>> getDeletePage(Locale locale, @PathVariable("id") Long id) {
        log.info(ConstantVariable.ADMIN_USER + "Start getDeletePage at {} - with userId : {} ", SecurityUtils.currentTime, id);
        this.adminService.handleDeleteAccount(id);
        log.info(ConstantVariable.ADMIN_USER + "End getDeletePage at {}  ", SecurityUtils.currentTime);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("admin.user.delete.success", locale))
                .build());

    }


}
