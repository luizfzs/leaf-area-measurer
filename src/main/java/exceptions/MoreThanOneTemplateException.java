package exceptions;

/**
 * Created by luiz on 08/06/17.
 */
public class MoreThanOneTemplateException extends RuntimeException {
    public MoreThanOneTemplateException() {
    }

    public MoreThanOneTemplateException(String s) {
        super("More than one template on directory " + s);
    }

    public MoreThanOneTemplateException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MoreThanOneTemplateException(Throwable throwable) {
        super(throwable);
    }

    public MoreThanOneTemplateException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
