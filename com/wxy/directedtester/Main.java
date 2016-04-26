package com.wxy.directedtester;

import java.io.IOException;

import com.android.chimpchat.adb.AdbChimpDevice;

public class Main {

	public static void main(String[] args) throws IOException {
		boolean flag = PathRecorder.preConfig();
		if (flag == true) {
			AdbConnect adbConnect = new AdbConnect();
		    AdbChimpDevice device = adbConnect.getDevice();
			GetPathsTree.getPathsTree("C:\\Users\\yym\\Documents\\6", device);
			adbConnect.getAdb().shutdown();
		}
	}

}
