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

import com.tenduke.example.scribeoauth.BaseServlet;
import com.tenduke.example.scribeoauth.SessionManager;
import com.tenduke.example.scribeoauth.graph.GraphMe;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * <p>
 * This servlet implements OAuth 2.0 response message handling
 * (finalizing the SSO process at this consumer web application).
 * </p>
 * <p>
 * JSON configuration for OAuth 2.0 settings is read from a resource stream,
 * which is initialized by referring to the resource by name "oauth20.json".
 * </p>
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public class OAuth20CallbackServlet extends BaseServlet {

    /**
     * JSON configuration object for OAuth parameters.
     */
    private JSONObject oauthConfig;

    /**
     * The OAuth provider.
     */
    OAuth20Provider provider;

    /**
     * "Mock" session manager used to create and manage authenticated sessions.
     */
    private final SessionManager sessionManager = SessionManager.instance();

    /**
     * Initializes this servlet by reading OAuth configuration from file WEB-INF/oauth20.json.
     * @param config Servlet configuration.
     * @throws ServletException For errors during init.
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        //
        super.init(config);
        //
        oauthConfig = readConfiguration("oauth20.json", config.getServletContext());
        provider = new OAuth20Provider(oauthConfig);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        //
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        //
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        //
        return "OAuth 2.0 callback servlet implements OAuth 2.0 response message handling, the final step of the SSO process";
    }

    /**
     * <p>
     * Proesses requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * </p>
     * <p>
     * Expects request parameter named "code", which is used for making access token request to provider.
     * </p>
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        //
        String verifier = request.getParameter("code");
        //
        OAuth20Provider provider = new OAuth20Provider(oauthConfig);
        OAuthService service = new ServiceBuilder()
                .provider(provider)
                .apiKey(provider.getApiKey())
                .apiSecret(provider.getApiSecret())
                .callback(provider.getCallbackUrl())
                .build();
        //
        Token accessToken = service.getAccessToken(OAuthConstants.EMPTY_TOKEN, new Verifier(verifier));
        if (accessToken == null) {
            //
            request.getRequestDispatcher("/unauthorized.jsp").forward(request, response);
        } else {
            //
            JSONObject userInfo = graphMe(accessToken, provider);
            doLogin(request, response, userInfo);
            //
            request.setAttribute("isOauth20", true);
            request.setAttribute("userInfo", userInfo);
            if (service instanceof IdTokenOauth20Service) {
                //
                request.setAttribute("id_token", ((IdTokenOauth20Service) service).getIdToken());
            }
            //
            request.getRequestDispatcher("/welcomeIn.jsp").forward(request, response);
        }
    }

    /**
     * Creates an authenticated session (mock) for the user agent calling this service.
     *
     * @param request Client HTTP request.
     * @param response HTTP response
     * @param userData JSON object responded from /graph/me call to IdP right after successful
     *                 access token request.
     */
    private void doLogin(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final JSONObject userData) {
        //
        sessionManager.createSession(request, response, userData);
    }

    /**
     * Makes an OAuth signed call to fetch logged in user information from IdP (/graph/me()).
     * @param accessToken The access token.
     * @param provider The provider configuration object.
     * @return JSON object with information about the logged in user.
     */
    private JSONObject graphMe(final Token accessToken, final OAuth20Provider provider) {
        //
        return new GraphMe<>(accessToken, provider).get(provider.getGraphUrl() + "/me()");
    }

}
