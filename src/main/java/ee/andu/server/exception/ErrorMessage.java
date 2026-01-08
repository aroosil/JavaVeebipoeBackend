package ee.andu.server.exception;

import lombok.Data;

import java.util.Date;

@Data // @Getter ja @Setter, @NoArgsConstructor tekib automaatselt
public class ErrorMessage {
    private String message;
    private Date timestamp;
    private int status;
}
