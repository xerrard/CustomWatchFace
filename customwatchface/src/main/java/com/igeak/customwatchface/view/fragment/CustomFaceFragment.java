package com.igeak.customwatchface.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.igeak.android.common.api.GeakApiClient;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.MyApplication;
import com.igeak.customwatchface.R;
import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.model.WatchFacesModel;
import com.igeak.customwatchface.presenter.IWatchFacesContract;
import com.igeak.customwatchface.presenter.WatchFaceListPresent;
import com.igeak.customwatchface.view.activity.MenuActivity;
import com.igeak.customwatchface.view.view.ItemDecorationAlbumColumns;
import com.igeak.customwatchface.view.view.watchfaceview.WatchPreviewView;
import com.igeak.customwatchface.view.activity.FaceDetailActivity;
import com.igeak.customwatchface.view.activity.FaceEditActivity;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuqiang on 16-5-11.
 */
public class CustomFaceFragment extends Fragment implements IWatchFacesContract.IWatchFacesView {

    private static final String TAG = "CustomFaceFragment";
    private static final int SPAN_COUNT = 2;
    RecyclerView mRecyclerView = null;
    RecycleViewAdapter mRecycleViewAdapter = null;
    WatchFaceListPresent present = null;
    private WatchFacesModel.FacePath facePath = WatchFacesModel.FacePath.FACE_CUSTOM;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_facelist, container, false);
        rootView.setTag(TAG);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                getResources().getDimensionPixelSize(R.dimen.divider_spacing_horizon),
                getResources().getDimensionPixelSize(R.dimen.divider_spacing_vertical),
                SPAN_COUNT));
        mRecycleViewAdapter = new RecycleViewAdapter();
        present = new WatchFaceListPresent(this, getActivity().getApplicationContext());
        //present.getWatchfaceBeanList(facePath);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        present.getWatchfaceBeanList(facePath);
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
    public void updateWatchFace(final WatchPreviewView imageView, final WatchFace watchFace) {


        Observable.create(new Observable.OnSubscribe<WatchPreviewView>() {
            @Override
            public void call(Subscriber<? super WatchPreviewView> subscriber) {
                imageView.setElements(watchFace);
                subscriber.onNext(imageView);
                subscriber.onCompleted();

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WatchPreviewView>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WatchPreviewView imageView) {
                        imageView.invalidate();
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_MENU) {
            switch (resultCode) {
                case 2000:
                    deleteOperation();
                    break;
                case 2001:
                    break;
                case 2002:
                    break;
                case 2003:
                    break;
                case 2004:
                    break;
                default:
                    break;
            }
        }


        if (item.getItemId() == R.id.option_delete) {
            deleteOperation();
            return true;
        } else if (item.getItemId() == R.id.option_rename) {
            renameOperation();
            return true;
        } else if (item.getItemId() == R.id.option_edit) {
            Intent intent = new Intent(getContext(), FaceEditActivity.class);
            intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, watchfaceList.get((int)
                    getItemId()));
            intent.putExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, facePath.equals
                    (WatchFacesModel.FacePath.FACE_CUSTOM));
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.option_sent2watch) {
            sendToWatch(watchfaceList.get((int) getItemId()));
        }

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
            View v = getActivity().getLayoutInflater().inflate(R.layout
                    .recycler_item_face_custom, null);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            WatchFaceBean watchface = watchfaceList.get(i);
            viewHolder.textView.setText(watchface.getName());
            //viewHolder.imageView.setElements(watchface);
            present.loadWatchimg(viewHolder.imageView, watchface, facePath);
            viewHolder.itemView.setBackgroundColor(0x1fffffff);
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
            View itemView;

            //在布局中找到所含有的UI组件
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
                imageView = (WatchPreviewView) itemView.findViewById(R.id.imageView);
                imb = (ImageButton) itemView.findViewById(R.id.option_menu);
                imb.setOnClickListener(this);
                imageView.setOnClickListener(this);
                this.itemView = itemView;
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
//                    PopupMenu popup = new PopupMenu(getActivity(), v);
//                    MenuInflater inflater = popup.getMenuInflater();
//                    inflater.inflate(R.menu.option_menu_custom, popup.getMenu());
//                    popup.show();
//                    popup.setOnMenuItemClickListener(this);

                    Intent intent = new Intent(getContext(), MenuActivity.class);
                    intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, watchfaceList.get((int)
                            getItemId()));
                    startActivityForResult(intent, Const.REQUEST_MENU);
                    itemView.setBackgroundColor(getResources().getColor(android.R.color.white));
                }

            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.option_delete) {
                    deleteOperation();
                    return true;
                } else if (item.getItemId() == R.id.option_rename) {
                    renameOperation();
                    return true;
                } else if (item.getItemId() == R.id.option_edit) {
                    Intent intent = new Intent(getContext(), FaceEditActivity.class);
                    intent.putExtra(Const.INTENT_EXTRA_KEY_WATCHFACE, watchfaceList.get((int)
                            getItemId()));
                    intent.putExtra(Const.INTENT_EXTRA_KEY_ISCUSTOM, facePath.equals
                            (WatchFacesModel.FacePath.FACE_CUSTOM));
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.option_sent2watch) {
                    sendToWatch(watchfaceList.get((int) getItemId()));
                }
                return false;
            }

            private void deleteOperation() {
                present.deleteWatchFace(watchfaceList, (int) getItemId());
            }

            private void renameOperation() {
                final EditText et = new EditText(getContext());
                String name = watchfaceList.get((int) getItemId()).getName();
                et.setText(name);
                et.setSelection(name.length());
                new AlertDialog.Builder(getContext())
                        .setTitle("请输入")
                        .setIcon(
                                android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = et.getText().toString();
                                present.changeName(name, watchfaceList, (int) getItemId());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        }
    }

    public void sendToWatch(WatchFaceBean watchfacebean) {
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        GeakApiClient googleApiClient = myApplication.mGoogleApiclent;
        present.zipFileAndSentToWatch(googleApiClient, watchfacebean, facePath);
    }

    @Override
    public void updateWatchSent(WatchFaceBean watchFace) {
        Toast.makeText(getActivity().getApplicationContext()
                , watchFace.getName() + getString(R.string.has_sent)
                , Toast.LENGTH_LONG).show();

    }

    @Override
    public void showThrowable(Throwable e) {
        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                .show();

    }
}
