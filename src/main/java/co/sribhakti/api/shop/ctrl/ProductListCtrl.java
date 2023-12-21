package co.sribhakti.api.shop.ctrl;


import co.sribhakti.api.common.ExceptionUtils;
import co.sribhakti.api.common.SriBhaktiApiResponse;
import co.sribhakti.api.shop.models.Product;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;



@Component
@Log
public class ProductListCtrl {

    @Autowired
    Firestore db;

    public SriBhaktiApiResponse run() {

        ArrayList<Product> productList = new ArrayList<>();

        log.info("fetching product list from db");

        try {
            // Reference to the "product" collection in Firestore
            CollectionReference productsCollection = db.collection("product");

            // Fetch all documents in the "product" collection
            ApiFuture<QuerySnapshot> querySnapshot = productsCollection.get();

            // Retrieve the documents and map them to Product objects
            for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
                Product product = document.toObject(Product.class);
                productList.add(product);
            }

            log.info("Product list fetched successfully");

        } catch (InterruptedException | ExecutionException e) {
            log.info("Failed to fetch product list: {}");
            // Handle exceptions
        }

        ProductListCtrl.productListResponse res = new ProductListCtrl.productListResponse();
        res.setProductList(productList);
        res.setCount(productList.size());

        return new SriBhaktiApiResponse(res);
    }

    @Data
    public static class productListResponse {
        @SerializedName("items")
        List<Product> productList;
        int count;

    }
}
