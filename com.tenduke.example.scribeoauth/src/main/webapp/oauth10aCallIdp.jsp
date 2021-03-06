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
<%@page import="com.tenduke.example.scribeoauth.graph.GraphQuery"%>
<%@page import="com.tenduke.example.scribeoauth.oauth10a.OAuth10aProvider"%>
<%@page import="com.tenduke.example.scribeoauth.SessionManager"%>
<%@page import="com.tenduke.example.scribeoauth.SessionInformation"%>
<%--
    Document   : oauth10aCalldp.jsp, makes an Oauth 1.0a signed call to the IdP /graph endpoint
    Created on : Jul 16, 2015, 10:34:26 AM
    Author     : Frej, 10Duke Software, Ltd.
--%>
<%@page import="org.scribe.model.Token"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%
    SessionInformation sessionInformation = SessionManager.instance().validateSession(request, response);
    if (sessionInformation != null
            && sessionInformation.getUser() != null
            && sessionInformation.getUser().get("accessToken") != null) {
        //
        Token accessToken = (Token) sessionInformation.getUser().get("accessToken");
        OAuth10aProvider provider = (OAuth10aProvider) sessionInformation.getUser().get("provider");
        GraphQuery<OAuth10aProvider> graphQuery = new GraphQuery<>(
                accessToken,
                provider);
        Object result = graphQuery.get(provider.getGraphUrl() + request.getParameter("idpQuery"));
        out.print(result.toString());
    } else {
        out.println("{\"error\":\"You do not have an authenticated session\"}");
    }
%>
