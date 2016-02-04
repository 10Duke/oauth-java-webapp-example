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
<%@page import="com.tenduke.example.scribeoauth.Configuration"%>
<%--
    Document   : calldp.jsp, Makes OAuth signed call to the IdP /graph OR /authz service.
    Created on : Jul 16, 2015, 10:34:26 AM
    Author     : Frej, 10Duke Software, Ltd.
--%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>

<%-- Page head --%>
<%@include file="pageHead.jsp" %>

<%-- Page content --%>
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
        <form role="form" target="newwindow" action="oauth10aCallIdp.jsp">
            <div class="form-group">
                <label for="idpQuery">IdP URL Path and Query</label>
                <input class="form-control" type="text" id="idpQuery" name="idpQuery"/>
            </div>
            <button class="btn btn-primary" type="submit">Call /graph</button>
        </form>

        <!-- this form will trigger a /graph API call to the IdP (from the consumer webapp server) -->
        <h2>Authorization API: /authz</h2>
        <form role="form" target="newwindow2" action="oauth10aCallIdpAuthz.jsp">

            <div class="form-group">
                <label for="itemname">Item name</label>
                <input class="form-control" type="text" id="itemname" name="itemname"/>

                <label for="itemname">Action</label>
                <input class="form-control" type="text" id="actionname" name="actionname"/>
            </div>

            <div class="form-group">
                <label for="permissionscope">Permission scope</label>
                <input class="form-control" type="text" id="permissionscope" name="permissionscope"/>
            </div>

            <div class="form-group">
                <label for="hw">Hardware id</label>
                <input class="form-control" type="text" id="hw" name="hw"/>
            </div>
            <br/>

            <button class="btn btn-primary" type="submit">Check authorization</button>
        </form>

        <!-- OAUTH2 - Bearer token /authz call from Client -->
<%
    String authzApiUrl = Configuration.get("oauth20.json").getString("authzApi");
%>
        <script type="text/javascript" charset="utf-8">
            var itemName = "";
            var hwId = "";
            $( document ).ready(function() {
                $('#authzBtn').click(function() {
                    var authzWithParams = "<%= authzApiUrl %>" + "/.json?" + itemName + "&hw=" + hwId;
                    $.ajax({
                        url: authzWithParams
                      , beforeSend: function (xhr) {
                          xhr.setRequestHeader('Authorization', "Bearer " + Cookies.get("token"));
                          xhr.setRequestHeader('Accept',        "application/json");
                        }
                      , success: function (response) {
                            if (response) {
                              alert(itemName + "=" + response[itemName]
                                      + "\n" + "iss:" + response.iss
                                      + "\n" + "exp:" + response.exp
                                      + "\n" + "iat:" + response.iat
                                      + "\n" + "jti:" + response.jti);
                            } else {
                              alert("An error occurred.");
                            }
                        }
                    });
                });
            });
        </script>
        <h2>Authorization API: /authz with Bearer token</h2>
        <div role="form">

            <div class="form-group">
                <label for="itemname">Item name</label>
                <input class="form-control" type="text" id="itemname" name="itemname" onchange="javascript: itemName = $(this).val()"/>

                <label for="actionname">Action</label>
                <input class="form-control" type="text" id="actionname" name="actionname" value=""/>
            </div>

            <div class="form-group">
                <label for="permissionscope">Permission scope</label>
                <input class="form-control" type="text" id="permissionscope" name="permissionscope" value=""/>
            </div>

            <div class="form-group">
                <label for="hw">Hardware id</label>
                <input class="form-control" type="text" id="hw" name="hw" id="hw" onchange="javascript: hwId = $(this).val()"/>
            </div>
            <br/>

            <button class="btn btn-primary" id="authzBtn">Check authorization</button>
        </div>
<%-- End page content --%>

<%-- Page tail --%>
<%@include file="pageTail.jsp" %>