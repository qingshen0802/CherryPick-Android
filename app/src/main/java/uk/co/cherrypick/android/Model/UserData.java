package uk.co.cherrypick.android.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KKX on 04/10/2015.
 */
public class UserData {

    boolean isRevisiting;
    List<Integer> likedCards;
    List<Integer> dislikedCards;
    List<Integer> notSureCards;
    List<Integer> sourceCards;

    int networkFilter;
    int systemFilter;
    int memoryFilter;
    int dataAllowanceFilter;
    int priceFilter;

    public UserData() {
        this.likedCards = new ArrayList<>();
        this.dislikedCards = new ArrayList<>();
        this.notSureCards = new ArrayList<>();
        this.sourceCards = new ArrayList<>();
        this.networkFilter = 0;
        this.systemFilter = 0;
        this.memoryFilter = 0;
        this.dataAllowanceFilter = 0;
        this.priceFilter = 0;
    }

    public boolean isRevisiting() {
        return isRevisiting;
    }

    public void setIsRevisiting(boolean isRevisiting) {
        this.isRevisiting = isRevisiting;
    }

    public List<Integer> getLikedCards() {
        return likedCards;
    }

    public void setLikedCards(List<Integer> likedCards) {
        this.likedCards = likedCards;
    }

    public List<Integer> getDislikedCards() {
        return dislikedCards;
    }

    public void setDislikedCards(List<Integer> dislikedCards) {
        this.dislikedCards = dislikedCards;
    }

    public List<Integer> getNotSureCards() {
        return notSureCards;
    }

    public void setNotSureCards(List<Integer> notSureCards) {
        this.notSureCards = notSureCards;
    }

    public List<Integer> getSourceCards() {
        return sourceCards;
    }

    public void setSourceCards(List<Integer> sourceCards) {
        this.sourceCards = sourceCards;
    }

    public void setNetworkFilter(int networkFilter){
        this.networkFilter = networkFilter;
    }
    public int getNetworkFilter(){
        return networkFilter;
    }

    public void setSystemFilter(int systemFilter){
        this.systemFilter = systemFilter;
    }
    public int getSystemFilter(){
        return systemFilter;
    }

    public void setMemoryFilter(int memoryFilter){
        this.memoryFilter = memoryFilter;
    }
    public int getMemoryFilter(){
        return memoryFilter;
    }

    public void setDataAllowanceFilter(int dataAllowanceFilter){
        this.dataAllowanceFilter = dataAllowanceFilter;
    }
    public int getDataAllowanceFilter(){
        return dataAllowanceFilter;
    }

    public void setPriceFilter(int priceFilter){
        this.priceFilter = priceFilter;
    }
    public int getPriceFilter(){
        return priceFilter;
    }
}
