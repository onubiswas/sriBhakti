package co.sribhakti.api.common;

import java.util.Random;

public class OtpGenerator {

    private int length;

    public OtpGenerator(int length) {
        this.length = length;
    }
    public String generateOtp() {
        String digits = "123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(digits.length());
            sb.append(digits.charAt(index));
        }
        return sb.toString();
    }


}
