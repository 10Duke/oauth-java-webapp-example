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

import com.tenduke.example.scribeoauth.authz.CallAuthzException;
import java.io.IOException;
import org.json.JSONException;
import org.scribe.builder.api.Api;
import org.scribe.model.Token;

/**
 * Implements Graph API basics for implementing concrete /graph/... calls.
 * The demo here is both making a graph call and parsing the JSON object AND how scribe API
 * is ~neutral for OAuth versions when making a signed call.
 *
 * @author Frej, 10Duke Software, Ltd.
 *
 * @param <A> Type of OAuth API (options are v. 1.0a and 2.0)
 */
public class GraphQuery<A extends Api> extends OAuthCallGraph<Object, A> {

    /**
     * Initializes a new instance of the {@link GraphMe} class.
     * @param accessToken The OAuth access token.
     * @param provider Provider configuration object.
     */
    public GraphQuery(final Token accessToken, final A provider) {
        //
        super(accessToken, provider);
    }

    /**
     * Expects content as from /graph/... and returns a JSON object or JSON array based on that string.
     * @param responsePayload The response content.
     * @param contentType application/json expected.
     * @return JSON object or JSON array with API response data.
     */
    @Override
    public Object readGraphResultObjects(final String responsePayload, final String contentType) {
        //
        Object retValue;
        try {
            //
            try {
                //
                retValue = parseJsonObject(responsePayload, contentType);
            } catch (JSONException jsonEx) {
                //
                retValue = parseJsonArray(responsePayload, contentType);
            }
        } catch (IOException ex) {
            //
            throw new CallAuthzException("Failed to parse graph response", ex);
        }
        //
        return retValue;
    }

}
