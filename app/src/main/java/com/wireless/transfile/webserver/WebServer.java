package com.wireless.transfile.webserver;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.wireless.transfile.constants.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer extends Thread {
    private Context context = null;
    private static final String SERVER_NAME = "TransfileWebServer";
    private NotificationManager notifyManager = null;
    private int serverPort = 0;
    private ServerSocket httpServerSocket;

    public WebServer(Context context, NotificationManager notifyManager) {
        super(SERVER_NAME);
        this.setContext(context);
        this.setNotifyManager(notifyManager);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        serverPort = Integer.parseInt(pref.getString(Constants.PREF_SERVER_PORT, "" + Constants.DEFAULT_SERVER_PORT));
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        Socket socket;

        try {
            httpServerSocket = new ServerSocket(serverPort);

            while (true) {
                socket = httpServerSocket.accept();
                HttpResponse httpResponse = new HttpResponse(socket,getContext(),getNotifyManager());
                httpResponse.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Looper.loop();
    }

    public synchronized void startThread() {
        super.start();
    }

    public synchronized void stopThread() {
        try {
            httpServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setNotifyManager(NotificationManager notifyManager) {
        this.notifyManager = notifyManager;
    }

    public NotificationManager getNotifyManager() {
        return notifyManager;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
