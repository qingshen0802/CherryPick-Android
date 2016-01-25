package uk.co.cherrypick.android.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import uk.co.cherrypick.android.R;
import uk.co.cherrypick.android.Model.CardModel;
import uk.co.cherrypick.android.Model.DeviceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.cherrypick.android.Utiles.ApplicationConstants.OperatingSystemType;
import uk.co.cherrypick.android.Utiles.ApplicationConstants.Network;
/**
 * Created by john on 04/10/15.
 */
public class CardsHelper {


    private static HashMap<Integer, DeviceModel> devices;
    private static HashMap<Integer, CardModel> cards;

    public static DeviceModel getDeviceById(Context context, Integer id){

        if(devices == null || devices.size() <= 0 )
        {
            populateCardsFromJsonFile(context);
        }

        return devices.get(id);
    }


    private static void populateCardsFromJsonFile(Context context){

        devices = new HashMap<Integer, DeviceModel>();

        JSONObject jsonData = getDataFromJsonFile(context);

        try {
            JSONArray deviceData = jsonData.getJSONArray("devices");
            for(int i=0; i<= deviceData.length()-1; i++){

                Log.d("GetDevice", "Get Data");
                JSONObject d = deviceData.getJSONObject(i);

                String imageUrl = d.getString("imageUrl");
                String cameraResolution = d.getString("cameraResolution");
                String screenSize  = d.getString("screenSize");
                String dimensions = d.getString("dimensions");
                String sKU = d.getString("sKU");
                String operatingSystemTypeString = d.getString("operatingSystemType");
                String operatingSystemVersion = d.getString("operatingSystemVersion");
                String deviceMemory = d.getString("deviceMemory");
                String expandableMemory = d.getString("expandableMemory");
                String processor = d.getString("processor");
                String batteryLifeTime = d.getString("batteryLifeTime");
                String weight = d.getString("weight");
                String colour = d.getString("colour");

                Integer id = d.getInt("id");
                String name = d.getString("name");
                String manufacturerName = d.getString("manufacturerName");


                String cameraDescription;
                String batteryLifeTalk;
                String launchDate;

                cameraDescription = d.getString("cameraDescription");
                batteryLifeTalk = d.getString("batteryLifeTalk");
                launchDate = d.getString("launchDate");

                //, cameraDescription, batteryLifeTalk, launchDate, screenSize, operatingSystemVersion, colour

                Drawable drawableImage = null;
                    Log.d("GetDevice", "Get Image");

                try {

                    String localImageUrl = "@drawable/" + d.getString("imageUrl");
                    int imageResource = context.getResources().getIdentifier(localImageUrl, null, context.getPackageName());
                    drawableImage = ContextCompat.getDrawable(context, imageResource);
                }
                catch(Exception e)
                {
                    drawableImage = ContextCompat.getDrawable(context, R.drawable.apple_iphone6_plus);
                }
//                if(false) {
//                    try {
//                        drawableImage = getDrawableFromRemoteUrl(imageUrl);
//
//                    } catch (MalformedURLException e) {
//                        Log.d("Get Image: MalformedURL", e.getMessage());
//                    }
//                    catch(IOException e)
//                    {
//                        Log.d("Get Image: IOException", e.getMessage());
//                    }
//
//                }
                OperatingSystemType operatingSystemType = null;
                if(operatingSystemTypeString!= null && operatingSystemTypeString != "") {
                    switch (operatingSystemTypeString.toLowerCase()) {
                        case "ios":
                            operatingSystemType = OperatingSystemType.IOS;
                            break;
                        case "windows phone ":
                            operatingSystemType = OperatingSystemType.WINDOWS;
                            break;
                        case "android":
                            operatingSystemType = OperatingSystemType.ANDROID;
                            break;
                    }
                }

                DeviceModel device = new DeviceModel(id, name, cameraResolution, screenSize, batteryLifeTime,
                        manufacturerName, sKU, deviceMemory, colour, operatingSystemType, operatingSystemVersion, dimensions,  drawableImage,
                        cameraDescription, batteryLifeTalk, launchDate);

                Log.d("GetDevice", "Get Done");
                devices.put(device.getId(), device);

            }


            cards = new HashMap<Integer, CardModel>();

            JSONArray offerData = jsonData.getJSONArray("offers");

            for(int i=0; i <= offerData.length()-1; i++) {

                // limit to first 10 items
//                if(i == 200){
//                    i= offerData.length()+1;
//                    continue;
//                }

                Log.d("GetCard", "Get Data");
                JSONObject o = offerData.getJSONObject(i);

                Integer id;
                Integer deviceId;
                String title;
                String description;
                String networkString;
                Network network = null;
                Double upfrontCost;
                Double monthlyCost;
                String contractLength;
                String dataAllowance;
                String talktimeAllowance;
                String smsAllowance;


                id = o.getInt("id");
                deviceId = o.getInt("deviceId");
                title = o.getString("title");
                description = ""; //o.getString("description");
                networkString = o.getString("network");

                if(networkString!=null && networkString != "")
                switch (networkString.toLowerCase())
                {
                    case "vodafone" :
                        network = Network.VODAFONE;
                        break;
                    case "o2" :
                        network = Network.O2;
                        break;
                    case "ee" :
                        network = Network.EE;
                        break;
                    case "three" :
                        network = Network.THREE;
                        break;
                    default:
                }

                upfrontCost = o.getDouble("upfrontCost");
                monthlyCost = o.getDouble("monthlyCost");
                contractLength = o.getString("contractLength");
                dataAllowance = o.getString("dataAllowance");
                smsAllowance = o.getString("smsAllowance");
                talktimeAllowance = o.getString("talktimeAllowance");



                CardModel cardModel = new CardModel(id, deviceId,title, description, network, upfrontCost, monthlyCost, contractLength,
                        false, false, dataAllowance, smsAllowance, talktimeAllowance, null);

                cards.put(id, cardModel);
            }
        }
        catch(JSONException e)
        {

        }

        Integer offerCount = cards.size();
        Log.d("Offers", offerCount + " offers created.");

    }


