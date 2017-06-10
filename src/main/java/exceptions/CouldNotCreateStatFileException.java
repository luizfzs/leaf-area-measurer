package exceptions;

/**
 * Created by luiz on 09/06/17.
 */
public class CouldNotCreateStatFileException extends RuntimeException {
    public CouldNotCreateStatFileException() {
    }

    public CouldNotCreateStatFileException(String s) {
        super("Could not create stat file at directory " + s);
    }

    public CouldNotCreateStatFileException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CouldNotCreateStatFileException(Throwable throwable) {
        super(throwable);
    }

    public CouldNotCreateStatFileException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
