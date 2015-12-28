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
<%@page import="org.json.JSONObject"%>
<%@page import="com.tenduke.example.scribeoauth.SessionManager"%>
<%@page import="com.tenduke.example.scribeoauth.SessionInformation"%>
<%--
    Document   : index.jsp, provides UI for starting Single Sign-in process from this OAUth consumer.
    Created on : Jul 16, 2015, 10:27:15 AM
    Author     : Frej, 10Duke Software, Ltd.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SSO - Start here</title>
    </head>
    <body>
        <h1>Hello SSO!</h1>
<%
    SessionInformation sessionInfo = SessionManager.instance().validateSession(request, response);
    if (sessionInfo != null && sessionInfo.getUser() != null) {
        //
        JSONObject userInfo = sessionInfo.getUser();
%>
        <p>
            You seem to have a valid session with this OAuth consumer. Go check out
            <a href="localSession.jsp">your cached user information.</a>
        </p>
<%
    } else {
%>
        <p>
            You do not have a valid session with this OAuth consumer.
            Pick OAuth version 1.0a or 2.0 button below to start Single Sign-on process
            with the corresponding flavor.
        </p>
<%
    }
%>

        <form action="login-oauth10a">
            <label>Login with Oauth 1.0a</label>
            <input type="submit" value="Login - 1.0a"/>
        </form>
        <form action="login-oauth20">
            <label>Login with Oauth 2.0</label>
            <input type="submit" value="Login - 2.0"/>
        </form>
    </body>
</html>
