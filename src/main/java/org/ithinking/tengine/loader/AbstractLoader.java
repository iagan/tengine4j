package org.ithinking.tengine.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.ithinking.tengine.core.Loader;

public abstract class AbstractLoader implements Loader {

    public String load(File file) {
        StringBuilder sb = null;
        if (file.exists()) {
            sb = new StringBuilder();
            InputStream ins = null;
            try {
                ins = new FileInputStream(file);
                BufferedReader bf = new BufferedReader(new InputStreamReader(ins));
                String temp;

                while ((temp = bf.readLine()) != null) {
                    sb.append(temp).append("\n");
                }
                bf.close();
            } catch (IOException ioe) {
                throw new RuntimeException();
            } finally {
                close(ins);
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
}