    public static HashMap<Integer, CardModel> getCards(Context context)
    {
        if(cards == null || cards.size() <= 0 )
        {
            populateCardsFromJsonFile(context);
        }
        return cards;
    }

    public static HashMap<Integer, CardModel> getCards(Context context, List<Integer> ids)
    {
        if(cards == null || cards.size() <= 0 )
        {
            populateCardsFromJsonFile(context);
        }
        if(ids==null || ids.size()==0)
        return cards;

        HashMap<Integer, CardModel> selectedCards = new HashMap<Integer, CardModel>();

        for (int id :
                ids) {
            if(cards.containsKey(id)){
                selectedCards.put(id,cards.get(id));
            }
        }
        return selectedCards;
    }


    public static CardModel getCardById(Context context, Integer id)
    {

        if(cards == null || cards.size() <= 0 )
        {
            populateCardsFromJsonFile(context);
        }

        return cards.get(id);
    }

    public static List<Integer> getAllCardIds(Context context)
    {

        if(cards == null || cards.size() <= 0 )
        {
            populateCardsFromJsonFile(context);
        }
        List<Integer> list = new ArrayList<>();

        for (Integer id:cards.keySet()) {
            list.add(id);
        }

        return list;
    }

    public static JSONObject getDataFromJsonFile(Context context) {
        JSONObject data = new JSONObject();
        try {
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.phone_data)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null; ) {
                jsonBuilder.append(line).append("\n");
            }
            //Parse Json
            JSONTokener tokener = new JSONTokener(jsonBuilder.toString());
            data = new JSONObject(tokener);
        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        } catch (JSONException e) {
            Log.e("jsonFile", "Error in Main");
            e.printStackTrace();
        }
        return data;
    }

    private static Drawable getDrawableFromRemoteUrl(String imageUrlString) throws MalformedURLException, IOException {

            URL imageUrl = new URL(imageUrlString);
            return Drawable.createFromStream(imageUrl.openStream(), "src");

    }

}
