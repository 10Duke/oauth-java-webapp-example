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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * Static access to OAuth configuration.
 *
 * @author Frej, 10Duke Software, Ltd.
 */
public final class Configuration {

    /**
     * Map that is used to store tokens.
     */
    private static final Map<String, JSONObject> configs = Collections.synchronizedMap(new HashMap<String, JSONObject>());

    /**
     * Prevents initializing a new instance of the {@link Configuration} class.
     */
    private Configuration() {
        //
    }

    /**
     * Gets a configuration JSON object by name.
     * @param resourceName Resource name for lookup.
     * @return Config object or null if none was found by the name.
     */
    public static JSONObject get(final String resourceName) {
        //
        return configs.get(resourceName);
    }

    /**
     * Sets (registers) a configuration JSON object by name.
     * @param resourceName Resource name to register the configuration object with.
     * @param config The configuration object.
     */
    public static void set(final String resourceName, final JSONObject config) {
        //
        configs.put(resourceName, config);
    }

}
