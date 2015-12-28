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
package com.tenduke.example.scribeoauth.oauth2;

import com.tenduke.example.scribeoauth.ConfigurationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.UUID;
import org.json.JSONObject;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * <p>
 * OAuth 2.0 provider class implements required consumer side view of the OAuth API version 2.0.
 * This implementation uses a JSON based configuration.
 * </p>
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public class OAuth20Provider extends DefaultApi20 {

    /**
     * Endpoint (URL at IdP) where this consumer will get access tokens.
     */
    private final String accessTokenEndpoint;

    /**
     * Endpoint (URL at IdP) where this consumer will get authorization tokens.
     */
    private final String authzEndpoint;

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
     * Initializes a new instance of the {@link OAuth20Provider} class.
     * @param configuration OAuth configuration for this consumer.
     */
    public OAuth20Provider(final JSONObject configuration) {
        //
        super();
        //
        accessTokenEndpoint = configuration.getString("accessTokenEndpoint");
        authzEndpoint = configuration.getString("authzEndpoint");
        authzApi = configuration.getString("authzApi");
        callbackUrl = configuration.getString("callbackUrl");
        apiKey = configuration.getString("apiKey");
        apiSecret = configuration.getString("apiSecret");
        graphUrl = configuration.getString("graphUrl");
    }

    /**
     * Gets authorization endpoint (implementation of DefaultApi20).
     * @param oac OAuth configuration object.
     * @return authorization URL.
     */
    @Override
    public String getAuthorizationUrl(final OAuthConfig oac) {
        //
        String authzUrl = MessageFormat.format(
                authzEndpoint,
                oac.getApiKey(),
                OAuthEncoder.encode(oac.getCallback()),
                UUID.randomUUID().toString());
        //
        // sanity check URI
        try {
            //
            URI sanityCheck = new URI(authzUrl);
        } catch (URISyntaxException ex) {
            //
            throw new ConfigurationException("IdP URL configuration syntax exception for: " + authzUrl, ex);
        }
        //
        return authzUrl;
    }

    /**
     * Creates new OAuth Service.
     * @param config Configuration to use.
     * @return new OAuth Service.
     */
    @Override
    public OAuthService createService(final OAuthConfig config) {
        //
        return new IdTokenOauth20Service(this, config);
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
     * {@inheritDoc}
     * This implemenation expects a JSON object as response back from the IdP. The
     * JSON object is also expected to have a field named "access_token", which holds
     * the result access token value.
     */
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new AccessTokenExtractor() {
            @Override
            public Token extract(final String response) {
                Preconditions.checkEmptyString(
                        response,
                        "Response body is incorrect. Can't extract a token from an empty string");
                JSONObject responseJson = new JSONObject(response);
                String token = OAuthEncoder.decode(responseJson.getString("access_token"));
                return new Token(token, "", response);
            }
        };
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
     * Gets API key assigned to this consumer.
     * @return API key assigned to this consumer.
     */
    public String getApiKey() {
        //
        return apiKey;
    }

    /**
     * Gets API secret assigned to this consumer.
     *
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
