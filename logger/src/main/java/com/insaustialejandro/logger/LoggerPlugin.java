package com.insaustialejandro.logger;
import android.os.Environment;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class LoggerPlugin {
    public static final String LOGTAG = "LOG -> ";
    static LoggerPlugin instance = new LoggerPlugin();
    public static LoggerPlugin getInstance() { return instance; }

    String filePath;
    String currentLogs = "";

    private LoggerPlugin()
    {
        Log.i(LOGTAG, "Logger started");
        this.currentLogs = "Plugin start";
        this.filePath = Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOCUMENTS).getPath() + "/logs.txt";
    }

    public void SendLog(String msg)
    {
        currentLogs += "\n" + msg;
        Log.i(LOGTAG, msg);
    }
    public String GetLogs() { return currentLogs; }
    public void SaveLogs()
    {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(currentLogs);
            writer.close();
        } catch (IOException e) {
            currentLogs += "\n ERROR SAVING FILE";
            throw new RuntimeException(e);
        }
    }
    public void ClearLogs()
    {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write("");
            writer.close();
            try{
                FileReader reader = new FileReader(filePath);
                char[] logs = new char[1000];
                reader.read(logs);
                reader.close();
                currentLogs = new String(logs);
            } catch (IOException e) {
                currentLogs += "\n ERROR READING FILE";
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            currentLogs += "\n ERROR SAVING FILE";
            throw new RuntimeException(e);
        }
    }
}