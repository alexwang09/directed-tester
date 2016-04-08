package com.wxy.pathRecorder;

import java.io.IOException;

import com.android.chimpchat.adb.AdbChimpDevice;

public class Main {

	public static void main(String[] args) throws IOException {
		boolean flag = PathRecorder.preConfig();
		if (flag == true) {
			AdbChimpDevice device = AdbConnect.AdbConnect();
			// System.out.print("hi");
			GetPathsTree.getPathsTree("C:\\Users\\yym\\Documents\\1", device);
		}
	}

}
