package com.xgimi.zhushou.util;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
 

public class WOL {
    private static int PORT = 10000;

     
    public WOL(){

    }
     

    public static void sendMagicPackage(final String macAddress, final String desIp){
        new Thread() {
            @Override
            public void run() {
                InetAddress destHost = null;
                try {
                    destHost = InetAddress.getByName(desIp);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                byte[] destMac = getMacBytes(macAddress);

                byte[] magic = new byte[102];
                for (int i = 0; i < 6; i++)
                    magic[i] = (byte) 0xFF;
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < destMac.length; j++) {
                        magic[6 + destMac.length * i + j] = destMac[j];
                    }
                }
                String log = "";
                for (int i = 0; i < magic.length; i++) {
                    log += magic[i] + ",";
                    if ((i + 1) % 6 == 0) {
                        System.out.println();
                    }
                }

                DatagramPacket dp = null;
                dp = new DatagramPacket(magic, magic.length, destHost, PORT);
                XGIMILOG.E("开机唤醒包 : " + log);
                DatagramSocket ds;
                try {
                    ds = new DatagramSocket();
                    ds.send(dp);
                    ds.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
 
    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("mac地址不正确ַ");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("mac地址不正确ַ");
        }
        return bytes;
    }
}