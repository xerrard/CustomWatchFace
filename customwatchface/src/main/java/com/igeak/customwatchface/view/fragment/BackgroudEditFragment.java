package com.igeak.customwatchface.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.igeak.customwatchface.presenter.IWatchFaceEditContract;
import com.igeak.customwatchface.presenter.WatchFaceEditPresent;
import com.igeak.customwatchface.view.activity.FaceEditActivity;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.List;


/**
 * Created by xuqiang on 16-5-11.
 */
public class BackgroudEditFragment extends Fragment implements IWatchFaceEditContract
        .IBackgroundView {

    private static final String TAG = "InnerFaceFrgment";
    private static final int SPAN_COUNT = 2;
    RecyclerView mRecyclerView = null;
    RecycleViewAdapter adapter;

    WatchFaceEditPresent present;
    List<Bitmap> bitmaps;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        present = ((FaceEditActivity) getActivity()).present;
        adapter = new RecycleViewAdapter(bitmaps);
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        bitmaps = present.loadbackImg();
        if (!adapter.isSetAdapter()) {
            adapter.setBitmaps(bitmaps);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.setBitmaps(bitmaps);
            adapter.notifyDataSetChanged();
        }
    }

    //继承自 RecyclerView.Adapter
    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

        private List<Bitmap> bitmaps;

        public boolean isSetAdapter() {
            return bitmaps != null;
        }

        public void setBitmaps(List<Bitmap> bitmaps) {
            this.bitmaps = bitmaps;
        }

        public RecycleViewAdapter(List<Bitmap> bitmaps) {
            this.bitmaps = bitmaps;
            setHasStableIds(true); //必须要加的代码，默认为false，当true，getItemId才有效
        }

        //RecyclerView显示的子View
        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.recycler_item_element,
                    null);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if (i > 0) {
                viewHolder.imageView.setImageBitmap(bitmaps.get(i - 1));
            } else {
                viewHolder.imageView.setImageResource(R.drawable.card_background);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //RecyclerView显示数据条数
        @Override
        public int getItemCount() {
            return bitmaps.size() + 1;
        }

        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView imageView;

            //在布局中找到所含有的UI组件
            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int index = (int) getItemId();
                if (index > 0) {
                    Bitmap bitmap = bitmaps.get(index - 1);
                    present.changeBackImg(bitmap);
                } else {
                    Crop.pickImage(getActivity());
                   // Crop.pickImage(BackgroudEditFragment.this);

                }
            }
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent result) {
//        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
//            Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
//            //Crop.of(result.getData(), destination).asSquare().start(this);
//            boolean isCircleCrop = true;
//            new Crop(result.getData()).output(destination).setCropType(isCircleCrop).start
//                    (getActivity());
//
//
//        } else if (requestCode == Crop.REQUEST_CROP) {
//            present.handleCrop(resultCode, result);
//        }
//    }

}
