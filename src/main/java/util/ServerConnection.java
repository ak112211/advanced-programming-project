package util;

import model.Game;

import java.io.*;
import java.net.Socket;

public class ServerConnection {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 5555;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ServerConnection() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendRequest(String request) {
        try {
            out.println(request);
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to process request.";
        }
    }

    public void saveGame(Game game) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(game);
            oos.flush();
            String serializedGame = bos.toString();
            sendRequest("saveGame " + serializedGame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Game getGame(int gameId) {
        String response = sendRequest("getGame " + gameId);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Game) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
