package org.div.vram.imagegallery.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.div.vram.imagegallery.R;
import org.div.vram.imagegallery.util.DateUtil;
import org.div.vram.imagegallery.util.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private List<ImageItem> imageItems = new ArrayList<>();
    private List<Boolean> isSelected = new ArrayList<>();

    public static final int DATE = 0;
    public static final int IMAGE = 1;


    public void setIsSelected(List<Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public GalleryAdapter(Activity activity, List<ImageItem> imageItems) {
        this.activity = activity;
        this.imageItems = imageItems;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if (type == DATE) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_time, viewGroup, false);
            return new TimeHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_gallery, viewGroup, false);
            return new GalleryHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        int type = imageItems.get(i).getType();
        if (imageItems.get(i).getType() == DATE) {
            TimeHolder holder = (TimeHolder) viewHolder;
            long milliSeconds = imageItems.get(i).getTime();
            String time = DateUtil.getTime(milliSeconds);
            holder.txt_time.setText(time);

        } else {
            final GalleryHolder holder = (GalleryHolder) viewHolder;
            String path = imageItems.get(i).getPath();

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(activity).
//                    load("http://i.imgur.com/DvpvklR.png")
        load(Uri.fromFile(new File(path)))
                    .apply(requestOptions)
                    .placeholder(R.drawable.default_image)
                    .into(holder.image_drawee);
//            imageLoader.displayImage(path, holder.image_drawee);

            if (isSelected.get(i)) {
                holder.rl_selected.setVisibility(View.VISIBLE);
            } else {
                holder.rl_selected.setVisibility(View.GONE);
            }

            holder.image_drawee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectImage != null) {
                        boolean b = isSelected.get(i);
                        selectImage.select(i, b, holder.rl_selected, imageItems.get(i).getPath());
                    }
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (imageItems.get(position).getType() == ImageItem.DATE) {
            return DATE;
        } else {
            return IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    public class GalleryHolder extends RecyclerView.ViewHolder {
        private ImageView image_drawee;
        private TextView txt_bucket_name;
        private RelativeLayout rl_selected;

        public GalleryHolder(@NonNull View itemView) {
            super(itemView);
            image_drawee = itemView.findViewById(R.id.image_drawee);
            txt_bucket_name = itemView.findViewById(R.id.txt_bucket_name);
            rl_selected = itemView.findViewById(R.id.rl_selected);
        }
    }

    public class TimeHolder extends RecyclerView.ViewHolder {
        private TextView txt_time;

        public TimeHolder(@NonNull View itemView) {
            super(itemView);
            txt_time = itemView.findViewById(R.id.txt_time);
        }
    }

    private SelectImage selectImage;

    public void setSelectImage(SelectImage selectImage) {
        this.selectImage = selectImage;
    }

    public interface SelectImage {
        void select(int position, boolean b, RelativeLayout rl_selected, String path);
    }
}
