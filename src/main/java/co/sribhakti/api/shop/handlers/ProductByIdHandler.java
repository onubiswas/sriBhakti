package co.sribhakti.api.shop.handlers;


import co.sribhakti.api.common.SriBhaktiApiResponse;
import co.sribhakti.api.shop.ctrl.ProductByIdCtrl;
import co.sribhakti.api.shop.ctrl.ProductListCtrl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@Log
public class ProductByIdHandler {


    @Autowired
    ProductByIdCtrl productByIdCtrl;

    @GetMapping(value = "/v1/products/{id}",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> product(@PathVariable String id) throws ExecutionException, InterruptedException {

        log.info("Product details api request start");

        SriBhaktiApiResponse response = productByIdCtrl.run(id);

        log.info("Product details api request ends");

        if(response.errors != null)
            return new ResponseEntity<>(response.errors, response.errors.getStatusCode());
        return new ResponseEntity<>(response.success, response.successCode);
    }
}
