package uk.co.cherrypick.android.Model;

/**
 * Created by mohanp on 18/9/15.
 */
public class Session {
    private String   name;
    private String   loginTime;

    public Session(String name, String loginTime) {
        this.name = name;
        this.loginTime = loginTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
