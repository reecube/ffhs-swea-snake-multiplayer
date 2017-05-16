package ffhs.swea.global;

public interface ConnectionListener {
    void onObject(Connection connection, Object object);

    void onMessage(Connection connection, String json);

    void onError(Connection connection, String text);

    void onException(Connection connection, Exception exception);
}
