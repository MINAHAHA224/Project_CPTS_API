package vn.javaweb.ComputerShop.service;


import org.springframework.web.multipart.MultipartFile;
import vn.javaweb.ComputerShop.domain.dto.request.UserCreateRqDTO;
import vn.javaweb.ComputerShop.domain.dto.response.AccountDTO;
import vn.javaweb.ComputerShop.domain.dto.response.AccountDetailDTO;
import vn.javaweb.ComputerShop.domain.dto.response.DashboardDTO;

import java.util.List;
import java.util.Locale;

public interface AdminUserService {
        DashboardDTO handleGetDashboard();
        List<AccountDTO> handleGetAccounts();
        AccountDetailDTO handleGetAccount(Long id);

        void handleCreateAccount (UserCreateRqDTO userCreateRqDTO , MultipartFile file  , Locale locale);
        void handleUpdateAccount (AccountDetailDTO userUpdateRqDTO , MultipartFile file , Locale locale);
        void handleDeleteAccount (Long id);

}
