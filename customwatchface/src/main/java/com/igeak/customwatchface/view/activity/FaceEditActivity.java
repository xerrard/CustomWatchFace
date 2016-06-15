package com.igeak.customwatchface.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.R;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.presenter.IWatchFaceEditContract;
import com.igeak.customwatchface.presenter.WatchFaceEditPresent;
import com.igeak.customwatchface.view.fragment.BackgroudEditFragment;
import com.igeak.customwatchface.view.fragment.PointEditFragment;
import com.igeak.customwatchface.view.fragment.ScaleEditFragment;
import com.igeak.customwatchface.view.view.slide.SlidingTabLayout;
import com.igeak.customwatchface.view.view.watchfaceview.PointView;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FaceEditActivity extends BaseActivity implements IWatchFaceEditContract
        .IWatchFaceEditView {

    ViewPager viewPager = null;
    MainActivityPagerAdapter mMyPagerAdapter = null;
    SlidingTabLayout mSlidingTabLayout = null;
    List<Fragment> fragments = null;
    List<String> titlelist = null;
    WatchPreviewView watchPreviewView = null;
    public WatchFaceEditPresent present = null;
    public WatchFaceBean watchfacebean;
    BackgroudEditFragment backgroudEditFragment = new BackgroudEditFragment();
    ScaleEditFragment scaleEditFragment = new ScaleEditFragment();
    PointEditFragment pointEditFragment = new PointEditFragment();
    Button saveBtn;
    WatchFacesModel.FacePath facePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_edit);
        Intent intent = getIntent();
        watchfacebean = intent.getParcelableExtra(Const.INTENT_EXTRA_KEY_WATCHFACE);
        facePath = intent.getBooleanExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, false) ? WatchFacesModel
                .FacePath.FACE_CUSTOM : WatchFacesModel.FacePath.FACE_INNER;
        setTitle(watchfacebean.getName());
        watchPreviewView = (WatchPreviewView) findViewById(R.id.watch_view);
        initFragment();
        initViewpager();
        present = new WatchFaceEditPresent(this
                , this
                , backgroudEditFragment
                , scaleEditFragment
                , pointEditFragment);

        saveBtn = (Button) findViewById(R.id.savewatch);
        present.loadWatchimg(watchfacebean,facePath);

    }


    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        fragments.add(backgroudEditFragment);
        fragments.add(scaleEditFragment);
        fragments.add(pointEditFragment);
        titlelist = new ArrayList<String>();
        titlelist.add(getResources().getString(R.string.backgroud));
        titlelist.add(getResources().getString(R.string.scale));
        titlelist.add(getResources().getString(R.string.pointer));
    }

    private void initViewpager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mMyPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mMyPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabView(R.layout.custom_tab, 0);
        mSlidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public void updateWatchFace(WatchFace watchFace) {
        watchPreviewView.setElements(watchFace);
    }

    @Override
    public void updatebackground(Bitmap bitmap) {
        watchPreviewView.setBackground(bitmap);
    }

    @Override
    public void updateScale(Bitmap bitmap) {
        watchPreviewView.setScale(bitmap);
    }

    @Override
    public void updatePoint(Map<PointView.Type, Bitmap> map) {
        watchPreviewView.setPoint(map);
    }

    @Override
    public void updateSaved() {
        if (facePath.equals(WatchFacesModel.FacePath.FACE_CUSTOM)){
            Toast.makeText(this, "watchface saved", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "watchface created", Toast.LENGTH_LONG).show();
        }

        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
            //Crop.of(result.getData(), destination).asSquare().start(this);
            boolean isCircleCrop = true;
            new Crop(result.getData()).output(destination).setCropType(isCircleCrop).start(this);


        } else if (requestCode == Crop.REQUEST_CROP) {
            present.handleCrop(resultCode, result);
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        savewatch(null);
    }

    public void savewatch(View view){

            final EditText et = new EditText(this);
            String name = watchfacebean.getName();
            et.setText(name);
            et.setSelection(name.length());
            new AlertDialog.Builder(this)
                    .setTitle("保存表盘")
                    .setIcon(
                            android.R.drawable.ic_dialog_info)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = et.getText().toString();
                            if (facePath.equals(WatchFacesModel.FacePath.FACE_CUSTOM)){
                                present.savewatch(name);
                            }else {
                                present.creatNewFace(name);
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();

    }


    class MainActivityPagerAdapter extends FragmentPagerAdapter {
        public MainActivityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return titlelist.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titlelist.get(position);
        }
    }


}
