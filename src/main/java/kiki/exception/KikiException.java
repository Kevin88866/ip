package kiki.exception;
// Exception Level-5

public class KikiException extends Exception{
    // constructor, so catch can print the message directly
    public KikiException(String message){
        super(message);
    }
}
