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
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

/**
 * <p>
 * This servlet implements OAuth request token and redirect to IdP steps of SSO process.
 * </p>
 * <p>
 * JSON configuration for OAuth 1.0a settings is read from a resource stream,
 * which is initialized by referring to the resource by name "oauth10a.json".
 * </p>
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public class OAuth10aLoginServlet extends BaseServlet {

    /**
     * JSON configuration object for OAuth parameters.
     */
    private JSONObject oauthConfig;

    /**
     * The OAuth provider.
     */
    OAuth10aProvider provider;

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
        return "Login servlet implements OAuth request token and redirect to IdP steps of SSO process";
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        //
        String idPUrl = provider.getAccessTokenEndpoint();
        String callbackUrl = provider.getCallbackUrl();
        //
        OAuthService service = new ServiceBuilder()
                .provider(provider)
                .apiKey(provider.getApiKey())
                .apiSecret(provider.getApiSecret())
                .callback(provider.getCallbackUrl())
                .build();
        //
        try {
            //
            Token requestToken = service.getRequestToken();
            //
            String authzUrl = service.getAuthorizationUrl(requestToken);
            response.sendRedirect(authzUrl);
            //
            storeRequestToken(requestToken);
        } catch (Throwable t) {
            //
            request.setAttribute("exception", t);
            request.getRequestDispatcher("/oauthError.jsp").forward(request, response);
        }
    }

    /**
     * Stores request token for OAuth message validation when user has signed in with the IdP.
     * @param requestToken The token to store.
     */
    protected void storeRequestToken(final Token requestToken) {
        //
        RequestTokens.storeRequestToken(requestToken);
    }

}
