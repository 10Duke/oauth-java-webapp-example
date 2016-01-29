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
    Document   : localSession.jsp, provides a check if the mock session manager thinks you are logged in.
    Created on : Nov 24, 2015, 16:59:01 PM
    Author     : Frej, 10Duke Software, Ltd.
--%>
<%@page import="com.tenduke.example.scribeoauth.SessionManager"%>
<%@page import="com.tenduke.example.scribeoauth.SessionInformation"%>
<%@page import="org.json.JSONObject"%>

<%-- Page head --%>
<%@include file="pageHead.jsp" %>

<%-- Page content --%>
<%
    SessionInformation sessionInfo = SessionManager.instance().validateSession(request, response);
    if (sessionInfo != null && sessionInfo.getUser() != null) {
        //
        JSONObject userInfo = sessionInfo.getUser();
%>
        <h1>Cached user information</h1>
        <p>
            This User info was stored locally in memory when SSO process completed successfully:
        </p>
        <code>
            <%= userInfo.toString() %>
        </code>
<%
    } else {
%>
        <p>
            You do not have a valid session with this consumer webapp.
        </p>
<%
    }
%>
        <h2>Start over.</h2>
        <p>
            <a href="<%= request.getContextPath() == null ? "/" : request.getContextPath() %>">Go back to login page...</a>
        </p>
<%-- End page content --%>

<%-- Page tail --%>
<%@include file="pageTail.jsp" %>
