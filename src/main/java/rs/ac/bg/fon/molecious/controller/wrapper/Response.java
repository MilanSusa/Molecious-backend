package rs.ac.bg.fon.molecious.controller.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {

    private T data;
}
