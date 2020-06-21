package com.codeyard.aakraman3;

public class BLEDevice {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public String name;
    public int rssi;
    public String scanRecord;


    public BLEDevice(String name, int rssi, byte[] scanRecord) {
        this.name = name;
        this.rssi = rssi;
        this.scanRecord = BLEDevice.bytesToHex(scanRecord);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;  //byte to into
            hexChars[j * 2] = hexArray[v >>> 4];  //high nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; //low nibble
        }
        return new String(hexChars);
    }
}
