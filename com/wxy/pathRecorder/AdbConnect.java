package com.wxy.pathRecorder;

import java.util.ArrayList;
import java.util.Collection;

import com.android.chimpchat.adb.AdbBackend;
import com.android.chimpchat.adb.AdbChimpDevice;

public class AdbConnect {
	private static AdbChimpDevice device;
	private static AdbBackend adb;

	public static AdbChimpDevice AdbConnect() {
		String deviceID = "192.168.56.101:5555";
		// TODO Auto-generated method stub
		if (adb == null) {
			adb = new AdbBackend();
			// 参数分别为自己定义的等待连接时间和设备id
			// 这里需要注意一下adb的类型
			device = (AdbChimpDevice) adb.waitForConnection(8000, deviceID);
		} else {
			device = (AdbChimpDevice) adb.waitForConnection(8000, deviceID);
		}
		// 需要关闭adb？
		return device;
	}
}
