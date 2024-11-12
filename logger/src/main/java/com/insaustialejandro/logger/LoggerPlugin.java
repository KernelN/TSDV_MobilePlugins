package com.insaustialejandro.logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class LoggerPlugin {
    public static final String LOGTAG = "LOG -> ";
    static LoggerPlugin instance = new LoggerPlugin();
    public static LoggerPlugin getInstance() { return instance; }
    Activity mainActivity;

    AlertDialog clearLogsDialog;
    AlertDialog.Builder dialogBuilder;

    String filePath;
    String currentLogs = "";

    public LoggerPlugin() {
        Log.i(LOGTAG, "Logger started");
    }
    public void Set(Activity activity)
    {
        Log.i(LOGTAG, "Setting logger");

        mainActivity = activity;
        currentLogs = "Plugin Started";
        filePath = mainActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/logs.txt";

        dialogBuilder = new AlertDialog.Builder(mainActivity);
        Log.i(LOGTAG, "AlertDialog created");
        dialogBuilder.setTitle("You'll delete all previous logs, are you sure about this?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConfirmClearLogs();
            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { } //this doesn't do anything
        });

        clearLogsDialog = dialogBuilder.create();
        Log.i(LOGTAG, "Logger setted");
    }

    public void SendLog(String msg)
    {
        Log.i(LOGTAG, msg);
        msg = "\n" + msg;
        currentLogs += msg;
        SaveLogs(msg);
    }
    void SaveLogs(String msg)
    {
        try {
            FileWriter writer = new FileWriter(filePath, true);
            writer.write(msg);
            writer.close();
            Log.i(LOGTAG, "Logs saved to file");
        } catch (IOException e) {
            Log.e(LOGTAG, "Logs file save FAILED: " + e.getMessage());
            currentLogs += "\n ERROR SAVING FILE: " + e.getMessage();
        }
    }
    public String GetLogs()
    {
        return currentLogs;
    }
    public void ReadLogs()
    {
        try{
            FileReader reader = new FileReader(filePath);
            currentLogs = "";
            boolean hasTextLeft = true;
            while(hasTextLeft)
            {
                int c = reader.read();
                if(c < 0) hasTextLeft = false;
                else currentLogs += (char)c;
            }
            reader.close();
        } catch (IOException e) {
            Log.e(LOGTAG, "Logs file read FAILED: " + e.getMessage());
            currentLogs += "\n ERROR READING FILE: " + e.getMessage();
        }
    }
    public void ClearLogs()
    {
        clearLogsDialog.show();
    }
    void ConfirmClearLogs()
    {
        try {
            File file = new File(filePath);

            if(!file.exists()) return;

            file.delete();
            Log.i(LOGTAG, "Logs file cleared");
            currentLogs = "";
        } catch (SecurityException e) {
            Log.e(LOGTAG, "Logs file clear FAILED: " + e.getMessage());
            currentLogs += "\n ERROR DELETING FILE: " +  e.getMessage();
        }
    }
}