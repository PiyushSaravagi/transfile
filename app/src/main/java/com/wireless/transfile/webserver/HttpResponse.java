package com.wireless.transfile.webserver;

import android.app.NotificationManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wireless.transfile.utility.JsonInfo.getSystemInfo;

public class HttpResponse extends Thread {
    Socket socket;
    String header;
    Context context;
    NotificationManager notificationManager;
    // private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    // private DateFormat mHttpDate = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);

    HttpResponse(Socket socket, Context context, NotificationManager notificationManager) {
        this.socket = socket;
        this.context = context;
        this.notificationManager = notificationManager;
        header = "";
    }

    @Override
    public void run() {
        BufferedReader bufferedReader;
        String firstLine;
        String secondLine;
        String response = "";

        String method;
        String httpVersion;
        String requestUri;
        OutputStream outputStream;

        try {
            outputStream = socket.getOutputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()), 2 * 1024);
            firstLine = bufferedReader.readLine();
            int first = firstLine.indexOf(" ");
            int end = firstLine.lastIndexOf(" ");
            Map<String, List<String>> query = null;
            method = firstLine.substring(0, first);
            httpVersion = firstLine.substring(end + 1, firstLine.length());

            requestUri = firstLine.substring(first + 1, end);
            String key = "";
            secondLine = bufferedReader.readLine();
            query = getQueryParams("www.example.com" + requestUri);
            List<String> list = null;
            if (query.containsKey("Key")) {
                list = query.get("Key");
            }
            if (list != null && !list.isEmpty()) {
                key = list.get(0);
            }

            if (method.equals("GET")) {
                if (key.equals("PhoneSystemInfo")) {
                    response = getSystemInfo(context);
                }
            } else {
                response = "404";
            }


            outputStream.write(("HTTP/1.0 200" + "\r\n").getBytes());
            outputStream.write(("Content type: text/html" + "\r\n").getBytes());
            outputStream.write(("Content length: " + response.length() + "\r\n").getBytes());
            outputStream.write(("\r\n").getBytes());
            outputStream.write((response + "\r\n").getBytes());
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("//?");
            if (urlParts.length > 1) {
                urlParts[1] = urlParts[1].substring(1);
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    private void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes;
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }
}