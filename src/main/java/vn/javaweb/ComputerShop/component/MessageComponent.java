package vn.javaweb.ComputerShop.component;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageComponent {

    private final MessageSource messageSource;

    public String getLocalizedMessage(String messageId, Locale locale) {
        return messageSource.getMessage(messageId, null, locale);
    }
}
