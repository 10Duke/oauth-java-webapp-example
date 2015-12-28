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

import com.tenduke.example.scribeoauth.oauth10a.OAuth10aProvider;
import com.tenduke.example.scribeoauth.oauth2.OAuth20Provider;
import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpHeaders;
import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * <p>
 * Utility to make OAuth signed HTTP calls to a authz endpoint.
 * </p>
 * <p>
 * Note: this class is NOT thread safe. Each calling user should allocate an instance that is scoped
 * to a single thread.
 * </p>
 * @param <O> Type of result objects provided by implementation.
 * @param <A> OAuth API type.
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public abstract class OAuthCallAuthz<O, A extends Api> implements CallAuthzUrl<O> {

    // <editor-fold defaultstate="collapsed" desc="private fields">

    /**
     * Access token to use for signing calls.
     */
    private final Token accessToken;

    /**
     * The OAuth provider.
     */
    private final A provider;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="construction">

    /**
     * Initializes a new instance of the {@link OAuthCallAuthz} class.
     * @param accessToken The OAuth access token.
     * @param provider Provider configuration object.
     */
    public OAuthCallAuthz(
        final Token accessToken,
        final A provider) {
        //
        this.accessToken = accessToken;
        this.provider = provider;
    }

    // </editor-fold>

    /**
     * {@inheritDoc}
     */
    @Override
    public O get(final Map<String, String> parameters) throws CallAuthzException {
        //
        O retValue = null;
        //
        String apiKey = (provider instanceof OAuth10aProvider) ? ((OAuth10aProvider) provider).getApiKey()
                : ((OAuth20Provider) provider).getApiKey();
        String apiSecret = (provider instanceof OAuth10aProvider) ? ((OAuth10aProvider) provider).getApiSecret()
                : ((OAuth20Provider) provider).getApiSecret();
        String providerAuthzApi = (provider instanceof OAuth10aProvider) ? ((OAuth10aProvider) provider).getAuthzUrl()
                : ((OAuth20Provider) provider).getAuthzUrl();
        OAuthService service = new ServiceBuilder()
                .provider(provider)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
        //
        Token scribeAccessToken = new Token(accessToken.getToken(), accessToken.getSecret());
        //
        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, providerAuthzApi);
        parameters.entrySet().stream().forEach(e -> {
            String name = e.getKey();
            String value = e.getValue();
            oauthRequest.addQuerystringParameter(name, value);
        });
        //
        service.signRequest(scribeAccessToken, oauthRequest);
        //
        Response oauthResponse = oauthRequest.send();
        if (oauthResponse.isSuccessful()) {
            //
            String response = oauthResponse.getBody();
            String contentType = oauthResponse.getHeader(HttpHeaders.CONTENT_TYPE);
            retValue = readResultObject(response, contentType);
        }
        //
        return retValue;
    }

    /**
     * Converts graph result to result object.
     * @param responsePayload content that was responded by remote service.
     * @param contentType The content type of response content.
     * @return Result object.
     */
    public abstract O readResultObject(
            final String responsePayload,
            final String contentType);

    /**
     * Utility for parsing the response from call to /graph URL.
     * @param responsePayload content that was responded by remote service.
     * @param contentType Response content type, which is used to validate content before parsing.
     * @return JSON object deserialized from response payload.
     * @throws IOException for IO errors for handling response.
     */
    protected final JSONObject parseJsonObject(
            final String responsePayload,
            final String contentType) throws IOException {
        //
        JSONObject retVal;
        //
        if (contentType != null && contentType.startsWith("application/json")) {
            //
            retVal = new JSONObject(responsePayload);
        } else {
            //
            throw new CallAuthzException("Response content is not a valid JSON object, can not deserialize.");
        }
        //
        return retVal;
    }

    /**
     * Utility for parsing the response from call to /authz/ URL.
     * @param responsePayload content that was responded by remote service.
     * @param contentType Response content type, which is used to validate content before parsing.
     * @return JSON array deserialized from response payload.
     * @throws IOException for IO errors for handling response.
     */
    protected final JSONArray parseJsonArray(
            final String responsePayload,
            final String contentType) throws IOException {
        //
        JSONArray retVal;
        //
        if (contentType != null && contentType.startsWith("application/json")) {
            //
            retVal = new JSONArray(responsePayload);
        } else {
            //
            throw new CallAuthzException("Response content is not a valid JSON array, can not deserialize.");
        }
        //
        return retVal;
    }

    // </editor-fold>

}
