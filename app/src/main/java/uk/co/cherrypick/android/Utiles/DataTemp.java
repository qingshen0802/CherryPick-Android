package uk.co.cherrypick.android.Utiles;

/**
 * Created by Simon on 10/12/2015.
 */
public class DataTemp<T> {
    private T template;

    public void set(T temp){
        template = temp;
    }
    public T get(){
        return template;
    }
}
