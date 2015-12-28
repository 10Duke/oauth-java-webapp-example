<%--
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
--%>
<%--
    Document   : welcomeIn.jsp, used to communicate that user authorizes this consumer at the IdP - all is good.
    Created on : Jul 16, 2015, 10:34:26 AM
    Author     : Frej, 10Duke Software, Ltd.
--%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome In - All Good</title>
    </head>
    <body>
        <h1>Yay!</h1>
        <p>User authorized this consumer at the Idp, which means in SSO terms that Login was successful.</p>
<%
    if (request.getAttribute("userInfo") instanceof JSONObject) {
        //
        JSONObject userInfo = (JSONObject) request.getAttribute("userInfo");
%>
        <p>
            Logged in user info;
        </p>
        <code>
            <%= userInfo.toString() %>
        </code>
<%
    } else {
%>
        <p>
            There's a but in the login callback servlet, which should place the user info object into request context.
        </p>
<%
    }
%>
        <h2>Id Token extra goodies.</h2>
<%
    if (Boolean.TRUE.equals(request.getAttribute("isOauth20"))) {
        //
        JSONObject idToken = (JSONObject) request.getAttribute("id_token");
%>
        <p>
            Id token obtained using Open Id scope access token request:
        </p>
        <code>
            <%= idToken == null ? "Token is null" : idToken.toString() %>
        </code>
<%
    } else if (Boolean.TRUE.equals(request.getAttribute("isOauth10a"))) {
%>
        <p>
            OAuth 1.0: No Id token is available since Open Id Connect has been defined for OAuth 2.0.
        </p>
<%
    }
%>
        <h2>Cached user information.</h2>
        <p>
            This OAuth consumer cached user information provided by the IdP.
            <a href="<%= request.getContextPath() == null ? "" : request.getContextPath() %>/localSession.jsp"> Check it out...</a>
        </p>
    </body>
</html>
