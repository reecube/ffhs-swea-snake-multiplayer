package ffhs.swea.global;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {
    private ConnectionListener listener;
    private Socket clientSocket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private Gson gson;

    public Connection(ConnectionListener listener, Socket clientSocket) throws Exception {
        this.listener = listener;
        this.clientSocket = clientSocket;
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        this.gson = new GsonBuilder().create();

        (new Thread(this)).start();
    }

    public void sendMessage(String message) throws Exception {
        writeObject(ConnectionMessageType.MESSAGE, message);
    }

    public void sendObject(Object object) throws Exception {
        writeObject(ConnectionMessageType.OBJECT, object);
    }

    private void writeObject(ConnectionMessageType type, Object object) throws Exception {
        if (!clientSocket.isConnected()) {
            System.err.println("Could not send `" + object + "`!");
            return;
        }

        ConnectionMessage cm = new ConnectionMessage(type, object.getClass(), gson.toJson(object));

        writer.write(gson.toJson(cm) + '\n');
        writer.flush();
    }

    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                ConnectionMessage cm = gson.fromJson(message, ConnectionMessage.class);

                switch (cm.getType()) {
                    case MESSAGE:
                        listener.onMessage(this, gson.fromJson(cm.getDataJson(), cm.getDataClass()).toString());
                        break;
                    case OBJECT:
                        listener.onObject(this, gson.fromJson(cm.getDataJson(), cm.getDataClass()));
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

class ConnectionMessage {
    private ConnectionMessageType type;
    private String dataClass;
    private String dataJson;

    ConnectionMessage(ConnectionMessageType type, Class<?> dataClass, String dataJson) {
        this.type = type;
        this.dataClass = dataClass.toString().substring(6);
        this.dataJson = dataJson;
    }

    ConnectionMessageType getType() {
        return type;
    }

    Class<?> getDataClass() throws ClassNotFoundException {
        return Class.forName(dataClass);
    }

    String getDataJson() {
        return dataJson;
    }
}

enum ConnectionMessageType {
    MESSAGE,
    OBJECT
}

