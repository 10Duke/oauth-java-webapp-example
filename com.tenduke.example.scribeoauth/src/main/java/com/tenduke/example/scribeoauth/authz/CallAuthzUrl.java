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
package com.tenduke.example.scribeoauth.authz;

import java.util.Map;

/**
 * Interface for calling the Authz API.
 * @param <O> Type of object returned by calls.
 * @author Frej, 10Duke Software, Ltd.
 */
public interface CallAuthzUrl<O> {

    /**
     * Makes a HTTP GET call to the URL specified by the endpoint, which is expected to
     * return result in type defined by implementation.
     * @param parameters The parameters for call.
     * @return Object defined by implementation (converted from the API result).
     * @throws CallAuthzException if calling the endpoint responds with e.g. an authorization exception,
     */
    O get(Map<String, String> parameters) throws CallAuthzException;

}
