package vn.javaweb.ComputerShop.domain.dto.response;


import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse  implements Serializable {

    private int status;
    private String message;

    private LocalDateTime timestamp = LocalDateTime.now();
    private String error;
    private List<ValidationError> errorDetails;
    private String path;

}
