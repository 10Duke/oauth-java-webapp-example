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

import java.io.IOException;
import org.json.JSONException;
import org.scribe.builder.api.Api;
import org.scribe.model.Token;

/**
 * Implements /authz/ API call providing JSON object or JSON array as result.
 * The demo here is both making a Authz API call, parsing the JSON AND how scribe API
 * is ~neutral for OAuth versions when making a signed call.
 *
 * @author Frej
 */
public class ObjectAuthz<A extends Api> extends OAuthCallAuthz<Object, A> {

    /**
     * Initializes a new instance of the {@link ObjectAuthz} class.
     * @param accessToken The OAuth access token.
     * @param provider Provider configuration object.
     */
    public ObjectAuthz(final Token accessToken, final A provider) {
        //
        super(accessToken, provider);
    }

    /**
     * Expects content as such from /authz/ and returns a JSON object or JSON array based on that string.
     * @param responsePayload The response content.
     * @param contentType application/json expected.
     * @return JSON object with profile information.
     */
    @Override
    public Object readResultObject(final String responsePayload, final String contentType) {
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
            throw new CallAuthzException("Failed to parse graph/me response", ex);
        }
        //
        return retValue;
    }

}
