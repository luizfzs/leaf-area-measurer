package exceptions;

/**
 * Created by luiz on 09/06/17.
 */
public class NoAreaTemplateFoundException extends RuntimeException {
    public NoAreaTemplateFoundException() {
    }

    public NoAreaTemplateFoundException(String s) {
        super("No Area Template found at directory " + s);
    }

    public NoAreaTemplateFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoAreaTemplateFoundException(Throwable throwable) {
        super(throwable);
    }

    public NoAreaTemplateFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
