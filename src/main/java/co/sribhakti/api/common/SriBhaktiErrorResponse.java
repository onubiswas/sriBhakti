package co.sribhakti.api.common;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.HashMap;

@Data
public class SriBhaktiErrorResponse {
    private String message;
    private HashMap<String, String> errors;
    private int appcode;
    private HttpStatusCode statusCode;

    SriBhaktiErrorResponse(String message, HashMap<String, String> errors, int appcode, HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.appcode = appcode;
        this.errors = errors;
    }


    public HttpStatusCode getStatusCode() {
        return this.statusCode;
    }
    public static SriBhaktiErrorResponseBuilder builder() {
        return new SriBhaktiErrorResponseBuilder();
    }

    public static class SriBhaktiErrorResponseBuilder {

        private String message;
        private HashMap<String, String> errors;
        private int appcode;
        private HttpStatusCode statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        SriBhaktiErrorResponseBuilder(){
        }

        public SriBhaktiErrorResponseBuilder errors(final HashMap<String, String> errors) {
            this.errors = errors;
            return this;
        }

        public SriBhaktiErrorResponseBuilder appcode(final int appcode) {
            this.appcode = appcode;
            return this;
        }

        public SriBhaktiErrorResponse build() {
            return new SriBhaktiErrorResponse(this.message, this.errors, this.appcode, this.statusCode);
        }

        public SriBhaktiErrorResponseBuilder statusCode(final HttpStatusCode statusCode) {
            this.statusCode = statusCode;
            return this;
        }
        public SriBhaktiErrorResponseBuilder message(final String message) {
            this.message = message;
            return this;
        }


        public String toString() {
            return "SamitiErrorResponse.SamitiErrorResponseBuilder(message=" + this.message + ", errors=" + this.errors + ", appcode=" + this.appcode + ", statusCode=" + this.statusCode + ")";
        }

    }

}
