package jungjusung.boostcamp.android.alarmapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Jusung on 2017. 1. 23..
 */

public class RealmInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //램을 최초의 한번 초기화하고 매니페스트에 name에 등록해서 사용.
        Realm.init(this);
    }

}
