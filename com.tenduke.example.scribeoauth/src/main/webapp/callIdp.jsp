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
    Document   : calldp.jsp, Makes OAuth signed call to the IdP /graph OR /authz service.
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
        <title>Test Consumer call to IdP - OAuth</title>
    </head>
    <body>
        <h1>OAuth 1.0a signed consumer to IdP call</h1>
        <p>
            You may type a URL path and query into the input. Submitting the
            form will call the IdP using the given value of the input.
            The call is signed using current OAuth 1.0 access token
            for your session.
        </p>
        <p>
            NOTE: The response will be opened in a new window and
            this example expects a JSON response.
        </p>

        <!-- this form will trigger a /graph API call to the IdP (from the consumer webapp server) -->
        <h2>Graph API: /graph</h2>
        <form target="newwindow" action="oauth10aCallIdp.jsp">
            <label for="idpQuery">IdP URL Path and Query</label>
            <input type="text" id="idpQuery" name="idpQuery"/>
            <button type="submit">Call /graph</button>
        </form>

        <!-- this form will trigger a /graph API call to the IdP (from the consumer webapp server) -->
        <h2>Authorization API: /authz</h2>
        <form target="newwindow2" action="oauth10aCallIdpAuthz.jsp">

            <label for="itemname">Item name</label>
            <input type="text" id="itemname" name="itemname"/>

            <label for="itemname">Action</label>
            <input type="text" id="actionname" name="actionname"/>

            <br/>

            <label for="permissionscope">Permission scope</label>
            <input type="text" id="permissionscope" name="permissionscope"/>

            <br/>

            <label for="hw">Hardware id</label>
            <input type="text" id="hw" name="hw"/>

            <br/>

            <button type="submit">Check authorization</button>
        </form>

    </body>
</html>
