package ai.aja.demo;

import android.app.Application;

import ai.aja.sdk.Aja;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Aja.init(this);
    }

}
