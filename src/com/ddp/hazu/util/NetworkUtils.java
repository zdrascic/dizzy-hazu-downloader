/**
 * Copyright @ 2019 Zeljko Drascic
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ddp.hazu.util;

import org.apache.commons.io.IOUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NetworkUtils {

    public static String getWebPage(String source) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        String pageSource = "";
        try {
            url = new URL(source);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                pageSource += line;
            }
        } catch (IOException mue) {
            mue.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
        return pageSource;
    }

    public static String getJSONDataFromAPI(String url) {
        String jsonResponse = null;
        if (!url.equalsIgnoreCase("")) {
            try {
                URL apiEndpoint = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiEndpoint.openConnection();
                connection.setReadTimeout(60 * 1000);
                connection.setConnectTimeout(60 * 1000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", getRandomUserAgent());
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                int responseCode = connection.getResponseCode();
                String responseMsg = connection.getResponseMessage();

                if (responseCode == 200) {
                    InputStream inputStr = connection.getInputStream();
                    String encoding = connection.getContentEncoding() == null ? "UTF-8" : connection.getContentEncoding();
                    jsonResponse = IOUtils.toString(inputStr, encoding);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return jsonResponse;
    }

    public static void getRawFileFromServer(String link, String outputFile, String outputFolder) {
        String fileLocation = outputFolder + File.separator + outputFile;
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection c;
        try {
            c = url.openConnection();
            c.setRequestProperty("User-Agent", getRandomUserAgent());

            InputStream input;
            input = c.getInputStream();
            byte[] buffer = new byte[4096];
            int n = -1;

            OutputStream output = new FileOutputStream(new File(fileLocation));
            while ((n = input.read(buffer)) != -1) {
                if (n > 0) {
                    output.write(buffer, 0, n);
                }
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static byte[] getBinaryFileFromServer(String url) {
        try {
            URL imageUrl = new URL(url);
            InputStream in = new BufferedInputStream(imageUrl.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int n = 0;

            while (true) {
                if (!(-1 != (n = in.read(buf)))) break;
                out.write(buf, 0, n);
            }
            out.close();
            in.close();

            return out.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static String getUrlParameterValue(String query, String paramName) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            if (name.equalsIgnoreCase(paramName)) {
                return value;
            }
        }
        return ("");
    }

    /**
     * Khm... yeah :)
     */
    private static String getRandomUserAgent() {
        Random r = new Random();
        int rando = r.nextInt(10);
        return userAgents[rando];
    }

    /**
     * In case someone did some basic test on api server
     * */
    private static final String[] userAgents = {
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/600.7.12 (KHTML, like Gecko) Version/8.0.7 Safari/600.7.12",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/600.8.9 (KHTML, like Gecko) Version/8.0.8 Safari/600.8.9",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 8_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) CriOS/44.0.2403.67 Mobile/12D508 Safari/600.1.4",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; rv:38.0) Gecko/20100101 Firefox/38.0",
            "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240",
            "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.65 Safari/537.36"
    };

}

