package pl.kurs.java.test.exception.Entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorEntityExists {
    private String errorCode;
    private String entityName;
    private int id;
}
