package co.sribhakti.api.auth.ctrl;

import co.sribhakti.api.auth.handler.InitiateLoginHandler;
import co.sribhakti.api.auth.model.PhoneMapping;
import co.sribhakti.api.auth.model.UserAccount;
import co.sribhakti.api.common.*;
import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Component
public class InitiateLoginCtrl {
    @Autowired
    Firestore db;

    @Autowired
    ExceptionUtils exceptionUtils;


    private final Logger log =  Logger.getLogger(InitiateLoginCtrl.class.getName());


    public SriBhaktiApiResponse run(InitiateLoginRequest r) throws ExecutionException, InterruptedException {

        SriBhaktiErrorResponse err = validate(r);
        if(null != err) {
            return new SriBhaktiApiResponse(err);
        }

        return initiateLogin(r);
    }

    SriBhaktiApiResponse initiateLogin(InitiateLoginRequest r) throws ExecutionException, InterruptedException {

        try {
            OtpGenerator otpGenerator = new OtpGenerator(4);
            String otp = otpGenerator.generateOtp();

            long expiryTs = System.currentTimeMillis();
            expiryTs += 5 * 60 * 1000; // 5 minutes

            HashMap<String, Object> updates = new HashMap<>();
            updates.put("otp", otp);
            updates.put("otpExpiresAt", expiryTs);

            ApiFuture<WriteResult> promise = db.collection(PhoneMapping.table)
                    .document(r.getPhone())
                    .update(updates);

            promise.get(); // block until done


        } catch (Exception e) {
            if (exceptionUtils.indexOfType(NotFoundException.class, e)) {
                return phoneNotRegisteredError(r);
            }

            // internal server error
            throw e;
        }

        return new SriBhaktiApiResponse(InitiateLoginResponse.ok());



    }

    private SriBhaktiApiResponse phoneNotRegisteredError(InitiateLoginRequest r) {
        SriBhaktiErrorResponse err = SriBhaktiErrorResponse.builder()
                .message(String.format("phone number %s is not registered", r.getPhone()))
                .appcode(ErrorCodes.PHONE_NOT_REGISTERED)
                .statusCode(HttpStatus.UNAUTHORIZED)
                .build();
        return new SriBhaktiApiResponse(err);
    }



    SriBhaktiErrorResponse validate(InitiateLoginRequest request) {
        log.info("request body validation start");

        HashMap<String, String> err = request.validate();

        if(err.size() != 0) {
            log.info("request body validation failed");
            return SriBhaktiErrorResponse.builder()
                    .errors(err)
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .message("invalid request body")
                    .appcode(ErrorCodes.INVALID_REQUEST_BODY)
                    .build();
        }
        log.info("request body validation successful");

        return null;

    }
    @Data
    public static class InitiateLoginRequest {
        String phone;

        public HashMap<String, String> validate() {
            HashMap<String, String> errors = new HashMap<>();

            if (phone.length() != 10) {
                errors.put("phone", "not valid phone");
            }
            // TODO: advanced check is required

            return errors ;
        }

    }

    @Data
    public static class InitiateLoginResponse {
        public String message;

        public static InitiateLoginResponse ok() {
            InitiateLoginResponse ok = new InitiateLoginResponse();
            ok.message = "ok";
            return ok;
        }


    }
}
