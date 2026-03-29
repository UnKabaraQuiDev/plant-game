package com.codedisaster.steamworks;

public class SteamJNI {

	static {
		System.loadLibrary("steamjni");
	}

	public static native String getInstallDir(int appId);

}