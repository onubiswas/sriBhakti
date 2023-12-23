package co.sribhakti.api.shop.ctrl;

import co.sribhakti.api.common.ErrorCodes;
import co.sribhakti.api.common.ExceptionUtils;
import co.sribhakti.api.common.SriBhaktiApiResponse;
import co.sribhakti.api.common.SriBhaktiErrorResponse;
import co.sribhakti.api.shop.models.Product;
import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Log
@Component
public class ProductByIdCtrl {

    @Autowired
    Firestore db;

    @Autowired
    ExceptionUtils exceptionUtils;


    public SriBhaktiApiResponse run(String id) throws ExecutionException, InterruptedException {


        log.info("fetching product from database for given id");
        try {
            ApiFuture<DocumentSnapshot> promise = db.collection(Product.table)
                    .document(id)
                    .get();

            DocumentSnapshot doc = promise.get();

            Product product = doc.toObject(Product.class);

            log.info("successfully fetched product from database for given id");

            return new SriBhaktiApiResponse(product);

        } catch (Exception e) {
            if (exceptionUtils.indexOfType(NotFoundException.class, e)) {
                SriBhaktiErrorResponse err = SriBhaktiErrorResponse.builder().build();
                err.setMessage(String.format("product with given ID %s is not exist", id));
                err.setAppcode(ErrorCodes.ID_NOT_EXIST);
                return new SriBhaktiApiResponse(err);
            }
            throw e;

        }


    }
}
