package co.sribhakti.api.shop.handlers;

import co.sribhakti.api.common.SriBhaktiApiResponse;
import co.sribhakti.api.shop.ctrl.ProductListCtrl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log
public class ListProductHandler {

    @Autowired
    ProductListCtrl listProductCtrl;



    @GetMapping(value = "/v1/products",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> productList() {
        log.info("list products request start");

        SriBhaktiApiResponse response = listProductCtrl.run();

        log.info("list products request ends");

        if(response.errors != null) {
            return new ResponseEntity<>(response.errors, response.errors.getStatusCode());
        }
        return new ResponseEntity<>(response.success, response.successCode);
    }

}
