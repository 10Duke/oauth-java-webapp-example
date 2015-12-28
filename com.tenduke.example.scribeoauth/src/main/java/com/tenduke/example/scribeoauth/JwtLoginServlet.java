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
package com.tenduke.example.scribeoauth;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

/**
 * This servlet implements user authentication based on a JWT token. The token is an identity claim that the IdP has
 * signed and handed to a related party earlier in the chain.
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public class JwtLoginServlet extends BaseServlet {

    /**
     * Request parameter name to pass in identity claim in JWT format.
     */
    public static final String PARAMETER_NAME_ID_TOKEN = "idToken";

    /**
     * Public key for verification of IdP signed JWT tokens (read from servlet init parameter).
     */
    private transient PublicKey publicKey;

    /**
     * "Mock" session manager used to create and manage authenticated sessions.
     */
    private final transient SessionManager sessionManager = SessionManager.instance();

    /**
     * Initializes this servlet.
     * @param config Servlet configuration.
     * @throws ServletException For errors during init.
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        //
        super.init(config);
        //
        final JSONObject jwtPublicKey = readConfiguration("idp.jwt.publickey.json", config.getServletContext());
        try {
            //
            byte[] publicKeyDecoded = Base64.decodeBase64(jwtPublicKey.getString("publicKey"));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyDecoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            //
            throw new ServletException("No way, basic RSA based key generation failed...", ex);
        }
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
        String idToken = request.getParameter(PARAMETER_NAME_ID_TOKEN);
        //
        // check that parameter is ~OK.
        if (idToken != null && !idToken.isEmpty() && idToken.indexOf(".") > 0) {
            //
            // JWT has 3 elements, which are separated by a "." char.
            String [] jwtElements = idToken.split("\\.");
            if (jwtElements.length == 3) {
                //
                String header = jwtElements[0];
                String body = jwtElements[1];
                byte [] dataBytes = new StringBuilder(header).append(".").append(body).toString().getBytes("UTF-8");
                byte [] signatureBytes = Base64.decodeBase64(jwtElements[2]);
                //
                try {
                    //
                    java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA");
                    signature.initVerify(publicKey);
                    //
                    signature.update(dataBytes);
                    //
                    if (signature.verify(signatureBytes)) {
                        //
                        doLogin(request, response, new String(Base64.decodeBase64(body), "UTF-8"));
                    }
                } catch (InvalidKeyException
                        | NoSuchAlgorithmException
                        | SignatureException ex) {
                    //
                    throw new ServletException("No way, basic RSA based key handling and signature verification failed...", ex);
                }
            } else {
                //
                throw new ServletException("Unexpected JWT data");
            }
        } else {
            //
            throw new ServletException("Request parameter: " + PARAMETER_NAME_ID_TOKEN + " not given");
        }
    }

    /**
     * Creates an authenticated session for the user agent calling this service.
     *
     * @param request Client HTTP request.
     * @param response HTTP response
     * @param idTokenUserData User information JSON.
     */
    protected void doLogin(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final String idTokenUserData) {
        //
        JSONObject userInfo = new JSONObject(idTokenUserData);
        sessionManager.createSession(request, response, userInfo);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        //
        return "JWT Login servlet implements identity claim JSON Web Token based sign-in";
    }

}
