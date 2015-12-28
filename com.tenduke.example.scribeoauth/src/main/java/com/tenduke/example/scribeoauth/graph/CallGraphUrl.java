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

import java.util.List;
import java.util.Map;

/**
 * Interface for calling Graph API the IdP service.
 * @param <O> Type of object returned by calls.
 * @author Frej, 10Duke Software, Ltd.
 */
public interface CallGraphUrl<O> {

    /**
     * Makes a HTTP GET call to the URL specified by the endpoint.
     * @param endpoint The endpoint to call.
     * @return Object defined by implementation (converted from API result).
     * @throws CallServiceException if calling the endpoint responds with e.g. an authorization exception,
     */
    O get(String endpoint) throws CallServiceException;

    /**
     * Makes a HTTP POST call to the URL specified by the endpoint.
     * @param endpoint The endpoint to call.
     * @param payload Object serialized as XML (10DUke SDK XmlSerializer), which is posted to the graph endpoint.
     * @param contentType The content type of payload to post.
     * @return Object defined by implementation (converted from API result).
     * @throws CallServiceException if calling the endpoint responds with e.g. an authorization exception,
     */
    O post(String endpoint, String payload, String contentType) throws CallServiceException;

    /**
     * Makes a HTTP POST call to the URL specified by the endpoint.
     * @param endpoint The endpoint to call.
     * @param form Form parameter payload, which is posted to the graph endpoint.
     * @return Object defined by implementation (converted from API result).
     * @throws CallServiceException if calling the endpoint responds with e.g. an authorization exception,
     */
    O post(String endpoint, List<Map<String, String>> form) throws CallServiceException;

}
