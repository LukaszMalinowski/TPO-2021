package zad1.exception;

public class BookNotFoundException extends Exception {

    private final long id;

    public BookNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Book with id " + id + " not found.";
    }
}
