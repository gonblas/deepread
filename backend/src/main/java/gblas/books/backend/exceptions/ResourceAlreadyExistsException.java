package gblas.books.backend.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    private String message;

    public ResourceAlreadyExistsException() {}

    public ResourceAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}
