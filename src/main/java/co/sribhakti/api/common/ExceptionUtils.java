package co.sribhakti.api.common;


import org.springframework.stereotype.Component;

@Component
public class ExceptionUtils {
    public boolean indexOfType(Class<?> clazz, Throwable throwable) {
        Throwable cause = throwable.getCause();

        while (cause != null) {
            if (clazz.isInstance(cause)) {
                return true;
            }
            cause = cause.getCause();
        }

        return false;
    }
}
