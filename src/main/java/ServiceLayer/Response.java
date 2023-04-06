package ServiceLayer;

public class Response<T> {

    private T value = null;
    private boolean error = false;
    private String message;

    public Response<T> getSuccessResponse(T value) {
        Response<T> response = new Response<>();
        response.setValue(value);
        return response;
    }

    public Response<T> getFailResponse(String message) {
        Response<T> response = new Response<>();
        response.setMessage(message);
        response.setError(true);
        return response;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isError() {
        return error;
    }

    private void setError(boolean error) {
        this.error = error;
    }

    private String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }
}