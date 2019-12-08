package dbbwproject.serviceunit.dto.response;

import lombok.*;

@Getter
@Setter
public class ResponseWrapper<T> {
    private ErrStatus status;
    private T responseObject;
    private String errorMsg;

    public ResponseWrapper() {
    }

    public ResponseWrapper(ErrStatus status, T responseObject) {
        this.responseObject = responseObject;
        this.status = status;
    }

    public ResponseWrapper(ErrStatus status, T responseObject, String errorMsg) {
        this.responseObject = responseObject;
        this.status = status;
        this.errorMsg = errorMsg;
    }
}
