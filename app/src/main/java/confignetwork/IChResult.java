package confignetwork;

import java.net.InetAddress;

public interface IChResult {

    /**
     * check whether the ch task is executed suc
     *
     * @return whether the ch task is executed suc
     */
    boolean isSuc();

    /**
     * get the device's bssid
     *
     * @return the device's bssid
     */
    String getBssid();

    /**
     * check whether the ch task is cancelled by user
     *
     * @return whether the ch task is cancelled by user
     */
    boolean isCancelled();

    /**
     * get the ip address of the device
     *
     * @return the ip device of the device
     */
    InetAddress getInetAddress();
}
