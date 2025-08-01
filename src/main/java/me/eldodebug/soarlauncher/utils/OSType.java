package me.eldodebug.soarlauncher.utils;

public enum OSType {
	WINDOWS, LINUX, MAC, UNKNOWN;
	
	public static OSType getType() {
		
		String os = System.getProperty("os.name");
		
        if (os.toLowerCase().contains("win")) {
            return OSType.WINDOWS;
        }
        
        if (os.toLowerCase().contains("ux")) {
            return OSType.LINUX;
        }
        
        if (os.toLowerCase().contains("mac")) {
            return OSType.MAC;
        }
        
        return OSType.UNKNOWN;
	}
}