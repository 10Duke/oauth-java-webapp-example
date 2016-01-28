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
<%@page import="java.util.UUID"%>
<%@page import="java.text.MessageFormat"%>
<%@page import="com.tenduke.example.scribeoauth.Configuration"%>
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
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SSO - Start here</title>
        <script type="text/javascript" src="js/jquery-1.12.0.min.js"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

    </head>
    <body>
        <div class="container-fluid">
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

            <!-- OAUTH 1.0a FLOW STARTS HERE-->
            <h3>OAuth 1.0a flow</h3>
            <form action="login-oauth10a">
                <input class="btn-primary" type="submit" value="Login - 1.0a"/>
            </form>

            <!-- OAUTH 2.0 AUTHORIZATION GRANT FLOW STARTS HERE -->
            <h3>OAuth 2.0a Authorization grant flow</h3>
            <form action="login-oauth20">
                <input class="btn-primary" type="submit" value="Login - 2.0"/>
            </form>

            <!-- OAUTH 2.0 IMPLICIT GRANT FLOW STARTS HERE -->
            <script type="text/javascript" charset="utf-8">
            $(function () {

                /**
                 * Parses token from authz redirect URL.
                 * @param {String} urlFragment, which means content after # char in URL
                 * @returns Access token or false if no access token is present.
                 */
                var parseToken = function(urlFragment) {
                    var match = urlFragment.match(/access_token=([\w-]+)/);
                    return !!match && match[1];
                };

<%
    //
    // read configuration for using OAuth 2.0 implicit grant flow:
    String consumerState = UUID.randomUUID().toString();
    String clientId = Configuration.get("oauth20.json").getString("apiKey");
    String authzUrl = MessageFormat.format(
            Configuration.get("oauth20.json").getString("implicitFlowAuthzEndpoint"),
            clientId,
            Configuration.get("oauth20.json").getString("implicitFlowCallbackUrl"),
            consumerState);
    String userInfoEndpoint = Configuration.get("oauth20.json").getString("userinfo");
%>
                var STATE = "<%= consumerState %>"; // todo: store this over page transition for matching on return
                var CLIENT_ID = "<%= clientId %>";  // this is the consumer identifier given to this application
                var AUTHORIZATION_ENDPOINT = "<%= authzUrl %>"; // this is where the IdP handles authorization requests
                var RESOURCE_ENDPOINT = "<%= userInfoEndpoint %>"; // this is where to get user information (todo still)

                //
                // check if there's a token, if not then we are in square 1.
                var token = parseToken(document.location.hash);
                if (token) {

                    // we have a token --> hide login button and show information
                    $('div.authenticate').hide();
                    $('div.authenticated').show();

                    $('span.token').text(token);

                    $.ajax({
                        url: RESOURCE_ENDPOINT
                      , beforeSend: function (xhr) {
                          xhr.setRequestHeader('Authorization', "Bearer " + token);
                          xhr.setRequestHeader('Accept',        "application/json");
                        }
                      , success: function (response) {
                            var container = $('span.user');
                            if (response) {
                              container.text(response.email);
                            } else {
                              container.text("An error occurred.");
                            }
                        }
                  });
                } else {
                  $('div.authenticate').show();
                  $('div.authenticated').hide();

                  var authUrl = AUTHORIZATION_ENDPOINT +
                    "?response_type=token" +
                    "&client_id="    + CLIENT_ID +
                    "&redirect_uri=" + window.location;

                  $("a.startImplicitFlow").attr("href", authUrl);
                }
            });
            </script>
            <h3>Or, try OAuth 2.0 Implicit grant flow</h3>
            <div class="authenticate">
                <a class="startImplicitFlow btn btn-primary" role="button" href="">Login - 2.0 Implicit</a>
            </div>

            <div class="authenticated">
                <p>
                    Your access token is:
                    <span class="token">N/A</span>
                </p>
                <p>
                    Your email is:
                    <span class="user">N/A</span>
                </p>
            </div>
        </div>
    </body>
</html>
