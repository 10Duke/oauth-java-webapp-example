/*
The MIT License (MIT)

Copyright (c) 2016 10Duke Software, Ltd.

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.tenduke.example.scribeoauth.graph;

/**
 * Runtime exception reserved for errors in calling the Graph API at the IdP.
 * @author Frej, 10Duke Software, Ltd.
 */
public class CallServiceException extends RuntimeException {

    // <editor-fold defaultstate="collapsed" desc="private fields">

    /**
     * serialVersionUID used with {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Result code expected from FilePlus.
     */
    private String _expectedResultCode;

    /**
     * Actual result code given by FilePlus.
     */
    private String _actualResultCode;

    /**
     * Response string as given by FilePlus in response to a call.
     */
    private String _responseString;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="construction">

    /**
     * Initializes new instance of the {@link CallServiceException} class.
     */
    public CallServiceException() {
        //
        super();
    }

    /**
     * Initializes new instance of the {@link CallServiceException} class.
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public CallServiceException(final String message) {
        //
        super(message);
    }

    /**
     * Initializes new instance of the {@link CallServiceException} class.
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public CallServiceException(final String message, final Throwable cause) {
        //
        super(message, cause);
    }

    /**
     * Initializes new instance of the {@link CallServiceException} class.
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public CallServiceException(final Throwable cause) {
        //
        super(cause);
    }

    // </editor-fold>

}
