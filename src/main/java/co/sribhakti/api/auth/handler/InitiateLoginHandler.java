package co.sribhakti.api.auth.handler;

import co.sribhakti.api.auth.ctrl.InitiateLoginCtrl;
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
public class InitiateLoginHandler {


    private final Logger log = Logger.getLogger(InitiateLoginHandler.class.getName());

    @Autowired
    InitiateLoginCtrl handlerCtrl;

    @PostMapping(value = "/v1/initiate-login",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initateLogin(@RequestBody InitiateLoginCtrl.InitiateLoginRequest body) throws ExecutionException, InterruptedException {
        log.info("initiate login request start");

        SriBhaktiApiResponse response = handlerCtrl.run(body);

        log.info("initiate login ends");

        if(response.errors != null) {
            return new ResponseEntity<>(response.errors, response.errors.getStatusCode());
        }
        return new ResponseEntity<>(response.success, response.successCode);

    }

}
