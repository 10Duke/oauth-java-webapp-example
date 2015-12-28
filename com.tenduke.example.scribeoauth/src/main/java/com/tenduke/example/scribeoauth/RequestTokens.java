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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.scribe.model.Token;

/**
 * "In memory" utility that will store and manage request tokens.
 * @author Frej, 10Duke Software, Ltd.
 */
public class RequestTokens {

    /**
     * Map that is used to store tokens.
     */
    private static final Map<String, Token> TOKENS = Collections.synchronizedMap(new HashMap<String, Token>());

    /**
     * Stores request token.
     * @param requestToken The token to store.
     */
    public static void storeRequestToken(final Token requestToken) {
        //
        TOKENS.put(requestToken.getToken(), requestToken);
    }

    /**
     * Gets a request token object using token string as key.
     * @param token The token string to use as key.
     * @return The token object or null if not found by the key.
     */
    public static Token getRequestToken(final String token) {
        //
        return TOKENS.get(token);
    }
}
