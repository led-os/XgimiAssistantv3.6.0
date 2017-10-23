package confignetwork;

public interface IChListener {
    /**
     * when new ch result is added, the listener will call
     * onChResultAdded callback
     *
     * @param result the Ch result
     */
    void onChResultAdded(IChResult result);
}
