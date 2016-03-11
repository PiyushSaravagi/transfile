package com.wireless.transfile.utility;

import android.content.Context;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class JsonInfo {

    public static String getSystemInfo(Context context) throws JSONException {
        JSONObject systemInfo = new JSONObject();
        systemInfo.put("DeviceModel", Utility.getDeviceName());
        systemInfo.put("BatteryPower", Utility.getBatteryPercentage(context));
        systemInfo.put("MacAddress", Utility.getLocalIpAddress(2));

        systemInfo.put("PhoneMemorySize", Utility.getTotalInternalMemorySize());
        systemInfo.put("PhoneMemoryLeft", Utility.getAvailableInternalMemorySize());

        systemInfo.put("IsSdcard", Utility.externalMemoryAvailable());
        systemInfo.put("SdcardMemorySize", Utility.getTotalExternalMemorySize());
        systemInfo.put("SdcardMemoryLeft", Utility.getAvailableExternalMemorySize());

        systemInfo.put("PhoneMemoryPath", Environment.getDataDirectory());
        systemInfo.put("SdcardMemoryPath", Environment.getExternalStorageDirectory().toString());
       // List image = Utility.getListFiles(Environment.getExternalStorageDirectory());
        //systemInfo.put("Pictures", image.containsValue("count") ? image.get("count") : 0);
        //systemInfo.put("PictureSize", image.containsValue("size") ? image.get("count") : 0);
/*
        systemInfo.put("Videos", Utility.getFileList("video"));
        systemInfo.put("VideoSize", Utility.getFileList("video"));

        systemInfo.put("Musics", Utility.getFileList("music"));
        systemInfo.put("MusicSize", Utility.getFileList("music"));

        systemInfo.put("Documents", Utility.getFileList("document"));
        systemInfo.put("DocumentSize", Utility.getFileList("document"));*/

        systemInfo.put("Wifi", Utility.getWifiStatus(context));
        systemInfo.put("ChargingStatus", Utility.getBatteryStatus(context));
        return systemInfo.toString();
    }
}
