package com.nikhil.spycam.utils.logUtility;

/**
 * Created by Sunit deswal on 1/23/2017.
 */

import android.os.Environment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

/**
 * write  exceptions in to file  in release mode of API
 */
public class SpyCamExceptionLogger {
    private static SpyCamExceptionLogger sInstance;
    private static final String mFileName = Environment.getExternalStorageDirectory() + "/SpyCamLogs.txt";

    public static SpyCamExceptionLogger getLoggerInstance() {
        if (sInstance == null) {
            sInstance = new SpyCamExceptionLogger();
            return sInstance;
        }
        return sInstance;
    }

    /**
     * Handles logging in a text file creates if not available in the device
     *
     * @param exception Exception thrown
     */
    public void writeToLogFile(Exception exception) {
        StackTraceElement[] temp = exception.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("\nTime : ").append(new Date().toString());
        sb.append("\nException Message: ").append(exception.getMessage());
        for (StackTraceElement aTemp : temp) {
            sb.append("\n\nFile Name : ").append(aTemp.getFileName());
            sb.append("\nClass Name: ").append(aTemp.getClassName());
            sb.append("\nMethodName: ").append(aTemp.getMethodName());
            sb.append("\nLineNumber: ").append(aTemp.getLineNumber());
        }
        sb.append("\n===============================================================================\n");
        try {
            File myLogFile = new File(mFileName);

            long bytesLength = myLogFile.length();

            int mbLength = (int) (bytesLength / (1000 * 1000));
            if (mbLength > 2) {
                //noinspection ResultOfMethodCallIgnored
                myLogFile.delete();

                myLogFile = new File(mFileName);

                //noinspection ResultOfMethodCallIgnored
                myLogFile.createNewFile();
            }

            System.out.println("Size of the file ************" + myLogFile.length());

            if (!myLogFile.exists())
                //noinspection ResultOfMethodCallIgnored
                myLogFile.createNewFile();
            FileWriter fileStream = new FileWriter(mFileName, true);
            BufferedWriter out = new BufferedWriter(fileStream);

            out.write(sb.toString());
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
