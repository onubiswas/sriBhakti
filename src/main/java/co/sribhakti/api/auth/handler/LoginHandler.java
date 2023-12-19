package co.sribhakti.api.auth.handler;


import co.sribhakti.api.auth.ctrl.LoginCtrl;
import co.sribhakti.api.common.SriBhaktiApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@RestController
public class LoginHandler {

    private final Logger log = Logger.getLogger(LoginHandler.class.getName());

    @Autowired
    LoginCtrl ctrl;

    @PostMapping(value = "/v1/login",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginCtrl.LoginRequest body) throws ExecutionException, InterruptedException {
        log.info("login request start");

        SriBhaktiApiResponse response = ctrl.run(body);

        log.info("login request ends");

        if(response.errors != null) {
            return new ResponseEntity<>(response.errors, response.errors.getStatusCode());
        }


        return new ResponseEntity<>(response.success, response.successCode);

    }



}
