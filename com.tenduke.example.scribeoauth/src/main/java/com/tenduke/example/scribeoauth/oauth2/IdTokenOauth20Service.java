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

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import org.json.JSONObject;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

/**
 * {@link OAuth20ServiceImpl} extension that provides access to Id token
 * resulting from call to IdP using OpenId scope.
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public class IdTokenOauth20Service extends OAuth20ServiceImpl {

    /**
     * Value of authorization code grant type used with grant_type parameter.
     */
    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

    /**
     * Grant type parameter name.
     */
    private static final String GRANT_TYPE = "grant_type";

    /**
     * The OAuth 2.0 API implementation that uses this class.
     */
    private final DefaultApi20 api;

    /**
     * Configuration provided to this class.
     */
    private final OAuthConfig config;

    /**
     * Cached access token response.
     */
    private String accessTokenResponse;

    /**
     * Initializes a new instance of the {@link IdTokenOauth20Service} class.
     * @param api The OAuth 2.0 API implementation using this OAUth 2,0 service class.
     * @param config OAuth configuration provided for this class.
     */
    public IdTokenOauth20Service(final DefaultApi20 api, final OAuthConfig config) {
        //
        super(api, config);
        this.api = api;
        this.config = config;
    }

    /**
     * {@inheritDoc}
     * Implements OAuth access token request using HTTP POST as method and
     * client_id, client_secret, code, redirect_uri and grant_type parameters
     * in request body,
     * @param requestToken Request token obtained prior to this call.
     * @param verifier The OAUth 2.0 code obtained in authorization phase.
     * @return Access token.
     */
    @Override
    public Token getAccessToken(final Token requestToken, final Verifier verifier) {
        //
        OAuthRequest request = new OAuthRequest(Verb.POST, api.getAccessTokenEndpoint());
        request.addBodyParameter(OAuthConstants.CLIENT_ID,
                config.getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET,
                config.getApiSecret());
        request.addBodyParameter(OAuthConstants.CODE,
                verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI,
                config.getCallback());
        request.addBodyParameter(GRANT_TYPE,
                GRANT_TYPE_AUTHORIZATION_CODE);
        final Response response = request.send();
        accessTokenResponse = response.getBody();
        return api.getAccessTokenExtractor().extract(accessTokenResponse);
    }

    /**
     * Gets Id token using the result obtained from a {@link #getAccessToken(org.scribe.model.Token, org.scribe.model.Verifier)}
     * call made earlier.
     * @return Id token (Open Id scope) read from access token response that was cached in call to
     *         {@link #getAccessToken(org.scribe.model.Token, org.scribe.model.Verifier)}.
     */
    public JSONObject getIdToken() {
        //
        if (accessTokenResponse == null) {
            //
            throw new IllegalStateException("Access token request not made yet, can't extract OpenId scope Id token");
        }
        //
        JSONObject retValue = null;
        //
        JSONObject atResponse = new JSONObject(accessTokenResponse);
        String idToken = atResponse.getString("id_token");
        byte [] idContent = null;
        if (idToken != null && idToken.indexOf(".") > 0) {
            //
            String [] jwtParts = idToken.split("\\.");
            String jwtBody = jwtParts[1];
            idContent = Base64.getDecoder().decode(jwtBody);
        }
        //
        if (idContent != null) {
            //
            try {
                //
                retValue = new JSONObject(new String(idContent, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                //
                // yes, bad style here. "Real" applications should not suppress exceptions like this
            }
        }
        //
        return retValue;
    }
}
