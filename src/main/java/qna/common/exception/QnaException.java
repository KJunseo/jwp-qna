package qna.common.exception;

public class QnaException extends RuntimeException {
    public QnaException() {
    }

    public QnaException(String message) {
        super(message);
    }

    public QnaException(Throwable cause) {
        super(cause);
    }

    public QnaException(String message, Throwable cause) {
        super(message, cause);
    }

    public QnaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
