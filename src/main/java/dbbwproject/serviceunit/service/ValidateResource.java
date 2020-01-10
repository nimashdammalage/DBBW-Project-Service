package dbbwproject.serviceunit.service;

import org.springframework.web.client.ResourceAccessException;

import java.util.function.Supplier;

public class ValidateResource {
    public static void valArg(boolean isTrue, String err) {
        if (isTrue) throw new ResourceAccessException(err);
    }

    public static Supplier<ResourceAccessException> throwEx(String str) {
        return () -> new ResourceAccessException(str);
    }
}
