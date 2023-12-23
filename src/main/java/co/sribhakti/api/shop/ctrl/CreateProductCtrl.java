package co.sribhakti.api.shop.ctrl;

import co.sribhakti.api.common.ExceptionUtils;
import co.sribhakti.api.common.SriBhaktiApiResponse;


import co.sribhakti.api.shop.models.Product;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log
@Component
public class CreateProductCtrl {
    @Autowired
    Firestore db;

    @Autowired
    ExceptionUtils exceptionUtils;

    public SriBhaktiApiResponse run(productRequest body) {

        Product product = new Product();
        String uniqueID = UUID.randomUUID().toString();
        product.setId(uniqueID);
        product.setName(body.name);
        product.setPrice(body.price);
        product.setDescription(body.description);
        product.setAvailableQuantity(body.quantity);

        log.info("saving product in the database");

        ApiFuture<WriteResult> res = db.collection(Product.table).document(product.getId()).create(product);


        log.info("saved successfully in database");


        productResponse rr = new productResponse(uniqueID);
        return new SriBhaktiApiResponse(rr);
    }

    public class productRequest {
        String name;
        String description;
        String price;

        Integer quantity;

        private String image;
    }

    class productResponse {
        String id;
        productResponse(String id) {
            this.id = id;
        }
    }


}
