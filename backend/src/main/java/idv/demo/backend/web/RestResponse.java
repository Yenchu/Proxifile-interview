package idv.demo.backend.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestResponse<T> {

    private Integer code;
    private String msg;
    private T data;

}
