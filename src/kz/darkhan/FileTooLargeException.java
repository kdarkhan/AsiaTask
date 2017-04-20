package kz.darkhan;

public class FileTooLargeException extends Exception {
    public FileTooLargeException(int size) {
        super(String.format("File is larger than %d bytes", size));
    }
}
