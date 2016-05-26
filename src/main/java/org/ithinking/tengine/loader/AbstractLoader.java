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
		StringBuilder sb = new StringBuilder();
		if (file.exists()) {
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
			}
		}
		return sb.toString();
	}
}
