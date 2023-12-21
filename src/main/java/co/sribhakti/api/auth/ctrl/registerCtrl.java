package co.sribhakti.api.auth.ctrl;

import co.sribhakti.api.auth.models.PhoneMapping;
import co.sribhakti.api.auth.models.UserAccount;
import co.sribhakti.api.common.*;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.AlreadyExistsException;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Component
public class RegisterCtrl {

    @Autowired
    Firestore db;

    @Autowired
    ExceptionUtils exceptionUtils;

    private final Logger log = Logger.getLogger(RegisterCtrl.class.getName());

    public SriBhaktiApiResponse run(RegisterRequest r) throws ExecutionException, InterruptedException {

        SriBhaktiErrorResponse err = validate(r);
        if(err != null) {
            log.info("request body validation has failed" );
            return new SriBhaktiApiResponse(err);
        }

        return register(r);
    }

    private SriBhaktiApiResponse register(RegisterRequest r) throws ExecutionException, InterruptedException {
        String uniqueID = UUID.randomUUID().toString();
        PhoneMapping ph = new PhoneMapping(r.phone);
        UserAccount userAccount = new UserAccount();

        OtpGenerator otpGenerator = new OtpGenerator(4);
        String otp = otpGenerator.generateOtp();
        long otpExpire = System.currentTimeMillis() + (5 * 60 * 1000);

        ph.setId(uniqueID);
        ph.setOtp(otp);
        ph.setCreatedAt(System.currentTimeMillis());
        ph.setOtpExpiresAt(otpExpire);

        userAccount.setPhone(r.phone);
        userAccount.setId(uniqueID);
        userAccount.setCreatedAt(System.currentTimeMillis());
        userAccount.setUpdatedAt(System.currentTimeMillis());
        userAccount.setName(r.name);
        userAccount.setEmail(null);

        //database save
        try {
            ApiFuture<Void> res = db.runTransaction(transaction -> {
                DocumentReference phoneRef = db.collection(PhoneMapping.table).document(ph.getPhone());
                DocumentReference accountRef = db.collection(UserAccount.table).document(userAccount.getId());

                transaction = transaction.create(phoneRef, ph);

                transaction.create(accountRef, userAccount);
                return null;
            });

            res.get();
        }
        catch (Exception e) {
            if (exceptionUtils.indexOfType(AlreadyExistsException.class, e)) {
                return duplicateUserCreate();
            }
            System.out.println(e);

            throw e;

        }

        RegisterResponse rr = new RegisterResponse("ok");
        return new SriBhaktiApiResponse(rr);
    }

    private SriBhaktiApiResponse duplicateUserCreate() {
        HashMap<String, String> errors = new HashMap<String, String>();
        errors.put("phone", "phone number already registered");
        SriBhaktiErrorResponse err = SriBhaktiErrorResponse.builder().build();
        err.setErrors(errors);
        err.setMessage("phone number already registered");
        err.setAppcode(1); // TODO

        return new SriBhaktiApiResponse(err);
    }

    public SriBhaktiErrorResponse validate(RegisterRequest r) {
        log.info("request body validation start");

        HashMap<String, String> err = r.validate();
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
    public static class RegisterRequest {
        String name;
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
    public static class RegisterResponse {
        public String status;

        public RegisterResponse(String ok) {
            status = ok;
        }
    }
}
