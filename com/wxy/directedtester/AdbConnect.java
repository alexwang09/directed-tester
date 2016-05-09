package com.wxy.directedtester;

import com.android.chimpchat.adb.AdbBackend;
import com.android.chimpchat.adb.AdbChimpDevice;
import com.share.util.ShareUtil;


public class AdbConnect {
	private AdbChimpDevice device;
	private AdbBackend adb;

	public AdbChimpDevice getDevice() {
		return this.device;
	}

	public void setDevice(AdbChimpDevice device) {
		this.device = device;
	}

	public AdbBackend getAdb() {
		return this.adb;
	}

	public void setAdb(AdbBackend adb) {
		this.adb = adb;
	}

	public AdbConnect() {
		String deviceID = ShareUtil.deviceId;
		// TODO Auto-generated method stub
		if (this.adb == null) {
			this.adb = new AdbBackend();
			// 参数分别为自己定义的等待连接时间和设备id
			// 这里需要注意一下adb的类型
			this.device = (AdbChimpDevice) adb.waitForConnection(8000, deviceID);
		} else {
			this.device = (AdbChimpDevice) adb.waitForConnection(8000, deviceID);
		}
	}
}
