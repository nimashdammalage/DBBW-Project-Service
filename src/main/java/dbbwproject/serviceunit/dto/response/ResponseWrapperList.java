package dbbwproject.serviceunit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseWrapperList<T> {
    private ErrStatus status;
    private List<T> responseObjectList;
    private String errorMsg;

    public ResponseWrapperList() {
    }

    public ResponseWrapperList(ErrStatus status, List<T> responseObjectList) {
        this.status = status;
        this.responseObjectList = responseObjectList;
    }

    public ResponseWrapperList(ErrStatus status, List<T> responseObjectList, String errorMsg) {
        this.status = status;
        this.responseObjectList = responseObjectList;
        this.errorMsg = errorMsg;
    }
}
