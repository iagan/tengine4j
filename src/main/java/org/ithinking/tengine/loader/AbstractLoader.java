package org.ithinking.tengine.loader;

import java.io.*;

import org.ithinking.tengine.core.Loader;

public abstract class AbstractLoader implements Loader {

    public String load(File file, String encoding) {
        StringBuilder sb = null;
        BufferedReader bufferedReader = null;
        if (file.exists()) {
            sb = new StringBuilder();
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(
                        file), encoding);

                bufferedReader = new BufferedReader(isr);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

            } catch (Exception ioe) {
                throw new RuntimeException();
            } finally {
                close(bufferedReader);
            }
        }
        return sb == null ? null : sb.toString();
    }

    protected void close(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void close(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
