package co.sribhakti.api.auth.handler;


import co.sribhakti.api.auth.ctrl.RegisterCtrl;
import co.sribhakti.api.common.SriBhaktiApiResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@RestController
public class RegisterHandler {

    private final Logger log = Logger.getLogger(RegisterHandler.class.getName());
    @Autowired
    private RegisterCtrl ctrl;

    @PostMapping(value = "/v1/register",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody RegisterCtrl.RegisterRequest body) throws ExecutionException, InterruptedException {
        log.info("registration request start");

        SriBhaktiApiResponse response = ctrl.run(body);

        log.info("registration request ends");
        if(response.errors != null) {
            return new ResponseEntity<>(response.errors, response.errors.getStatusCode());
        }
        return new ResponseEntity<>(response.success, response.successCode);

    }






}
