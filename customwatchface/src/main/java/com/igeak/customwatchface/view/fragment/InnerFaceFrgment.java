package com.igeak.customwatchface.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.presenter.IPresenter;
import com.igeak.customwatchface.presenter.IWatchFacesContract;
import com.igeak.customwatchface.presenter.WatchFaceListPresent;
import com.igeak.customwatchface.R;
import com.igeak.customwatchface.view.activity.FaceEditActivity;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;
import com.igeak.customwatchface.view.activity.FaceDetailActivity;

import java.util.List;

/**
 * Created by xuqiang on 16-5-11.
 */
public class InnerFaceFrgment extends Fragment implements IWatchFacesContract.IWatchFacesView {

    private static final String TAG = "InnerFaceFrgment";
    private static final int SPAN_COUNT = 2;
    RecyclerView mRecyclerView = null;
    RecycleViewAdapter mRecycleViewAdapter = null;

    WatchFaceListPresent present = null;
    WatchFacesModel.FacePath facePath = WatchFacesModel.FacePath.FACE_INNER;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_face, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecycleViewAdapter = new RecycleViewAdapter();

        present = new WatchFaceListPresent(this, getActivity().getApplicationContext());
        present.getWatchfaceBeanList(facePath);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void updateWatchFaceBeanList(List<WatchFaceBean> watchFaceBeanList) {

        if (!mRecycleViewAdapter.isSetAdapter()) {
            mRecycleViewAdapter.setWatchList(watchFaceBeanList);
            mRecyclerView.setAdapter(mRecycleViewAdapter);
        } else {
            mRecycleViewAdapter.setWatchList(watchFaceBeanList);
            mRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateWatchFace(WatchPreviewView imageView, WatchFace watchFace) {
        imageView.setElements(watchFace);
    }

    @Override
    public void onWatchCreated(WatchFaceBean watchFaceBean) {
//        Intent intent = new Intent(getContext(), FaceEditActivity.class);
//        intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, watchFaceBean);
//        intent.putExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, facePath.equals
//                (WatchFacesModel.FacePath.FACE_CUSTOM));
//        startActivity(intent);
    }


    //继承自 RecyclerView.Adapter
    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

        private List<WatchFaceBean> watchfaceList = null;

        public boolean isSetAdapter() {
            return watchfaceList != null;
        }

        public void setWatchList(List<WatchFaceBean> watchfaceList) {
            this.watchfaceList = watchfaceList;
        }

        public RecycleViewAdapter() {
            setHasStableIds(true);
        }

        //RecyclerView显示的子View
        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.recycler_item, null);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            WatchFaceBean watchface = watchfaceList.get(i);
            viewHolder.textView.setText(watchface.getName());
            present.loadWatchimg(viewHolder.imageView, watchface, facePath);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        //RecyclerView显示数据条数
        @Override
        public int getItemCount() {
            return watchfaceList.size();
        }

        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                PopupMenu.OnMenuItemClickListener {

            TextView textView;
            WatchPreviewView imageView;
            ImageButton imb;

            //在布局中找到所含有的UI组件
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
                imageView = (WatchPreviewView) itemView.findViewById(R.id.imageView);
                imb = (ImageButton) itemView.findViewById(R.id.option_menu);
                imb.setOnClickListener(this);
                imageView.setOnClickListener(this);
            }


            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.imageView) {

                    Intent intent = new Intent(getContext(), FaceDetailActivity.class);
                    intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, watchfaceList.get((int)
                            getItemId()));
                    intent.putExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, facePath.equals
                            (WatchFacesModel.FacePath.FACE_CUSTOM));
                    startActivity(intent);
                } else if (v.getId() == R.id.option_menu) {
                    PopupMenu popup = new PopupMenu(getActivity(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.option_menu_inner, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(this);
                }

            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.option_new) {
                    //present.creatNewFace(watchfaceList.get((int) getItemId()));
                    Intent intent = new Intent(getContext(), FaceEditActivity.class);
                    intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, watchfaceList.get((int)
                            getItemId()));
                    intent.putExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, facePath.equals
                            (WatchFacesModel.FacePath.FACE_CUSTOM));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        }
    }
}
