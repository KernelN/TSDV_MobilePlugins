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
        currentLogs = "Plugin start";
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
        currentLogs += "\n" + msg;
        Log.i(LOGTAG, msg);
    }
    public String GetLogs()
    {
        Log.i(LOGTAG, "Logs: " + currentLogs);
        return currentLogs;
    }
    public void SaveLogs()
    {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(currentLogs);
            writer.close();
            Log.i(LOGTAG, "Logs saved to file");
        } catch (IOException e) {
            currentLogs += "\n ERROR SAVING FILE";
            throw new RuntimeException(e);
        }
    }
    public void ReadLogs()
    {
        try{
            FileReader reader = new FileReader(filePath);
            char[] logs = new char[10000];
            reader.read(logs);
            currentLogs = Arrays.toString(logs);
            Log.i(LOGTAG, "Clean logs file content: " + logs);
            reader.close();
        } catch (IOException e) {
            currentLogs += "\n ERROR READING FILE";
            throw new RuntimeException(e);
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