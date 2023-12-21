package co.sribhakti.api.shop.ctrl;


import co.sribhakti.api.common.SriBhaktiApiResponse;
import co.sribhakti.api.shop.models.Product;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
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

        // fetch product list from db
        ApiFuture<DocumentSnapshot> query= db.collection(Product.table).document().get();

        try {
            DocumentSnapshot querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }




//
//        productList.add(new Product().setId("1").setName("first product")); // todo add other fields
//        productList.add(new Product().setId("2").setName("second product")); // todo add other fields
//        productList.add(new Product().setId("3").setName("third product")); // todo add other fields
//        productList.add(new Product().setId("4").setName("fourth product")); // todo add other fields


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
