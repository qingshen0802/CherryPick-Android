package uk.co.cherrypick.android.Model;

/**
 * Created by Simon on 10/28/2015.
 */
public class CardItem {
    private int id;
    private String title;
    private String memory;
    private String color;
    private String upfront;
    private String monthly;
    private String dataAllowance;
    private String smsAllowance;
    private String talkTimeAllowance;
    private String contractDuration;

    public CardItem() {
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getMemory(){
        return memory;
    }
    public void setMemory(String memory){
        this.memory = memory;
    }
    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color = color;
    }
    public String getUpfront(){
        return upfront;
    }
    public void setUpfront(String upfront){
        this.upfront = upfront;
    }
    public String getMonthly(){
        return monthly;
    }
    public void setMonthly(String monthly){
        this.monthly = monthly;
    }
    public String getDataAllowance(){
        return dataAllowance;
    }
    public void setDataAllowance(String dataAllowance){
        this.dataAllowance = dataAllowance;
    }
    public String getSmsAllowance(){
        return smsAllowance;
    }
    public void setSmsAllowance(String smsAllowance){
        this.smsAllowance = smsAllowance;
    }
    public String getTalkTimeAllowance(){
        return talkTimeAllowance;
    }
    public void setTalkTimeAllowance(String talkTimeAllowance){
        this.talkTimeAllowance = talkTimeAllowance;
    }
    public String getContractDuration(){
        return contractDuration;
    }
    public void setContractDuration(String contractDuration){
        this.contractDuration = contractDuration;
    }
}