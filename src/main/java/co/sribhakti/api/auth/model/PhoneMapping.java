package co.sribhakti.api.auth.model;

import lombok.Data;

@Data
public class PhoneMapping {
    public static String table = "auth_phones";
    private String id;
    private String phone;
    private String otp;
    private long otpExpiresAt;
    private long createdAt;

    public PhoneMapping(String phone) {
    this.phone = phone;
    }
    PhoneMapping() {
    }

}
