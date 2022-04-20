package pl.kurs.java.test.exception.date;

public class ValidDateException extends RuntimeException {
    public ValidDateException(String entityName) {
        super(entityName);
    }
}
