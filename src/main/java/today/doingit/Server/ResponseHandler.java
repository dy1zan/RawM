package today.doingit.Server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * ResponseHandler will handle all outgoing messages.
 * These outgoing messages are passed on from the RequestHandler.
 * This message will be in JSON, and will have the scope of
 * users or user, that it should be sent to.
 *
 * It is only focused on who it should be sent to!
 */
public class ResponseHandler {
    /*
        Requests will have the following format,
        {
            "type":"responseType",
            "address":"ScopeOfUsers",
            "message":"message"
        }
     */

    /**
     * Sends a response generated by the RequestHandler, to the specified audience.
     * @param server the server object for performing Server function calls
     * @param key the SelectionKey of the client who sent the request in the first place.
     * @param response the JSON response that is to be sent.
     * @return true if the message has been sent to the audience, or otherwise false.
     */
    public static boolean handleResponse(Server server, SelectionKey key, String response) {

        //Find out who to send the response to...
        try {
            JsonParser parser = new JsonParser();
            JsonElement rootNode = parser.parse(response);

            if (rootNode.isJsonObject()) {
                JsonObject json = rootNode.getAsJsonObject();
                String type = json.get("type").getAsString();

                //What type of response is it?
                switch (type) {
                    case "broadcast": {
                        SendBroadcastMessage(server, response);
                        break;
                    }
                    case "error": {
                        SendResponse(server, key, response);
                    }
                    case "user": {
                        SendClientMessage(server, key, response);
                        break;
                    }

                }
            }
        }
        catch(JsonParseException jpe) {
            jpe.printStackTrace();
        }
        return false;
    }

    //Send a message to every single client
    public static boolean SendBroadcastMessage(Server server, String response) {
        for(String user : server.getClientList()) {
            server.send(user, response);
        }
        return true;
    }

    //Send a message back to only the client who sent a request.
    public static boolean SendClientMessage(Server server,  SelectionKey key, String response) {
        String username = server.getClientUsername(key);
        System.out.println(username);
        return server.send(username, response);
    }

    public static boolean SendResponse(Server server, SelectionKey key, String response) {
        return server.send(key, response);
    }
}
