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
package com.tenduke.example.scribeoauth.oauth10a;

import com.tenduke.example.scribeoauth.ConfigurationException;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * <p>
 * OAuth 1.0a provider class implements required consumer side view of the OAuth API version 1.0a.
 * This implementation uses a JSON based configuration.
 * </p>
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public class OAuth10aProvider extends DefaultApi10a {

    /**
     * Endpoint (URL at IdP) where this consumer will get request tokens.
     */
    private final String requestTokenEndpoint;

    /**
     * Endpoint (URL at IdP) where this consumer will get access tokens.
     */
    private final String accessTokenEndpoint;

    /**
     * Endpoint (URL at this service) where the OAuth result message is delivered when returning from the IdP.
     */
    private final String callbackUrl;

    /**
     * API key assigned to this consumer.
     */
    private final String apiKey;

    /**
     * API secret assigned to this consumer.
     */
    private final String apiSecret;

    /**
     * Graph URL at the provider.
     */
    private final String graphUrl;

    /**
     * Endpoint (URL at IdP) where this consumer will get authorization decisions at.
     */
    private final String authzApi;

    /**
     * Initializes a new instance of the {@link OAuth10aProvider} class.
     * @param configuration OAuth configuration for this consumer.
     */
    public OAuth10aProvider(final JSONObject configuration) {
        //
        super();
        //
        requestTokenEndpoint = configuration.getString("requestTokenEndpoint");
        accessTokenEndpoint = configuration.getString("accessTokenEndpoint");
        authzApi = configuration.getString("authzApi");
        callbackUrl = configuration.getString("callbackUrl");
        apiKey = configuration.getString("apiKey");
        apiSecret = configuration.getString("apiSecret");
        graphUrl = configuration.getString("graphUrl");
    }

    /**
     * Gets request token endpoint (implementation of DefaultApi10a).
     * @return request token endpoint
     */
    @Override
    public String getRequestTokenEndpoint() {
        //
        return requestTokenEndpoint;
    }

    /**
     * Gets access token endpoint (implementation of DefaultApi10a).
     * @return access token endpoint
     */
    @Override
    public String getAccessTokenEndpoint() {
        //
        return accessTokenEndpoint;
    }

    /**
     * Gets URL at this service where the OAuth result message is delivered when returning from the IdP.
     * @return callback URL.
     */
    public String getCallbackUrl() {
        //
        return callbackUrl;
    }

    /**
     * Gets authorization endpoint (implementation of DefaultApi10a).
     * @return authorization endpoint.
     */
    @Override
    public String getAuthorizationUrl(final Token token) {
        //
        StringBuilder uri = new StringBuilder(accessTokenEndpoint);
        //
        // (over-) simplistic logic to add oauth_token parameter to the URL
        // does not address URI fragments (i.e. only URLs in simple form with URL parameters is supported)
        if (accessTokenEndpoint.contains("?")) {
            //
            uri.append("&");
        } else {
            //
            uri.append("?");
        }
        //
        uri.append("oauth_token=").append(token.getToken());
        //
        // sanity check URI
        try {
            //
            URI sanityCheck = new URI(uri.toString());
        } catch (URISyntaxException ex) {
            //
            throw new ConfigurationException("IdP URL configuration syntax exception for: " + uri.toString(), ex);
        }
        //
        return uri.toString();
    }

    /**
     * Gets API key assigned to this consumer.
     * @return API key assigned to this consumer.
     */
    public String getApiKey() {
        //
        return apiKey;
    }

    /**
     * Gets API secret assigned to this consumer.
     * @return API secret assigned to this consumer.
     */
    public String getApiSecret() {
        return apiSecret;
    }

    /**
     * Gets Graph URL at the provider.
     * @return Graph URL at the provider.
     */
    public String getGraphUrl() {
        //
        return graphUrl;
    }

    /**
     * Gets /authz URL at the provider.
     * @return /authz URL at the provider.
     */
    public String getAuthzUrl() {
        //
        return authzApi;
    }

}
