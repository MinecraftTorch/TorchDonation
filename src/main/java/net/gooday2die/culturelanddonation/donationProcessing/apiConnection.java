package net.gooday2die.culturelanddonation.donationProcessing;

import net.gooday2die.culturelanddonation.ConfigValues;
import org.checkerframework.checker.units.qual.C;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A method for controlling threads for async
 */
public class apiConnection {
    /**
     * A method for api connection
     */
    public static boolean isConnected(){
        final String TestURL = ConfigValues.api_url +  "/test_connection";
        System.out.println(ConfigValues.api_url);
        System.out.println(TestURL);
        boolean connectionResult = false;
        try {
            URL url = new URL(TestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");  // A get method for REST API

            con.setConnectTimeout(5000);  // 5 seconds for connection timeouts.
            con.setReadTimeout(5000);
            connectionResult = con.getResponseCode() == 200;
        } catch (IOException e) { // IO Exception
        }
        return connectionResult;
    }

    /**
     * A method for redeeming code using thread
     * @param argCode the code argument
     * @return returns httpResponse objecct
     */
    public static httpResponse redeemCode(String argCode){
        httpResponse result; // make a object for returning data.
        // make this as json
        String jsonInputString = "{\"code\": \"" + argCode + "\"}";
        result = postToServer(jsonInputString); // post it to server

        return result; // return
    }

    /**
     * A method for posting to server
     * @param data the string written json data to post
     * @return httpResponse object
     */
    private static httpResponse postToServer(String data) {
        // I did not write this code, but this works :b
        String totalUrl = "";
        httpResponse result = new httpResponse();
        totalUrl =  ConfigValues.api_url + "/redeem".trim().toString();
        URL url = null;
        HttpURLConnection conn = null;

        String responseData = "";
        BufferedReader br = null;
        StringBuffer sb = null;

        String returnData = "";

        result.responseCode = 0;
        result.responseText = "unknown error";


        try {
            url = new URL(totalUrl);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8"); //post body json으로 던지기 위함
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); //OutputStream을 사용해서 post body 데이터 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte request_data[] = data.getBytes("utf-8");
                os.write(request_data);
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            conn.connect();

            result.responseCode = Integer.parseInt(String.valueOf(conn.getResponseCode()));

            if (result.responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }

            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData);
            }

            returnData = sb.toString();
            result.responseText = returnData;

            return result;

        } catch (IOException e) {
            return result;
        }
    }
}

