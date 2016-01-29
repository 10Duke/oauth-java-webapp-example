# oauth-java-webapp-example
A basic OAuth 1.0a and OAuth 2.0 consumer web application that expects to be configured to use a 10Duke Identity Provider.

# What's included

## OAuth 1.0a
* Single Sign-in
* Caching user info in session in this consumer webapp
* Signed calls to IdP's Graph API (10Duke specific)
* Signed calls to IdP's Authorization API (10Duke specific)

## OAuth 2.0 Authorization Grant Flow
* Single Sign-in
* Caching user info in session in this consumer webapp

## OAuth 2.0 Implicit Grant Flow
* Sign-in from client (JavaScript)
* Requesting email and name from /userinfo endpoint provided by the IdP

# Technical notes

## Java source level

Java source and target code level 1.8 is required. This usually takes an extra step in the servlet containers configuration for JSP. Here's a take on what you need to see in JSP servlet init parameters:
    
    <init-param>
      <param-name>compilerTargetVM</param-name>
      <param-value>1.8</param-value>
    </init-param>
    <init-param>
      <param-name>compilerSourceVM</param-name>
      <param-value>1.8</param-value>
    </init-param>
