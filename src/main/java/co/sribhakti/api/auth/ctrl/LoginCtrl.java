package co.sribhakti.api.auth.ctrl;

import co.sribhakti.api.auth.model.PhoneMapping;
import co.sribhakti.api.auth.model.TokenPayload;
import co.sribhakti.api.common.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;


@Component
public class LoginCtrl {

    @Autowired
    Firestore db;
    static Logger log = Logger.getLogger(LoginCtrl.class.getName());

    @Autowired
    ExceptionUtils exceptionUtils;

    @Autowired
    JwtTokenizer jwtTokenizer;

    public  SriBhaktiApiResponse run(LoginRequest body) throws ExecutionException, InterruptedException {

        SriBhaktiErrorResponse res = validate(body.getPhone());
        if(res != null) {
            return new SriBhaktiApiResponse(res);

        }
        return login(body);

    }

    private  SriBhaktiApiResponse login(LoginRequest body) throws ExecutionException, InterruptedException {
        SriBhaktiErrorResponse response = checkOtp(body);
        if(response != null) {
            return new SriBhaktiApiResponse(response);
        }

        TokenPayload payload = new TokenPayload();
        payload.setId(body.getUserId());
        payload.setPhone(body.getPhone());
        payload.setName(body.getName());


        String token = jwtTokenizer.JWTTokenCreation(payload);

        log.info("successfully created token");

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);

        return new SriBhaktiApiResponse(loginResponse);
    }

    private  SriBhaktiErrorResponse checkOtp(LoginRequest body) throws ExecutionException, InterruptedException {
        log.info("otp validation start");
        try {
            ApiFuture<DocumentSnapshot> promise = db.collection(PhoneMapping.table)
                    .document(body.getPhone())
                    .get();
            DocumentSnapshot doc = promise.get();

            PhoneMapping pm = doc.toObject(PhoneMapping.class);
            // check
            if(!body.getOtp().equals(pm.getOtp())) {
                return SriBhaktiErrorResponse.builder()
                        .message("otp does not match")
                        .appcode(ErrorCodes.INVALID_OTP)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .build();
            }

            // check if expired
            if(pm.getOtpExpiresAt() < System.currentTimeMillis()) {
                return SriBhaktiErrorResponse.builder()
                        .message("otp expired")
                        .appcode(ErrorCodes.OTP_EXPIRED)
                        .build();
            }
            body.setMp(pm);
            body.setUserId(pm.getId());


        } catch (Exception e) {
            if (exceptionUtils.indexOfType(NotFoundException.class, e)) {
                return phoneNotRegisteredError(body).errors;
            }
            throw e;
        }
        log.info("otp validation check successful");
        return null;
    }

    private static SriBhaktiApiResponse phoneNotRegisteredError(LoginRequest body) {
        SriBhaktiErrorResponse err = SriBhaktiErrorResponse.builder().build();
        err.setMessage(String.format("phone number %s is not registered", body.getPhone()));
        err.setAppcode(ErrorCodes.PHONE_NOT_REGISTERED);
        return new SriBhaktiApiResponse(err);

    }

    private static SriBhaktiErrorResponse validate(String phone) {

        log.info("request body validation start");

        HashMap<String, String> err = new HashMap<>();
        // TODO: advanced check is required
        if (phone.length() != 10) {
            err.put("phone", "not valid phone");
        }
        if(err.size() != 0) {
            return SriBhaktiErrorResponse.builder()
                    .errors(err)
                    .appcode(ErrorCodes.INVALID_REQUEST_BODY)
                    .build();

        }
        log.info("request body validation successful");
        return null;
    }

    @Data
    public static class LoginRequest {
        String phone;
        String otp;

        @JsonIgnore
        private String userId;

        @JsonIgnore
        private String name;

        @JsonIgnore
        private PhoneMapping mp;
    }

    @Data
    public static class LoginResponse {
        public String token;
    }
}
