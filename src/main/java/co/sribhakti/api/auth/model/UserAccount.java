package co.sribhakti.api.auth.model;

import lombok.Data;


@Data
public class UserAccount {
    public static String table = "auth_user_accounts";
    private String id;
    private String name;
    private String email;
    private String phone;
    private Long createdAt;
    private Long updatedAt;
    public UserAccount(String id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    public UserAccount() {

    }
}
