import java.net.*;
import java.io.*;

public class Server {

    public static final int PORT = 8080;


    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(PORT);
        while (true) {
            Socket client = server.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String requestString = reader.readLine();
            String request = requestString.substring(requestString.indexOf("=") + 1, requestString.indexOf("HTTP")).trim();
            sendMsg(client, request);
        }
    }

    public static void sendMsg(Socket client, String result) throws IOException {
        OutputStream outputStream = client.getOutputStream();
        String statusText = "HTTP/1.1 200 OK\r\n";
        byte[] statusTextBytes = statusText.getBytes("UTF-8");

        String responseBody = calculate(result);
        byte[] responseBodyBytes = responseBody.getBytes("UTF-8");

        String responseHeader = "Content-Type: text/html; charset=UTF-8\r\nContent-Length: " + responseBody.length() + "\r\n";
        byte[] responseHeaderBytes = responseHeader.getBytes("UTF-8");

        outputStream.write(statusTextBytes);
        outputStream.write(responseHeaderBytes);
        outputStream.write(new byte[]{13, 10});
        outputStream.write(responseBodyBytes);
        client.close();
    }


    public static String calculate(String input) {
        if (input == null || input.length() < 1) {
            return "Param is illegal";
        }

        String[] numbers = input.split(",");

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            int tmp = 1;
            for (int j = 0; j < numbers.length; j++) {
                try {
                    if (i != j) tmp *= Integer.valueOf(numbers[j]);
                } catch (NumberFormatException e) {
                    return "Param \"" + numbers[j] + "\" is illegal";
                }
            }
            if (i != numbers.length - 1)
                out.append(tmp).append(",");
            else
                out.append(tmp);
        }
        return out.toString();
    }
}
