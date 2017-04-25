package com.codingbingo.fastreader.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.codingbingo.fastreader.Constants;
import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.base.BaseActivity;
import com.codingbingo.fastreader.model.LocalFile;
import com.codingbingo.fastreader.ui.adapter.FileListAdapter;
import com.codingbingo.fastreader.utils.FileUtils;


import java.util.List;

/**
 * 本地文本列表
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/5.
 */

public class LocalFileListActivity extends BaseActivity implements View.OnClickListener, FileListAdapter.OnItemClickListener{

    private RecyclerView localFileListView;
    private ImageView backBtn;
    private ImageView reloadBtn;

    private List<LocalFile> localFileList;
    private FileListAdapter localFileListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_local_file_list);

        initView();
    }

    private void initView(){
        localFileListView = (RecyclerView) findViewById(R.id.file_list);
        backBtn = (ImageView) findViewById(R.id.back_btn);
        reloadBtn = (ImageView) findViewById(R.id.reload_btn);

        backBtn.setOnClickListener(this);
        reloadBtn.setOnClickListener(this);
        ifNeedReadePermission();
    }

    private void ifNeedReadePermission(){
        // Here, thisActivity is the current activity
        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(LocalFileListActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LocalFileListActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(LocalFileListActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            0);
                }
            } else {
                readLocalFiles();
            }
        }else {
            readLocalFiles();
        }
    }

    private void readLocalFiles(){
        localFileList = FileUtils.getSupportFileList(this, new String[]{"txt"});
        localFileListAdapter = new FileListAdapter(this, localFileList);
        localFileListAdapter.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        localFileListView.setLayoutManager(linearLayoutManager);
        localFileListView.setAdapter(localFileListAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readLocalFiles();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.back_btn:
                finish();
                break;
            case R.id.reload_btn:
                synchronized (localFileList) {
                    localFileList.clear();
                    localFileList.addAll(FileUtils.getSupportFileList(this, new String[]{"txt"}));
                    localFileListAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void OnClick(int position) {
        LocalFile localFile = localFileList.get(position);

        Intent intent = new Intent(this, ReadingActivity.class);
        intent.putExtra("type", Constants.TYPE_FROM_LOCAL_FILE_ACTIVITY);
        intent.putExtra("bookPath", localFile.getFilePath());
        startActivity(intent);
        //关闭当前activity
        finish();
    }
}

