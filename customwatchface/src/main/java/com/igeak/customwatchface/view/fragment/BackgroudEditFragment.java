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
import com.igeak.customwatchface.model.PicOperation;
import com.igeak.customwatchface.presenter.IWatchFaceEditContract;
import com.igeak.customwatchface.presenter.WatchFaceEditPresent;
import com.igeak.customwatchface.util.FileUtil;
import com.igeak.customwatchface.util.MyUtils;
import com.igeak.customwatchface.view.activity.FaceEditActivity;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by xuqiang on 16-5-11.
 */
public class BackgroudEditFragment extends Fragment implements IWatchFaceEditContract
        .IBackgroundView {

    private static final String TAG = "InnerFaceFrgment";
    private static final int SPAN_COUNT = 3;
    RecyclerView mRecyclerView = null;
    RecycleViewAdapter adapter;

    WatchFaceEditPresent present;
    List<InputStream> bitmaps;


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
        bitmaps = present.loadbackImg();
        if (!adapter.isSetAdapter()) {
            adapter.setInputStreams(bitmaps);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.setInputStreams(bitmaps);
            adapter.notifyDataSetChanged();
        }
        return rootView;
    }


    //继承自 RecyclerView.Adapter
    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

        private List<InputStream> inputStreams;
        private List<Bitmap> bitmaps;

        public boolean isSetAdapter() {
            return inputStreams != null;
        }

        public void setInputStreams(List<InputStream> inputStreams) {
            this.inputStreams = inputStreams;
            bitmaps = new ArrayList<Bitmap>();
        }

        public RecycleViewAdapter(List<InputStream> inputStreams) {
            this.inputStreams = inputStreams;
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
                final ImageView imageView = viewHolder.imageView;
                final int index = i;
                final InputStream is = inputStreams.get(i - 1);
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        final int height = imageView.getHeight();
                        final int width = imageView.getWidth();
                        Observable.create(new Observable.OnSubscribe<Bitmap>() {
                            @Override
                            public void call(Subscriber<? super Bitmap> subscriber) {
                                try {
                                    Bitmap bitmap;
                                    if ((bitmaps.size() <= (index - 1)) || (bitmaps.get(index -
                                            1) == null)) {
                                        //如果当前已经有图片，就不要再重复加载了
                                        bitmap = PicOperation.InputStream2Bitmap(is, width, height);
                                        //bitmaps.add(index-1,bitmap);
                                        MyUtils.addAtPos(bitmaps, index - 1, bitmap);
                                    } else {
                                        bitmap = bitmaps.get(index - 1);
                                    }
                                    subscriber.onNext(bitmap);
                                    subscriber.onCompleted();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);

                                }

                            }
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Bitmap>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Bitmap bitmap) {
                                        imageView.setImageBitmap(bitmap);

                                    }
                                });
                    }
                });
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
            return inputStreams.size() + 1;
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
                    //Crop.pickImage(getActivity());
                    Crop.pickImage(getContext(), BackgroudEditFragment.this);
                }
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
            Crop.of(result.getData(), destination).asCircle(true).asPng(true).start(getContext(),
                    this);
        } else if (requestCode == Crop.REQUEST_CROP) {
            present.handleCrop(resultCode, result);
        }
    }
}
