package e3i2.ecommerce_backoffice.common.interceptor;

import e3i2.ecommerce_backoffice.common.annotation.LoginSessionCheck;
import e3i2.ecommerce_backoffice.common.exception.ServiceErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static e3i2.ecommerce_backoffice.common.exception.ErrorEnum.ERR_NOT_LOGIN_ACCESS;
import static e3i2.ecommerce_backoffice.common.util.Constants.ADMIN_SESSION_NAME;

@Component
public class LoginSessionAccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LoginSessionCheck loginSessionCheck = handlerMethod.getMethodAnnotation(LoginSessionCheck.class);

            if (loginSessionCheck == null) {
                return true;
            }

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(ADMIN_SESSION_NAME) == null) {
                throw new ServiceErrorException(ERR_NOT_LOGIN_ACCESS);
            }

            return true;
        }

        return true;
    }
}
