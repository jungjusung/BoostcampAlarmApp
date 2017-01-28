package jungjusung.boostcamp.android.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Jusung on 2017. 1. 24..
 */

public class IterationActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLayoutSun, mLayoutMon, mLayoutTue, mLayoutWed, mLayoutThu, mLayoutFri, mLayoutSat;
    private ImageView mImageSun, mImageMon, mImageTue, mImageWed, mImageThu, mImageFri, mImageSat;
    private List<LinearLayout> mLayoutList = new ArrayList<>();
    private List<ImageView> mImageList = new ArrayList<>();
    private boolean[] checkedItemList = new boolean[7];
    private TreeMap<Integer, String> returnList = new TreeMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteration);

        //선택된 요일의 반환과 선택 효과를 주기 위해 리스트에서 관리
        Arrays.fill(checkedItemList, Boolean.FALSE);
        mLayoutSun = (LinearLayout) findViewById(R.id.ll_sun);
        mLayoutMon = (LinearLayout) findViewById(R.id.ll_mon);
        mLayoutTue = (LinearLayout) findViewById(R.id.ll_tue);
        mLayoutWed = (LinearLayout) findViewById(R.id.ll_wed);
        mLayoutThu = (LinearLayout) findViewById(R.id.ll_thu);
        mLayoutFri = (LinearLayout) findViewById(R.id.ll_fri);
        mLayoutSat = (LinearLayout) findViewById(R.id.ll_sat);
        mLayoutList.add(mLayoutSun);
        mLayoutList.add(mLayoutMon);
        mLayoutList.add(mLayoutTue);
        mLayoutList.add(mLayoutWed);
        mLayoutList.add(mLayoutThu);
        mLayoutList.add(mLayoutFri);
        mLayoutList.add(mLayoutSat);

        mImageSun = (ImageView) findViewById(R.id.iv_sun);
        mImageMon = (ImageView) findViewById(R.id.iv_mon);
        mImageTue = (ImageView) findViewById(R.id.iv_tue);
        mImageWed = (ImageView) findViewById(R.id.iv_wed);
        mImageThu = (ImageView) findViewById(R.id.iv_thu);
        mImageFri = (ImageView) findViewById(R.id.iv_fri);
        mImageSat = (ImageView) findViewById(R.id.iv_sat);
        mImageList.add(mImageSun);
        mImageList.add(mImageMon);
        mImageList.add(mImageTue);
        mImageList.add(mImageWed);
        mImageList.add(mImageThu);
        mImageList.add(mImageFri);
        mImageList.add(mImageSat);

        for (int i = 0; i < mLayoutList.size(); i++) {
            mLayoutList.get(i).setOnClickListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        //선택된 요일들이 트리맵에 저장되어 있다.
        //이것을 정렬해서 반환
        Iterator<Integer> iterator = returnList.keySet().iterator();
        ArrayList<String> rList = new ArrayList<>();

        while (iterator.hasNext()) {
            int i = iterator.next();
            rList.add(returnList.get(i));
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("iteration_list", rList);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        int selectedViewId = view.getId();
        switch (selectedViewId) {
            case R.id.ll_sun:
                selectedItem(R.id.ll_sun);
                break;
            case R.id.ll_mon:
                selectedItem(R.id.ll_mon);
                break;
            case R.id.ll_tue:
                selectedItem(R.id.ll_tue);
                break;
            case R.id.ll_wed:
                selectedItem(R.id.ll_wed);
                break;
            case R.id.ll_thu:
                selectedItem(R.id.ll_thu);
                break;
            case R.id.ll_fri:
                selectedItem(R.id.ll_fri);
                break;
            case R.id.ll_sat:
                selectedItem(R.id.ll_sat);
                break;
            default:
                break;
        }
    }

    public void selectedItem(int id) {

        //선택된 요일을 트리맵에 저장해서 관리
        for (int i = 0; i < mLayoutList.size(); i++) {
            if (mLayoutList.get(i).getId() == id) {
                if (!checkedItemList[i]) {
                    checkedItemList[i] = true;
                    mImageList.get(i).setBackgroundResource(R.drawable.ic_check);
                    TextView layoutText = ((TextView) mLayoutList.get(i).getChildAt(0));
                    char returnDay = layoutText.getText().charAt(0);
                    returnList.put(i, String.valueOf(returnDay));

                } else {
                    checkedItemList[i] = false;
                    mImageList.get(i).setBackgroundResource(0);
                    returnList.remove(i);
                }
            }
        }
    }
}
