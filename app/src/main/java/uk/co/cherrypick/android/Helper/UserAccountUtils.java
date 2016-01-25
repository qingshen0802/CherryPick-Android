/**
 * AndTinder v0.1 for Android
 *
 * @Author: Enrique López Mañas <eenriquelopez@gmail.com>
 * http://www.lopez-manas.com
 * <p/>
 * TAndTinder is a native library for Android that provide a
 * Tinder card like effect. A card can be constructed using an
 * image and displayed with animation effects, dismiss-to-like
 * and dismiss-to-unlike, and use different sorting mechanisms.
 * <p/>
 * AndTinder is compatible with API Level 13 and upwards
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */

package uk.co.cherrypick.android.Helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class UserAccountUtils {

    public static float functionNormalize(int max, int min, int value) {
        int intermediateValue = max - min;
        value -= intermediateValue;
        float var = Math.abs((float) value / (float) intermediateValue);
        return Math.abs((float) value / (float) intermediateValue);
    }


    public static void writeToUserJson(Context current, String userName) {
        try{
            File jsonFile = new File(current.getFilesDir(), "users.json");
            if(!jsonFile.exists()){
                jsonFile.createNewFile();
            }
            String jsonString = Files.toString(jsonFile, Charsets.UTF_8);
            Gson gson = new Gson();

            JsonElement jelement = new JsonParser().parse(jsonString);
            if(jelement.isJsonArray()){
                JsonArray jobject = jelement.getAsJsonArray();
                JsonObject obj = new JsonObject(); //Json Object
                obj.addProperty("name", userName);
                jobject.add(obj);

                String resultingJson = gson.toJson(jobject);

                Files.write(resultingJson, jsonFile, Charsets.UTF_8);
            }else{
                JsonArray jobject = new JsonArray();
                JsonObject obj = new JsonObject(); //Json Object
                obj.addProperty("name", userName);
                jobject.add(obj);

                String resultingJson = gson.toJson(jobject);

                Files.write(resultingJson, jsonFile, Charsets.UTF_8);
            }



        }catch(Exception ex){
            ex.printStackTrace();
            Log.d("Wrinting to Users.json",ex.getMessage());
        }
    }

    private static User[] test(JsonReader reader){
        Gson gson = new Gson();
        User[] arr = gson.fromJson(reader, User[].class);
        return arr;
    }

    public static boolean isReturningUser(Context current, String userName) {
        JSONArray allUsers = null;
        try {

            File userFile = new File(current.getFilesDir(), "users.json");

            if(!userFile.exists())
                return false;

            JsonReader reader = new JsonReader(new FileReader(userFile));

            User[] users = test(reader);


            if (users == null)
                return false;

            for (int index = 0; index < users.length; index++) {
                try {
                        String name = users[index].name;
                        if (name != null && name.equals(userName)) {
                            return true;
                        }

                } catch (Exception e) {
                    Log.d("JSONException", e.getMessage());
                }
            }

            return false;

        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (Exception e) {
            Log.e("jsonFile", "Error in Main");
            e.printStackTrace();
        }

        return false;
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static JSONArray concatArray(JSONArray... arrs){
        JSONArray result = new JSONArray();
        try{
            for (JSONArray arr : arrs) {
                try{
                    for (int i = 0; i < arr.length(); i++) {
                        result.put(arr.get(i));
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    class User{
        String name;
    }

}
