package org.cop4656.assignment2;

import android.app.Service;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MyFirebaseInstanceIDService extends MyFirebaseMessagingService {
    private static final String TAG = "MyFirebaseInstanceIDService";


    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this app's subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        new Thread(() -> {
            try {
                URL url = new URL("https://cop4656-final-default-rtdb.firebaseio.com/texts");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                // Send the token as a JSON payload
                String jsonInputString = "{\"token\":\"" + token + "\"}";
                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                os.flush();
                os.close();

                // Read the response
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Log.d(TAG, "Server response: " + response.toString());

                conn.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error sending token to server", e);
            }
        }).start();
    }
}
