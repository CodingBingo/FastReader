package com.codingbingo.fastreader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.greenrobot.eventbus.EventBus;

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

        localFileList = FileUtils.getSupportFileList(this, new String[]{"txt"});
        localFileListAdapter = new FileListAdapter(this, localFileList);
        localFileListAdapter.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        localFileListView.setLayoutManager(linearLayoutManager);
        localFileListView.setAdapter(localFileListAdapter);
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

