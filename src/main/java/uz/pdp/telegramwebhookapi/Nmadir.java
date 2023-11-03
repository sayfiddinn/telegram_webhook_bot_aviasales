package uz.pdp.telegramwebhookapi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


public class Nmadir {
    public static void main(String[] args) throws IOException {

//        try {
//            URL url = new URL("https://www.google.com/search?q=nmadir&oq=nmadir&aqs=chrome..69i57j0i10i512l2j46i10i512j0i5i10i30i625j0i10i30i625.1022j1j15&sourceid=chrome&ie=UTF-8");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            // Set the request method (GET by default)
//            connection.setRequestMethod("GET");
//            // Get the response code
//            int responseCode = connection.getResponseCode();
//            System.out.println("Response Code: " + responseCode);
//            // Read the response from the input stream
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line;
//            StringBuilder response = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();
//            // Print the response
//            System.out.println("Response: " + response.toString());
//            // Disconnect the connection
//            connection.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        URL url=new URL("https://www.google.com/search?q=nmadir&oq=nmadir&aqs=chrome..69i57j0i10i512l2j46i10i512j0i5i10i30i625j0i10i30i625.1022j1j15&sourceid=chrome&ie=UTF-8");
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();
        BufferedReader reader=new BufferedReader(new FileReader(inputStream.toString()));
        int k=0;
        do {
            k++;
            String s = reader.readLine();
            System.out.println(s);
        } while (k != 10);

    }

    private static void davay(String word) {
        try {
            // Define the URL of the endpoint you want to send a request to
            URL url = new URL("https://api.telegram.org/bot6159804786:AAHDtTrexOPOqclr7rhPz8SkEwiB6eC7Jx8/sendMessage");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable output and set the appropriate headers
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // Create the request body
            String requestBody = "{\"chat_id\": \"1971194369\",\"text\": \"" + word + "\"}";

            // Convert the request body to bytes
            byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);

            // Set the content length of the request body
            connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(requestBodyBytes);
            outputStream.flush();
            outputStream.close();
            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the response
            System.out.println("Response: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
