package com.codingbingo.fastreader.base;

import android.support.v4.app.Fragment;

import com.avos.avoscloud.AVAnalytics;
import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.dao.DaoSession;

/**
 * Created by bingo on 2016/12/24.
 */

public abstract class BaseFragment extends Fragment {

    abstract String getFragmentName();

    @Override
    public void onPause() {
        super.onPause();

        AVAnalytics.onFragmentEnd(getFragmentName());
    }

    @Override
    public void onResume() {
        super.onResume();

        AVAnalytics.onFragmentStart(getFragmentName());
    }

    /**
     * 获取数据库session
     * @return
     */
    protected DaoSession getDaoSession(){
        return ((FRApplication) getActivity().getApplication()).getDaoSession();
    }
}
