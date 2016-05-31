package com.igeak.customwatchface.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.presenter.WatchFaceDetailPresent;
import com.igeak.customwatchface.R;
import com.igeak.customwatchface.view.watchfaceview.WatchPreviewView;

public class FaceDetailActivity extends BaseActivity implements WatchFaceDetailPresent
        .IWatchFaceDetailView {

    RecyclerView mRecyclerView = null;
    RecycleViewAdapter mRecycleViewAdapter = null;
    WatchFaceDetailPresent present = null;
    WatchFaceBean watchfacebean = null;
    WatchFace watchface;
    String[] data = {"12小时制", "显示天气", "显示日期", "显示星期"};
    WatchFacesModel.FacePath facePath;
    WatchPreviewView watchview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detail);

        Intent intent = getIntent();
        watchfacebean = intent.getParcelableExtra(Const.INTENT_EXTRA_KEY_WATCHFACE);
        facePath = intent.getBooleanExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, false) ? WatchFacesModel
                .FacePath.FACE_CUSTOM : WatchFacesModel.FacePath.FACE_INNER;
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecycleViewAdapter = new RecycleViewAdapter(watchfacebean);
        watchview = (WatchPreviewView) findViewById(R.id.watch_view);

        present = new WatchFaceDetailPresent(this.getApplicationContext(), this);
        present.loadWatchimg(watchfacebean,facePath);
        setTitle(watchfacebean.getName()); //更新title
    }

    public void sendToPhone(View v) {
        present.zipFileAndSentToWatch(this, watchfacebean,facePath);
    }

    @Override
    public void updateWatchFaceDetailView(WatchFace watchFace) {
        this.watchface = watchFace;
        watchview.setElements(watchface); //更新预览图
        //watchview.invalidate();
        if (!mRecycleViewAdapter.isSetAdapter()) {
            mRecyclerView.setAdapter(mRecycleViewAdapter);
        } else {
            mRecycleViewAdapter.notifyDataSetChanged(); //更新功能
        }
    }

    @Override
    public void updateWatchSent(WatchFaceBean watchFace) {
        Toast.makeText(this, watchFace.getName() + "has sent", Toast.LENGTH_LONG).show();
    }


    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
        private WatchFaceBean watchface = null;

        public boolean isSetAdapter() {
            return watchface != null;
        }

        public RecycleViewAdapter(WatchFaceBean watchface) {
            this.watchface = watchface;
        }

        //RecyclerView显示的子View
        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = getLayoutInflater().inflate(R.layout.detail_recycler_item, null);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.textView.setText(data[i]);
        }

        //RecyclerView显示数据条数
        @Override
        public int getItemCount() {
            return data.length;
        }

        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView;
            ImageView imageView;
            ImageButton imb;

            //在布局中找到所含有的UI组件
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }

        }
    }
}
