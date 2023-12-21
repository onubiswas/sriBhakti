package co.sribhakti.api.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Data
public class SriBhaktiApiResponse {
    public Object success;
    public HttpStatusCode successCode = HttpStatus.OK;
    public SriBhaktiErrorResponse errors;


    public SriBhaktiApiResponse(Object success) {
        this.success = success;
    }
    public SriBhaktiApiResponse(SriBhaktiErrorResponse errors) {
        this.errors = errors;
    }

}
