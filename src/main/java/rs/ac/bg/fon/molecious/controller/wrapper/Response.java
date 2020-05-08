package rs.ac.bg.fon.molecious.controller.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Response<T> {

    private T data;
}
