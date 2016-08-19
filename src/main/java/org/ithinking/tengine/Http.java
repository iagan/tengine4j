package org.ithinking.tengine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ${TITLE}
 *
 * @author agan
 * @date 2016-08-20
 */
public class Http {
    public static String get(String url) {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            connection.disconnect();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
