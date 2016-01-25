package uk.co.cherrypick.android.Helper;

import android.content.Context;

import uk.co.cherrypick.android.Model.UserData;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by KKX on 04/10/2015.
 */
public class FileHelpers {

    public static File getUserPreferences(Context context, String name) throws IOException {
        String fileName = "user-"+name+"-data.json";
        File cherryPickDir = getRootDirectory(context);
        File userDataFile = new File(cherryPickDir,fileName);
        if(!userDataFile.exists()){
            userDataFile.createNewFile();
            writeInitialInfo(context, userDataFile);
        }
        return userDataFile;
    }

    private static void writeInitialInfo(Context context, File userDataFile) {
        Gson gson = new Gson();
        UserData emptyObj = getDefaultData(context);
        String userDataJson = gson.toJson(emptyObj);
        try {
            //write converted json data to a file named "file.json"
            FileWriter writer = new FileWriter(userDataFile);
            writer.write(userDataJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static UserData getDefaultData(Context context){
        UserData emptyObj = new UserData();
        emptyObj.setSourceCards(CardsHelper.getAllCardIds(context));
        return emptyObj;
    }

    public static File getRootDirectory(Context context){
        return context.getFilesDir();
    }

}
