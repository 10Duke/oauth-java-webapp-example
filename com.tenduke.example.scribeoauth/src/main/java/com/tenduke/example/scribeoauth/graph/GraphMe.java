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

import com.tenduke.example.scribeoauth.SessionInformation;
import java.io.IOException;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.model.Token;

/**
 * Implements /graph/me() request and providing result as a JSON object expected by {@link SessionInformation}.
 * The demo here is both making a graph call and parsing the JSON object AND how scribe API
 * is ~neutral for OAuth versions when making a signed call.
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public class GraphMe<A extends Api> extends OAuthCallGraph<JSONObject, A> {

    /**
     * Initializes a new instance of the {@link GraphMe} class.
     * @param accessToken The OAuth access token.
     * @param provider Provider configuration object.
     */
    public GraphMe(final Token accessToken, final A provider) {
        //
        super(accessToken, provider);
    }

    /**
     * Expects content as such from /graph/me() and returns a JSON object based on that string.
     * @param responsePayload The response content.
     * @param contentType application/json expected.
     * @return JSON object with profile information.
     */
    @Override
    public JSONObject readGraphResultObjects(final String responsePayload, final String contentType) {
        //
        JSONObject retValue;
        try {
            //
            retValue = parseJsonArray(responsePayload, contentType).getJSONObject(0);
        } catch (IOException ex) {
            //
            throw new CallServiceException("Failed to parse graph/me response", ex);
        }
        //
        return retValue;
    }

}
