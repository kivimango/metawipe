package com.kivimango.metawipe.service;

/**
 * @author kivimango
 * @version 0.1
 * @since 0.1
 */

public class NotAFileException extends Exception {
    static final long serialVersionUID = -1L;
    public NotAFileException() {super();}
    public NotAFileException(String s) {
        super(s);
    }
}
