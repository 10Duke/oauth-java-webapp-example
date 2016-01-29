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
    Document   : oauthError.jsp, used to communicate that something is wrong with the OAuth configuration.
    Created on : Jul 16, 2015, 10:34:26 AM
    Author     : Frej, 10Duke Software, Ltd.
--%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>

<%-- Page head --%>
<%@include file="pageHead.jsp" %>

<%-- Page content --%>
        <h1>Arghhhh!</h1>
        <p>An error with Single Sign-on configuration for OAuth has occurred. The details are:</p>
<%
        Object ex = request.getAttribute("exception");
        if (ex instanceof Throwable) {
            //
            String error = "";
            StringWriter sw = null;
            PrintWriter pw = null;
            try {
                //
                sw = new StringWriter(4096);
                pw = new PrintWriter(sw);
                ((Throwable) ex).printStackTrace(pw);
                error = sw.toString();
                error.replace("<", "&lt;");
                error.replace("&", "&amp;");
            } catch (Throwable t) {
                //
            } finally {
                //
                if (pw != null) {
                    //
                    pw.close();
                }
                //
                if (sw != null) {
                    //
                    sw.close();
                }
            }
%>
        <code>
            <%= error %>
        </code>
<%
        } else {
%>
        <p>No further information available, seems like an unhandled exception.</p>
<%
        }
%>
<%-- End page content --%>

<%-- Page tail --%>
<%@include file="pageTail.jsp" %>
