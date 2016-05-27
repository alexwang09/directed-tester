package com.wxy.directedtester;

import java.io.IOException;

import com.android.chimpchat.adb.AdbChimpDevice;
import com.share.util.ShareUtil;

public class Main {

	public static void main(String[] args) throws IOException {
		boolean flag = PathRecorder.preConfig();
		if (flag == true) {
			AdbConnect adbConnect = new AdbConnect();
		    AdbChimpDevice device = adbConnect.getDevice();
			GetPathsTree.getPathsTree(ShareUtil.seedFilePath, device);
			adbConnect.getAdb().shutdown();
		}
	}

}
