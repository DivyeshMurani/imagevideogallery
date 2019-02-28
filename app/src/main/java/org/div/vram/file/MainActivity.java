package org.div.vram.file;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.div.vram.imagegallery.Gallery.GalleryActivity;
import org.div.vram.imagegallery.util.GalleryKey;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private ImageAdapter adapter;
    private List<String> list = new ArrayList<>();
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycleView = findViewById(R.id.recycleView);
        btn = findViewById(R.id.btn);

        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setHasFixedSize(true);
        adapter = new ImageAdapter(list);
        recycleView.setAdapter(adapter);

        int colorCode = getResources().getColor(R.color.colorPrimaryDark);
        Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
        intent.putExtra(GalleryKey.TOOLBAR_COLOR_CODE, colorCode);
        intent.putExtra(GalleryKey.TYPE, GalleryKey.IMAGE_TYPE);
        intent.putExtra(GalleryKey.MAX_SELECT, 10);
        startActivityForResult(intent, 3000);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int colorCode = getResources().getColor(R.color.colorPrimaryDark);
                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                intent.putExtra(GalleryKey.TOOLBAR_COLOR_CODE, colorCode);
                intent.putExtra(GalleryKey.TYPE, GalleryKey.IMAGE_TYPE);
                intent.putExtra(GalleryKey.MAX_SELECT, 10);
                startActivityForResult(intent, 3000);


            }
        });


//        openGallery();
    }

    int SELECT_IMAGE = 100;

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            List<String> list = (List<String>) data.getSerializableExtra(GalleryKey.SELECTED_LIST);
            this.list.addAll(list);
            adapter.notifyDataSetChanged();

//            for (String path : list) {
//                Log.e("TAG_", path);
//            }
        }
//        if (requestCode == SELECT_IMAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    String sourPath = getPath(data.getData());
//                    File file = new File(
//                            Environment.getExternalStorageDirectory() + "/name");
//                    if (!file.exists()) {
//                        file.mkdirs();
//                    }
//                    try {
//                        new FileUtil()
//                                .fileName("xyz.jpg")
//                                .folderName("A-team/ABC")
//                                .sourcePath(sourPath)
//                                .save();
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e("TAG_", e.getMessage());
//                    }
//                } else {
//                    Log.e("TAG_", "data is null");
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this,
//                        "Canceled", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    public class ImageAdapter extends RecyclerView.Adapter {

        private List<String> list = new ArrayList<>();

        public ImageAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_img, viewGroup, false);
            return new ImageHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ImageHolder holder = (ImageHolder) viewHolder;

            Glide.with(getApplicationContext())
                    .load(Uri.fromFile(new File(list.get(i))))
//                    .override(imageWidth,imageHeight)
                    .into(holder.img);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ImageHolder extends RecyclerView.ViewHolder {

            private ImageView img;

            public ImageHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);


            }
        }
    }

    public String getPath(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found_one";
        }
        return result;
    }


}
