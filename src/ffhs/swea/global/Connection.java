package ffhs.swea.global;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {
    private final static String KEY_TYPE = "type";
    private final static String KEY_DATA = "data";

    private final static String TYPE_MESSAGE = "message";
    private final static String TYPE_OBJECT = "object";

    private ConnectionListener listener;
    private Socket clientSocket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public Connection(ConnectionListener listener, Socket clientSocket) throws Exception {
        this.listener = listener;
        this.clientSocket = clientSocket;
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        (new Thread(this)).start();
    }

    public void sendMessage(String message) throws Exception {
        writeObject(TYPE_MESSAGE, message);
    }

    public void sendObject(JSONObject object) throws Exception {
        writeObject(TYPE_OBJECT, object);
    }

    @SuppressWarnings("unchecked")
    private void writeObject(String type, Object object) throws Exception {
        if (!clientSocket.isConnected()) {
            System.err.println("Could not send `" + object + "`!");
            return;
        }

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(KEY_TYPE, type);
        jsonObject.put(KEY_DATA, object);

        writer.write(jsonObject.toJSONString() + '\n');
        writer.flush();
    }

    @SuppressWarnings("unchecked")
    public void run() {
        try {
            JSONParser parser = new JSONParser();

            String message;
            while ((message = reader.readLine()) != null) {
                JSONObject object = (JSONObject) parser.parse(message);
                Object type = object.getOrDefault(KEY_TYPE, null);
                Object data = object.getOrDefault(KEY_DATA, null);

                if (type == null || data == null) {
                    continue;
                }

                switch (type.toString()) {
                    case TYPE_MESSAGE:
                        listener.onMessage(this, data.toString());
                        break;
                    case TYPE_OBJECT:
                        listener.onObject(this, (JSONObject) data);
                        break;
                    default:
                        listener.onError(this, message);
                        break;
                }

            }
        } catch (Exception ex) {
            listener.onException(this, ex);
        }
    }
}
