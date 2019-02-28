package org.div.vram.imagegallery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.div.vram.imagegallery.R;
import org.div.vram.imagegallery.util.FolderItem;

import java.util.Map;
import java.util.Set;

public class SpinnerAdapter extends BaseAdapter {

    private Map<String, FolderItem> foldersMap;

    private TextView txt_bucket_name;
    private RelativeLayout ll;

    public SpinnerAdapter(Map<String, FolderItem> foldersMap) {
        this.foldersMap = foldersMap;
    }

    @Override
    public int getCount() {
        return foldersMap.size();
    }

    @Override
    public Object getItem(int i) {
        return foldersMap.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_spinner, viewGroup, false);
        txt_bucket_name = view.findViewById(R.id.txt_bucket_name);
        ll = view.findViewById(R.id.ll);

        Set<String> set = foldersMap.keySet();
        String arr[] = new String[set.size()];
        arr = set.toArray(arr);
        FolderItem[] folderItems = new FolderItem[foldersMap.values().toArray().length];
        folderItems = foldersMap.values().toArray(folderItems);
        txt_bucket_name.setText(arr[i]);

        final FolderItem[] finalFolderItems = folderItems;
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onClick != null) {
                    onClick.onClick(finalFolderItems[i]);
                }


            }
        });
        return view;
    }

    private OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public interface OnClick {
        public void onClick(FolderItem item);
    }
}
