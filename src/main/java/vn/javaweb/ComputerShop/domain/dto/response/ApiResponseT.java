    package vn.javaweb.ComputerShop.domain.dto.response;

    import lombok.*;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public class ApiResponseT <T> {
        private int status;
        private String message;
        private T data;
    }
