package com.igeak.customwatchface.view.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqiang on 16-7-19.
 */
public class MenuActivity extends Activity {
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<Integer> mDrawableDatas;
    private List<Integer> mStringDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu);

        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(this, mDrawableDatas, mStringDatas);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initData() {
        mDrawableDatas = new ArrayList<>();
        mDrawableDatas.add(R.drawable.icon_edit);
        mDrawableDatas.add(R.drawable.icon_release);
        mDrawableDatas.add(R.drawable.icon_rename);
        mDrawableDatas.add(R.drawable.icon_share);
        mDrawableDatas.add(R.drawable.icon_delete);
        mStringDatas = new ArrayList<>();
        mStringDatas.add(R.string.edit);
        mStringDatas.add(R.string.sendtowatch);
        mStringDatas.add(R.string.rename);
        mStringDatas.add(R.string.share);
        mStringDatas.add(R.string.delete);
    }

    class GalleryAdapter extends
            RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private List<Integer> mDrawableDatas;
        private List<Integer> mStringDatas;

        public GalleryAdapter(Context context, List<Integer> drawableDatas, List<Integer>
                stringDatas) {
            mInflater = LayoutInflater.from(context);
            mDrawableDatas = drawableDatas;
            mStringDatas = stringDatas;
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ViewHolder(View itemView) {
                super(itemView);
                mImg = (ImageView) itemView
                        .findViewById(R.id.id_index_gallery_item_image);
                mTxt = (TextView) itemView
                        .findViewById(R.id.id_index_gallery_item_text);
                itemView.setOnClickListener(this);

            }

            ImageView mImg;
            TextView mTxt;

            @Override
            public void onClick(View v) {
                int position = (int) getItemId();
                int result = position + Const.RESULT_CODE_BASE;
                setResult(result);
                finish();
            }
        }

        @Override
        public int getItemCount() {
            return mDrawableDatas.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.menu_item,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            viewHolder.mImg.setImageResource(mDrawableDatas.get(i));
            viewHolder.mTxt.setText(mStringDatas.get(i));
        }

    }


}
