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

import com.tenduke.example.scribeoauth.BaseServlet;
import com.tenduke.example.scribeoauth.RequestTokens;
import com.tenduke.example.scribeoauth.SessionManager;
import com.tenduke.example.scribeoauth.graph.GraphMe;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * <p>
 * This servlet implements OAuth 1.0a response message handling (finalizing the SSO process at this consumer).
 * </p>
 * <p>
 * JSON configuration for OAuth 1.0a settings is read from a resource stream,
 * which is initialized by referring to the resource by name "oauth10a.json".
 * </p>
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public class OAuth10aCallbackServlet extends BaseServlet {

    /**
     * JSON configuration object for OAuth parameters.
     */
    private JSONObject oauthConfig;

    /**
     * The OAuth provider.
     */
    OAuth10aProvider provider;

    /**
     * "Mock" session manager used to create and manage authenticated sessions.
     */
    private final SessionManager sessionManager = SessionManager.instance();

    /**
     * Initializes this servlet by reading OAuth configuration from file WEB-INF/oauth10a.json.
     * @param config Servlet configuration.
     * @throws ServletException For errors during init.
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        //
        super.init(config);
        //
        oauthConfig = readConfiguration("oauth10a.json", config.getServletContext());
        provider = new OAuth10aProvider(oauthConfig);
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
        return "OAuth callback servlet implements OAuth response message handling, the final step of the SSO process";
    }

    /**
     * <p>
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * </p>
     * <p>
     * Expects request parameters "oauth_token" and "oauth_verifier", which are used to resolve
     * request token and to further make access token request to the IdP.
     * </p>
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        //
        String tokenKeyFromRequest = request.getParameter("oauth_token");
        String verifier = request.getParameter("oauth_verifier");
        //
        OAuth10aProvider provider = new OAuth10aProvider(oauthConfig);
        OAuthService service = new ServiceBuilder()
                .provider(provider)
                .apiKey(provider.getApiKey())
                .apiSecret(provider.getApiSecret())
                .build();
        Token storedToken = RequestTokens.getRequestToken(tokenKeyFromRequest);
        //
        Token accessToken = service.getAccessToken(storedToken, new Verifier(verifier));
        if (accessToken == null) {
            //
            request.getRequestDispatcher("/unauthorized.jsp").forward(request, response);
        } else {
            //
            JSONObject userInfo = graphMe(accessToken, provider);
            userInfo.put("accessToken", accessToken);
            userInfo.put("provider", provider);
            doLogin(request, response, userInfo);
            request.setAttribute("isOauth10a", true);
            request.setAttribute("userInfo", userInfo);
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
    protected void doLogin(
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
    private JSONObject graphMe(final Token accessToken, final OAuth10aProvider provider) {
        //
        return new GraphMe<>(accessToken, provider).get(provider.getGraphUrl() + "/me()");
    }

}
