package vn.javaweb.ComputerShop.domain.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiResponse {

    private int status;
    private String message;
    private Object data;

    public ApiResponse(int i, String localizedMessage) {
        this.status = i;
        this.message = localizedMessage;
    }

    public ApiResponse(String localizedMessage) {
        this.message = localizedMessage;
    }
}
