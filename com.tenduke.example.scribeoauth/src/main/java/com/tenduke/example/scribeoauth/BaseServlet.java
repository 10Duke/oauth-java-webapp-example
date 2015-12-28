/*
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
*/
package com.tenduke.example.scribeoauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import org.json.JSONObject;

/**
 * <p>
 * Servlet base class used in this example application. This servlet provides utility to load
 * a JSON configuration by name from /WEB-INF.
 * </p>
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public abstract class BaseServlet extends HttpServlet {

    /**
     * Map that is used to store tokens.
     */
    private static final Map<String, JSONObject> configs = Collections.synchronizedMap(new HashMap<String, JSONObject>());

    /**
     * Reads a JSON configuration from WEB-INF/[resourceName]
     * @param resourceName Name of resource in WEB-INF folder.
     * @param servletContext used to access resource as stream.
     * @return JSON configuration object.
     */
    protected JSONObject readConfiguration(final String resourceName, final ServletContext servletContext) {
        //
        JSONObject retValue;
        //
        JSONObject cached = configs.get(resourceName);
        if (cached != null) {
            //
            // return a copy (not trusting concurrent access to a JSONObject), of cource the next line
            // will make concurrent read access to the JSONObject (map).
            retValue = new JSONObject(cached, JSONObject.getNames(cached));
        } else {
            //
            try (
                InputStream is = servletContext.getResourceAsStream(new StringBuilder("/WEB-INF/").append(resourceName).toString());
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(reader);) {
                //
                StringBuilder sb = new StringBuilder(2048);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    //
                    sb.append(line);
                }
                //
                bufferedReader.close();
                reader.close();
                is.close();
                //
                cached = new JSONObject(sb.toString());
                configs.put(resourceName, cached);
                //
                // return a copy (not trusting concurrent access to a JSONObject), of cource the next line
                // will make concurrent read access to the JSONObject (map).
                retValue = new JSONObject(cached, JSONObject.getNames(cached));
            } catch (IOException ex) {
                //
                throw new ConfigurationException("Failed to initialize OAuth configuration from oauth.json", ex);
            }
        }
        //
        return retValue;
    }

}
