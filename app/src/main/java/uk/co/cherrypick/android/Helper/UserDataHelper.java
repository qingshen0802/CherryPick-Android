package uk.co.cherrypick.android.Helper;

import android.content.Context;

import uk.co.cherrypick.android.Model.User;
import uk.co.cherrypick.android.Model.UserData;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by KKX on 05/10/2015.
 */
public class UserDataHelper {

    public static UserData getUserData(Context context){
        try{
            File userPreferences = getUserPreferences(context);
            return getUserData(userPreferences);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static File getUserPreferences(Context context){
        try{
            String userName = User.getSession().getName();
            File userPreferences = FileHelpers.getUserPreferences(context, userName);
            return userPreferences;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static void updateLike(File userFile, int id){
        UserData obj = null;
        Gson gson = new Gson();
        // Add id to liked cards
        // Remove id from source cards
        obj = getUserData(userFile);

        if(obj!=null){
            try{
                Integer index = obj.getSourceCards().indexOf(id);
                obj.getSourceCards().remove((Object)id);
                obj.getLikedCards().add(id);
                String userDataJson = gson.toJson(obj);
                FileWriter writer = new FileWriter(userFile);
                writer.write(userDataJson);
                writer.close();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void updateDislike(File userFile, int id){
        UserData obj = null;
        Gson gson = new Gson();
        // Add id to liked cards
        // Remove id from source cards
        obj = getUserData(userFile);

        if(obj!=null){
            try{
                Integer index = obj.getSourceCards().indexOf(id);
                obj.getSourceCards().remove((Object)id);
                obj.getDislikedCards().add(id);
                String userDataJson = gson.toJson(obj);
                FileWriter writer = new FileWriter(userFile);
                writer.write(userDataJson);
                writer.close();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void updateNotSure(File userFile, int id){
        UserData obj = null;
        Gson gson = new Gson();
        // Add id to liked cards
        // Remove id from source cards
        obj = getUserData(userFile);

        if(obj!=null){
            try{
                Integer index = obj.getSourceCards().indexOf(id);
                obj.getSourceCards().remove((Object)id);
                obj.getNotSureCards().add(id);
                String userDataJson = gson.toJson(obj);
                FileWriter writer = new FileWriter(userFile);
                writer.write(userDataJson);
                writer.close();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private static UserData getUserData(File userFile) {
        UserData obj;
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(userFile));
            //convert the json string back to object
            obj = gson.fromJson(br, UserData.class);

            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            return new UserData();
        }
    }

    public static void updateData(Context conext, UserData data) {
        Gson gson = new Gson();
        String userDataJson = gson.toJson(data);
        try {
            File userFile = getUserPreferences(conext);
            //Write converted json data to a file named "file.json"
            FileWriter writer = new FileWriter(userFile);
            writer.write(userDataJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
