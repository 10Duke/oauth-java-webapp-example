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

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that "simulates" something that manages authenticated user session
 * in a web application.
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public final class SessionManager {

    // <editor-fold defaultstate="collapsed" desc="constants">

    /**
     * Cookie name used to mark an authenticated session.
     */
    private static final String SIGNED_SESSION_COOKIE_NAME = "cwsi";

    /**
     * Character used separate elements in multi-value cookie.
     */
    private static final String SESSION_COOKIE_VALUE_SEPARATOR = "_";

    /**
     * Message format pattern for session cookie value.
     */
    private static final String SESSION_COOKIE_PATTERN = "{0}" + SESSION_COOKIE_VALUE_SEPARATOR + "{1}";

    /**
     * Max age used for session cookies.
     */
    private static final int COOKIE_MAX_AGE = 3600 * 24 * 30;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="private fields">

    /**
     * Map that stores session information.
     */
    private final transient Map<String, SessionInformation> sessions
            = Collections.synchronizedMap(new HashMap<String, SessionInformation>());

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="construction">

    /**
     * Prevents initializing a new instance of the {@link SessionManager} class.
     */
    private SessionManager() {
        //
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="methods">

    /**
     * Gets singleton instance of this class.
     * @return singleton instance of this class.
     */
    public static SessionManager instance() {
        //
        return SingletonSessionManager.INSTANCE;
    }

    /**
     * Creates an authenticated session.
     * @param request Client HTTP request.
     * @param response HTTP response.
     * @param user User information as a JSON object.
     */
    public void createSession(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final JSONObject user) {
        //
        final String sessionId = UUID.randomUUID().toString();
        final SessionInformation sessionInfo = new SessionInformation(sessionId, user);
        //
        final String cookieValue = MessageFormat.format(SESSION_COOKIE_PATTERN, sessionId, resolveUserProfileId(user));
        //
        setSessionInformation(sessionInfo);
        //
        final Cookie cookie = new Cookie(SIGNED_SESSION_COOKIE_NAME, cookieValue);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * Validates an authenticated session and will provide the logged in user's session information as validation result.
     * @param request Client HTTP request.
     * @param response HTTP response.
     * @return Session information or null if session is not valid.
     */
    public SessionInformation validateSession(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        //
        SessionInformation retValue = null;
        //
        String sessionId = null;
        final Cookie [] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            //
            for (Cookie cookie : cookies) {
                //
                if (SIGNED_SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    //
                    sessionId = cookie.getValue().split("_")[0];
                }
            }
        }
        //
        if (sessionId != null) {
            //
            retValue = getSessionInformation(sessionId);
        }
        //
        return retValue;
    }

    /**
     * Terminates session.
     * @param request Client HTTP request.
     * @param response HTTP response.
     */
    public void endSession(final HttpServletRequest request, final HttpServletResponse response) {
        //
        final Cookie cookie = new Cookie(SIGNED_SESSION_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * <p>
     * Tries to read user profile identifier from JSON object by following fall back chain:
     * </p>
     * <ul>
     * <li>via an JSON array containment: userInfo.Items[0]."Profile_id"</li>
     * <li>secondary option, directly from field: "Profile_id"</li>
     * </ul>
     * <p>
     * Fallback handling follows the two alternative calls that could have been made to the
     * IdP /graph API: /graph/me or alternatively /graph/me()
     * </p>
     * @param userInfo The JSON object to find profile id in.
     * @return Users profile id or null if not found in the JSON object.
     */
    protected String resolveUserProfileId(final JSONObject userInfo) {
        //
        String retValue;
        //
        // a small portion of spagetthio here in lack of a formal object model at this end.
        try {
            //
            retValue = userInfo.getJSONArray("Items").getJSONObject(0).getString("Profile_id");
        } catch (JSONException | NullPointerException ex) {
            //
            retValue = userInfo.getString("Profile_id");
        }
        //
        return retValue;
    }

    /**
     * Gets session information.
     *
     * @param sessionId Session identifier to use for finding session info.
     * @return Session information or null if none was found by the id.
     */
    public SessionInformation getSessionInformation(final String sessionId) {
        //
        return sessions.get(sessionId);
    }

    /**
     * Sets session information.
     *
     * @param sessionInfo Session information to store.
     */
    public void setSessionInformation(final SessionInformation sessionInfo) {
        //
        sessions.put(sessionInfo.getSessionId(), sessionInfo);
    }

    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="nested singleton">

    /**
     * Nested singleton implementation.
     */
    private static final class SingletonSessionManager {

        /**
         * The singleton instance.
         */
        @SuppressWarnings("PMD.UnusedPrivateField")
        private static final SessionManager INSTANCE = new SessionManager();

        /**
         * Prevents an instance of the {@link SingletonSessionManager} class from being created.
         */
        private SingletonSessionManager() {
            //
        }
    }

    //</editor-fold>

}
