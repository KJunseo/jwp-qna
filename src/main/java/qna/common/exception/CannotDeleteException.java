package qna.common.exception;

public class CannotDeleteException extends QnaException {
    private static final long serialVersionUID = 1L;

    public CannotDeleteException(String message) {
        super(message);
    }
}
