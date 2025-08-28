package yun.todo.exception;

public enum ErrorCode {

    NO_SUCH_TODO("There is no todo");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
