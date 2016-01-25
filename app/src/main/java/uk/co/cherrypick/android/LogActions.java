package uk.co.cherrypick.android;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.cherrypick.android.Helper.FileHelpers;
import uk.co.cherrypick.android.Model.Session;


/*
 * @Author : Upender
 * Ref: http://www.java2blog.com/2013/11/gson-example-read-and-write-json.html
 */
public class LogActions {

    public static void logAction(Context current, String viewName,String action, @Nullable String ItemId) {

        Session session  = uk.co.cherrypick.android.Model.User.getSession();
        if (session != null) {
            try {
                String userEmail = session.getName();
                String fileName = "users-" + userEmail.trim() + ".log";
                //File logFile = new File(FileHelpers.getRootDirectory(), fileName);

                File logFile = new File(FileHelpers.getRootDirectory(current), fileName);

                if (!logFile.exists()) {
                    logFile.createNewFile();
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateAndTime = sdf.format(new Date());

                StringBuilder stringOut = new StringBuilder();
                stringOut.append(currentDateAndTime + ",");
                stringOut.append(viewName + ",");
                stringOut.append(action + ",");

                // if we have an item id (e.g. an offer id, include that)
                if (ItemId != null) {
                    stringOut.append(ItemId + ",");
                } else {
                    stringOut.append(",");
                }

                FileWriter writer = new FileWriter(logFile, true);
                writer.write(stringOut.toString() + "\n");
                writer.close();


                if (false) {

                    System.out.println("------***********------------************");
                    System.out.println("------***********------------************");
                    System.out.println("------***********------" + logFile.getCanonicalPath() + ".------************");
                    System.out.println("------***********------" + logFile.getName() + ".------************");
                    System.out.println("------***********------------************");
                    System.out.println("------***********------------************");


                    BufferedReader br = new BufferedReader(new FileReader(logFile));


                    String line = br.readLine();
                    int lineCount = 0;
                    while (line != null) {
                        lineCount++;
                        System.out.println(lineCount);
                        System.out.println(line);
                        line = br.readLine();
                        System.out.println("next is null? " + (line == null));

                    }
                    br.close();
                    System.out.println("------***********------------************");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
