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
package com.tenduke.example.scribeoauth;

/**
 * Runtime exception used for configuration errors.
 * @author Frej, 10Duke Software, Ltd.
 */
public class ConfigurationException extends RuntimeException {

    /**
     * serialVersionUID used with {@link java.io.Serializable}.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initializes new instance of the {@link ConfigurationException} class.
     */
    public ConfigurationException() {
        //
        super();
    }

    /**
     * Initializes new instance of the {@link ConfigurationException} class.
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public ConfigurationException(final String message) {
        //
        super(message);
    }

    /**
     * Initializes new instance of the {@link ConfigurationException} class.
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public ConfigurationException(final String message, final Throwable cause) {
        //
        super(message, cause);
    }

    /**
     * Initializes new instance of the {@link ConfigurationException} class.
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public ConfigurationException(final Throwable cause) {
        //
        super(cause);
    }
}