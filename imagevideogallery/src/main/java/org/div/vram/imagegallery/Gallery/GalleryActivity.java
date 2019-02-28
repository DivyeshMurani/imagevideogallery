package org.div.vram.imagegallery.Gallery;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import org.div.vram.imagegallery.R;
import org.div.vram.imagegallery.util.Util;
import org.div.vram.imagegallery.adapter.GalleryAdapter;
import org.div.vram.imagegallery.util.DateUtil;
import org.div.vram.imagegallery.util.GalleryKey;
import org.div.vram.imagegallery.util.ImageItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static org.div.vram.imagegallery.util.GalleryKey.IMAGE_TYPE;
import static org.div.vram.imagegallery.util.GalleryKey.MAX_SELECT;
import static org.div.vram.imagegallery.util.GalleryKey.TOOLBAR_COLOR_CODE;
import static org.div.vram.imagegallery.util.GalleryKey.TYPE;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private GalleryAdapter adapter;
    private ContentResolver contentResolver;
    private ProgressBar progressBar;

    private List<Boolean> booleans;

    private List<ImageItem> imageItems = new ArrayList<>();
    private List<String> selectedImagesList = new ArrayList<>();
    private Toolbar toolbar;
    private String mediaType = IMAGE_TYPE;
    private int maxSelect = 0;

    int toolbarColor = Color.parseColor("#D81B60");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_gellary);
        recycleView = findViewById(R.id.recycleView);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);

        checkValueFromIntent();
        setUpRecycleView();
    }

    private void checkValueFromIntent() {
        Intent intent = getIntent();

        if (intent != null) {
            toolbarColor = intent.getIntExtra(TOOLBAR_COLOR_CODE, 0);
            mediaType = intent.getStringExtra(TYPE);
            maxSelect = intent.getIntExtra(MAX_SELECT, 50);
        }


        if (toolbarColor != 0) {
            toolbar.setBackgroundColor(toolbarColor);
        } else {
            toolbar.setBackgroundColor(Color.RED);
        }

        if (mediaType == null) {
            mediaType = IMAGE_TYPE;
        }

        toolbar.setTitle(mediaType);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Log.e("TAG_", mediaType + "");


    }

    private void setUpRecycleView() {
        adapter = new GalleryAdapter(this, imageItems);
        recycleView.setHasFixedSize(false);

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSmoothScrollbarEnabled(true);
        recycleView.setLayoutManager(manager);
        recycleView.setAdapter(adapter);
        recycleView.setNestedScrollingEnabled(false);


        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case 0:
                        return 4;
                    default:
                        return 1;
                }
            }
        });

        adapter.setSelectImage(new GalleryAdapter.SelectImage() {
            @Override
            public void select(int position, boolean b, RelativeLayout rl_selected, String path) {

                if (!b) {
                    if (selectedImagesList.size() == maxSelect) {
                        Toast.makeText(GalleryActivity.this,
                                "Max selected " + selectedImagesList.size(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (!b) {
                    rl_selected.setVisibility(View.VISIBLE);
                    selectedImagesList.add(path);
                } else {
                    rl_selected.setVisibility(View.GONE);
                    if (selectedImagesList.size() > 0) {
                        selectedImagesList.remove(path);
                    }
                }
                if (selectedImagesList.size() > 0) {
                    toolbar.setTitle(mediaType + " : Selected " + selectedImagesList.size());
                    menuItem.setVisible(true);
                } else {
                    toolbar.setTitle(mediaType);
                    menuItem.setVisible(false);

                }
                booleans.set(position, !b);
                adapter.notifyDataSetChanged();
            }
        });


        loadImage();

//        loadVideo();
    }


    private Date date = new Date();
    private Calendar resCal = Calendar.getInstance();

    private void loadImage() {

        progressBar.setVisibility(View.VISIBLE);


        Log.e("TAG_", "Load Folder And Images...");
        Observable.just("")
                .flatMap(new Function<String, ObservableSource<ImageItem>>() {
                    @Override
                    public ObservableSource<ImageItem> apply(String s) throws Exception {
//                        FolderItem allImagesFolderItem = null;
                        List<ImageItem> results = new ArrayList<>();


                        Uri contentUri;
                        String where;
                        String orderSort;
                        if (mediaType.equalsIgnoreCase(IMAGE_TYPE)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            where = MediaStore.Images.Media.SIZE + ">" + 50000;
                            orderSort = MediaStore.Images.Media.DATE_ADDED + " DESC";
                        } else {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            where = MediaStore.Video.Media.SIZE + ">" + 50000;
                            orderSort = MediaStore.Video.Media.DATE_ADDED + " DESC";
                        }

//                        Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                        String where = MediaStore.Video.Media.SIZE + ">" + 50000;
//                        String orderSort = MediaStore.Video.Media.DATE_ADDED + " DESC";

                        contentResolver = getContentResolver();
                        Cursor cursor;

//                        Cursor cursor = contentResolver.query(contentUri,
//                                Util.Imageprojections, where,
//                                null, orderSort);

                        if (mediaType.equalsIgnoreCase(IMAGE_TYPE)) {
                            cursor = contentResolver.query(contentUri,
                                    Util.Imageprojections, where,
                                    null, orderSort);
                        } else {
                            cursor = contentResolver.query(contentUri,
                                    Util.videoProjections, where,
                                    null, orderSort);
                        }

                        if (cursor == null) {
                        } else {
                            if (cursor.moveToFirst()) {

                                int pathCol;
                                int nameCol;
                                int DateCol;
                                int buck;


                                if (mediaType.equalsIgnoreCase(IMAGE_TYPE)) {
                                    pathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                                    nameCol = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                                    DateCol = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                                    buck = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                                } else {
                                    pathCol = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                                    nameCol = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                                    DateCol = cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);
                                    buck = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

                                }
//                                int pathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                                int nameCol = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
//                                int DateCol = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
//                                int buck = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                                long changeDate = cursor.getLong(DateCol);

                                date.setTime(DateUtil.timeAdd(changeDate));
                                resCal.setTime(date);


                                ImageItem i = new ImageItem(",", "", "", changeDate);
                                i.setType(ImageItem.DATE);
                                results.add(i);

                                do {
                                    String path = cursor.getString(pathCol);
                                    String name = cursor.getString(nameCol);
                                    long dateTime = cursor.getLong(DateCol);
                                    String bucketName = cursor.getString(buck);

                                    Date imageTime = new Date();
                                    imageTime.setTime(DateUtil.timeAdd(dateTime));

                                    Calendar rottingCal = Calendar.getInstance();
                                    rottingCal.setTime(imageTime);

                                    ImageItem item;

                                    boolean sameDay = resCal.get(Calendar.DAY_OF_YEAR) ==
                                            rottingCal.get(Calendar.DAY_OF_YEAR)
                                            &&
                                            resCal.get(Calendar.YEAR) == rottingCal.get(Calendar.YEAR);


//                                    Log.e("TAG_", "Date:-> " + date.equals(imageTime) + " 1__ " + DateUtil.time(date.getTime()) +
//                                            " 2__" + DateUtil.time(imageTime.getTime()) + " __Day " + sameDay);

//                                    Log.e("TAG__", "Same Day:-> " + sameDay);
                                    if (sameDay) {
                                        item = new ImageItem(path, name,
                                                bucketName, dateTime);
                                        item.setType(ImageItem.IMAGE);
                                    } else {
                                        date.setTime(imageTime.getTime());
                                        resCal.setTime(date);

                                        ImageItem time = new ImageItem(",", "", "", dateTime);
                                        time.setType(ImageItem.DATE);

                                        item = new ImageItem(path, name,
                                                bucketName, dateTime);
                                        item.setType(ImageItem.IMAGE);
                                        results.add(time);
                                    }

                                    results.add(item);
//                                    item.setDate();

//                                    if (FolderContentList.FOLDERS.size() == 0) {
//                                        allImagesFolderItem = new FolderItem("folder_all", path);
//                                        FolderContentList.addItem(allImagesFolderItem);
//                                    }
//
//                                    allImagesFolderItem.addImageItem(item);
//
//                                    String folderPath = new File(path).getParentFile().getAbsolutePath();
//                                    folderItem = FolderContentList.getItem(folderPath);
////                                    Log.e("TAG_", "apply: folderItem " + (folderItem == null));
//                                    if (folderItem == null) {
////                                        Log.e("TAG_", "Folder name " + StringUtils.getLastPathSegment(folderPath));
//                                        folderItem = new FolderItem(StringUtils.getLastPathSegment(folderPath), path);
//                                        FolderContentList.addItem(folderItem);
//                                    }
//                                    folderItem.addImageItem(item);
                                } while (cursor.moveToNext());

                            }
                            cursor.close();
                        }
                        return Observable.fromIterable(results);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ImageItem imageItem) {
                        List<ImageItem> items = new ArrayList<ImageItem>();
                        items.add(imageItem);
                        imageItems.addAll(items);
                        Log.e("TAG_", "onNext");


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG_", "onError: " + Log.getStackTraceString(e));
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG_", "onComplete");

                        booleans = new ArrayList<>();
                        booleans.clear();
                        for (int i = 0; i < imageItems.size(); i++) {
                            booleans.add(i, false);
                        }
                        adapter.setIsSelected(booleans);
                        adapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    private void loadVideo() {
        Log.e("TAG_", "Load Folder And Images...");

        Observable.just("")
                .flatMap(new Function<String, ObservableSource<ImageItem>>() {
                    @Override
                    public ObservableSource<ImageItem> apply(String s) throws Exception {
                        List<ImageItem> results = new ArrayList<>();
                        Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        String where = MediaStore.Video.Media.BUCKET_DISPLAY_NAME;
                        String orderSort = MediaStore.Video.Media.DATE_ADDED + " DESC";
                        contentResolver = getContentResolver();
                        Cursor cursor = getContentResolver().query(contentUri, Util.videoProjections,
                                "_data IS NOT NULL) GROUP BY (bucket_display_name",
                                null, orderSort);
                        if (cursor == null) {

                        } else {
                            if (cursor.moveToFirst()) {
                                int buckCol = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                                int pathCol = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                                int nameCol = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                                int DateCol = cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);

                                do {
                                    String path = cursor.getString(pathCol);
                                    String name = cursor.getString(nameCol);
                                    String buck = cursor.getString(buckCol);
                                    long dateTime = cursor.getLong(DateCol);
                                    ImageItem item = new ImageItem(path, name, buck, dateTime);
                                    results.add(item);
                                } while (cursor.moveToNext());
                            }
                            cursor.close();
                        }
                        return Observable.fromIterable(results);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ImageItem imageItem) {
                        List<ImageItem> items = new ArrayList<ImageItem>();
                        items.add(imageItem);
                        imageItems.addAll(items);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG_", "onError: " + Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG_", "onComplete");
                    }
                });
    }


    private void loadImagesa() {
        Log.e("TAG_", "Load Folder And Images...");

        Observable.just("")
                .flatMap(new Function<String, ObservableSource<ImageItem>>() {
                    @Override
                    public ObservableSource<ImageItem> apply(String s) throws Exception {

                        List<ImageItem> results = new ArrayList<>();
                        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        String where = MediaStore.Images.Media.SIZE + ">" + 50000;
                        String orderSort = MediaStore.Images.Media.DATE_ADDED + " DESC";
                        contentResolver = getContentResolver();
                        Cursor cursor = contentResolver.query(contentUri,
                                Util.Imageprojections, where,
                                null, orderSort);
                        if (cursor == null) {

                        } else {
                            if (cursor.moveToFirst()) {
//                                int pathCol = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//                                int nameCol = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                                int pathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                                int nameCol = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                                int DateCol = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                                do {
                                    String path = cursor.getString(pathCol);
                                    String name = cursor.getString(nameCol);
                                    long dateTime = cursor.getLong(DateCol);


                                    ImageItem item = new ImageItem(path, name, "", dateTime);
                                    Log.e("TAG_", "Folder Name :=> " + path);
                                    results.add(item);
                                } while (cursor.moveToNext());
                            }
                            cursor.close();
                        }
                        return Observable.fromIterable(results);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ImageItem imageItem) {
                        List<ImageItem> items = new ArrayList<ImageItem>();
                        items.add(imageItem);
                        imageItems.addAll(items);
                        adapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG_", "onError: " + Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG_", "onComplete");
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_select, menu);

        menuItem = menu.findItem(R.id.menu_select);
        menuItem.setVisible(false);

        return true;
    }

    private MenuItem menuItem;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        int i = item.getItemId();
        if (i == R.id.menu_select) {
            Intent intent = new Intent();
            intent.putExtra(GalleryKey.SELECTED_LIST, (Serializable) selectedImagesList);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        return false;
    }
}
