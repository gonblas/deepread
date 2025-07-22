package gblas.books.backend.exceptions;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private int statusCode;
    private String message;
    private String details;
}