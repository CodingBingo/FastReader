package com.codingbingo.fastreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.model.LocalFile;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/6.
 */

public class FileListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<LocalFile> localFileList;

    private OnItemClickListener onItemClickListener;

    public FileListAdapter(Context mContext, @NotNull List<LocalFile> localFileList) {
        this.mContext = mContext;
        this.localFileList = localFileList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class FileHolder extends RecyclerView.ViewHolder{
        private TextView fileName;
        private ImageView fileIcon;
        private TextView fileSize;

        public FileHolder(View itemView) {
            super(itemView);

            fileIcon = (ImageView) itemView.findViewById(R.id.file_icon);
            fileSize = (TextView) itemView.findViewById(R.id.file_size);
            fileName = (TextView) itemView.findViewById(R.id.file_name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View fileItemView = LayoutInflater.from(mContext).inflate(R.layout.file_item, null);
        FileHolder fileHolder = new FileHolder(fileItemView);
        return fileHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LocalFile localFile = localFileList.get(position);

        FileHolder fileHolder = (FileHolder) holder;
        fileHolder.fileName.setText(localFile.getFileName());
        fileHolder.fileSize.setText(localFile.getFileSize() * 1.0 / 1000 + " KB");

        fileHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.OnClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (localFileList != null) {
            return localFileList.size();
        }else{
            return 0;
        }
    }

    public interface OnItemClickListener{
        void OnClick(int position);
    }
}
