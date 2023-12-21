package co.sribhakti.api.shop.models;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain  = true)
public class Product {

    public static String table = "product";
    String id;
    String name;
    String description;

    String price;

    Integer availableQuantity;

    private String image;
}
