package com.igeak.customwatchface.view.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.igeak.customwatchface.R;
import com.igeak.customwatchface.presenter.WatchFaceEditPresent;
import com.igeak.customwatchface.view.activity.FaceEditActivity;
import com.igeak.customwatchface.view.watchfaceview.PointView;
import com.soundcloud.android.crop.Crop;

import java.util.List;
import java.util.Map;


/**
 * Created by xuqiang on 16-5-11.
 */
public class PointEditFragment extends Fragment implements WatchFaceEditPresent.IPointView{

    private static final String TAG = "PointEditFragment";
    private static final int SPAN_COUNT = 2;
    RecyclerView mRecyclerView = null;
    RecycleViewAdapter adapter;

    WatchFaceEditPresent present;
    List<Map<PointView.Type,Bitmap>> pointMaps;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_backgroud, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        present = ((FaceEditActivity) getActivity()).present;
        pointMaps = present.loadPointImg();
        adapter = new RecycleViewAdapter(pointMaps);
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }


    //继承自 RecyclerView.Adapter
    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

        private List<Map<PointView.Type, Bitmap>> pointMaps;

        public RecycleViewAdapter(List<Map<PointView.Type, Bitmap>> pointMaps) {
            this.pointMaps = pointMaps;
            setHasStableIds(true); //必须要加的代码，默认为false，当true，getItemId才有效
        }

        //RecyclerView显示的子View
        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.recycler_item_point,
                    null);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.pointView.setPointElement(pointMaps.get(i));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //RecyclerView显示数据条数
        @Override
        public int getItemCount() {
            return pointMaps.size();
        }

        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            PointView pointView;

            //在布局中找到所含有的UI组件
            public ViewHolder(View itemView) {
                super(itemView);
                pointView = (PointView) itemView.findViewById(R.id.point);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int index = (int) getItemId();
                Map<PointView.Type,Bitmap> pointmap = pointMaps.get(index);
                present.changePointImg(pointmap);
            }
        }
    }


}