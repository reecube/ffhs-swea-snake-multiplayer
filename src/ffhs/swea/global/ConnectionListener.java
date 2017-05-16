package ffhs.swea.global;

import org.json.simple.JSONObject;

public interface ConnectionListener {
    void onObject(Connection connection, JSONObject object);

    void onMessage(Connection connection, String message);

    void onError(Connection connection, String text);

    void onException(Connection connection, Exception exception);
}
