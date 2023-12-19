package co.sribhakti.api.auth.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TokenPayload {

    public String id;
    String name;
    String phone;
    List<String> permissions = new ArrayList<>();

}
