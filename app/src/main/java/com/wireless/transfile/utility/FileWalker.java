package com.wireless.transfile.utility;

import android.content.Context;

import com.wireless.transfile.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileWalker {
    Map<String, Long> map = new HashMap<>();

    List<File> getListFiles(File parentDir, Context context) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file, context));
            } else {
                if (file.getName().endsWith(".csv")) {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }
//    public Map<String, Long> getFileSizeCount(File root, String fileType, Context context) {
//        File[] list = root.listFiles();
//        for (File f : list) {
//            if (f.isDirectory()) {
//                getFileSizeCount(f, fileType, context);
//            } else {
//                String[] array = null;
//                if (fileType.equals("image")) {
//                    array = context.getResources().getStringArray(R.array.image);
//                }
//                if (Arrays.asList(array).contains(Utility.getExtension(f.getName()))) {
//                    map.put("count", 1L + (map.containsKey("count") ? map.get("count") : 0L));
//                    map.put("size", f.length() + (map.containsKey("size") ? map.get("size") : 0L));
//                }
//            }
//        }
//        return map;
//    }

}