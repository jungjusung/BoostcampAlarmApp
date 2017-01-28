package jungjusung.boostcamp.android.alarmapp;

import io.realm.RealmObject;

/**
 * Created by Jusung on 2017. 1. 23..
 */

public class RealmString extends RealmObject {
    private String val;

    //램 데이터 베이스의 문자열 리스트를 제어하기 위한 보조 오브젝트
    public String getValue() {
        return val;
    }

    public void setValue(String value) {
        this.val = value;
    }
}
