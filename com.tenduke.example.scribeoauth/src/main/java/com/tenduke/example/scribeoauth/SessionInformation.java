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

import org.json.JSONObject;

/**
 * Information related to an (authenticated) session.
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public class SessionInformation {

    /**
     * session id, usually found by looking for JSESSIONID as a cookie in your user agent.
     */
    private String sessionId;

    /**
     * User information in form of JSON data.
     */
    private JSONObject user;

    /**
     * Prevents initializing a new instance of the {@link SessionInformation} class.
     */
    private SessionInformation() {
        //
        this(null, null);
    }

    /**
     * Initializes a new instance of the {@link SessionInformation} class.
     */
    public SessionInformation(final String sessionId, final JSONObject user) {
        //
        super();
        this.sessionId = sessionId;
        this.user = user;
    }

    /**
     * Gets session id.
     * @return session id.
     */
    public String getSessionId() {
        //
        return sessionId;
    }

    /**
     * Sets session id.
     * @param sessionId session id to set.
     */
    public void setSessionId(final String sessionId) {
        //
        this.sessionId = sessionId;
    }

    /**
     * Gets user information in form of JSON data.
     * @return user information in form of JSON data.
     */
    public JSONObject getUser() {
        //
        return user;
    }

    /**
     * Sets user information in form of JSON data.
     * @param user the user information to set.
     */
    public void setUser(final JSONObject user) {
        //
        this.user = user;
    }

}
