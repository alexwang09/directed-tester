package com.wxy.directedtester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CMDUtils {
	public static String runCMD(String cmd, String flag) {
		BufferedReader in = null;
		String result = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			in = new BufferedReader(new InputStreamReader(
					process.getInputStream(), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String line = null;
		try {
			while ((line = in.readLine()) != null) {
				if (null != flag) {
					int index = line.indexOf(flag);
					if (index != -1)
						result = line;
				} else
					result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
					process.destroy();
				} catch (IOException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
