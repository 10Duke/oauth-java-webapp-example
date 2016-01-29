# oauth-java-webapp-example
A basic OAuth 1.0a and OAuth 2.0 consumer web application that expects to be configured to use a 10Duke Identity Provider.

NOTE: Java source and target code level 1.8 is required in the servlet containers configuration for JSP. Here's a take on what you need to see in JSP servlet init parameters:
    
    <init-param>
      <param-name>compilerTargetVM</param-name>
      <param-value>1.8</param-value>
    </init-param>
    <init-param>
      <param-name>compilerSourceVM</param-name>
      <param-value>1.8</param-value>
    </init-param>
