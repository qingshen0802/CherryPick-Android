package uk.co.cherrypick.android.Model;

import android.graphics.drawable.Drawable;
import uk.co.cherrypick.android.Utiles.ApplicationConstants.OperatingSystemType;

/**
 * Created by john on 03/10/15.
 */
public class DeviceModel {
    private int id;
    private String name;
    private String cameraResolution;
    private String dimensions;
    private String batteryLifeTime;
    private String manufacturerName;
    private String sKU;
    private String deviceMemory;
    private OperatingSystemType operatingSystemType;
    private Drawable image;

    private String cameraDescription;
    private String batteryLifeTalk;
    private String launchDate;
    private String screenSize;
    private String operatingSystemVersion;
    private String colour;


//	private String deviceMemory;
//	private String deviceCameraResolution;
//	private OperatingSystemType operatingSystemType;
//	private String operatingSystemVersion;


    public String getCameraDescription() {
        return cameraDescription;
    }

    public void setCameraDescription(String cameraDescription) {
        this.cameraDescription = cameraDescription;
    }

    public String getBatteryLifeTalk() {
        return batteryLifeTalk;
    }

    public void setBatteryLifeTalk(String batteryLifeTalk) {
        this.batteryLifeTalk = batteryLifeTalk;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }


    public DeviceModel(int id, String name, String cameraResolution, String screenSize, String batteryLifeTime,
                       String manufacturerName, String sKU, String deviceMemory, String colour,
                       OperatingSystemType operatingSystemType, String operatingSystemVersion, String dimensions, Drawable image,
                       String cameraDescription, String batteryLifeTalk, String launchDate) {
        this.id = id;
        this.name = name;
        this.cameraResolution = cameraResolution;
        this.screenSize = screenSize;
        this.batteryLifeTime = batteryLifeTime;
        this.manufacturerName = manufacturerName;
        this.sKU = sKU;
        this.deviceMemory = deviceMemory;
        this.colour = colour;
        this.operatingSystemType = operatingSystemType;
        this.operatingSystemVersion = operatingSystemVersion;
        this.dimensions = dimensions;
        this.image = image;
        this.cameraDescription = cameraDescription;
        this.batteryLifeTalk = batteryLifeTalk;
        this.launchDate = launchDate;
        this.screenSize = screenSize;
        this.operatingSystemVersion = operatingSystemVersion;
        this.colour = colour;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getBatteryLifeTime() {
        return batteryLifeTime;
    }

    public void setBatteryLifeTime(String batteryLifeTime) {
        this.batteryLifeTime = batteryLifeTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCameraResolution() {
        return cameraResolution;
    }

    public void setCameraResolution(String cameraResolution) {
        this.cameraResolution = cameraResolution;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getsKU() {
        return sKU;
    }

    public void setsKU(String sKU) {
        this.sKU = sKU;
    }

    public String getDeviceMemory() {
        return deviceMemory;
    }

    public void setDeviceMemory(String deviceMemory) {
        this.deviceMemory = deviceMemory;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public OperatingSystemType getOperatingSystemType() {
        return operatingSystemType;
    }

    public void setOperatingSystemType(OperatingSystemType operatingSystemType) {
        this.operatingSystemType = operatingSystemType;
    }

    public String getOperatingSystemVersion() {
        return operatingSystemVersion;
    }

    public void setOperatingSystemVersion(String operatingSystemVersion) {
        this.operatingSystemVersion = operatingSystemVersion;
    }
}
