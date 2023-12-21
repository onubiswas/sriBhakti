package co.sribhakti.api.shop.handlers;


import co.sribhakti.api.common.SriBhaktiApiResponse;
import co.sribhakti.api.shop.ctrl.CreateProductCtrl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class CreateProductHandler {

    @Autowired
    CreateProductCtrl productCtrl;


    @PostMapping(value = "/v1/products",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> create(@RequestBody CreateProductCtrl.productRequest body) {
        log.info("create product api start");

        SriBhaktiApiResponse response = productCtrl.run(body);

        log.info("create product api ends");

        if(response.errors != null)
            return new ResponseEntity<>(response.errors, response.errors.getStatusCode());
        return new ResponseEntity<>(response.success, response.successCode);
    }
}
