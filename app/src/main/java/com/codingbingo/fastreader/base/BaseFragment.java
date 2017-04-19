package com.codingbingo.fastreader.base;

import android.app.Fragment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.dao.DaoSession;
import com.codingbingo.fastreader.view.LoadingDialog;

/**
 * Created by bingo on 2016/12/24.
 */

public abstract class BaseFragment extends Fragment {
    private LoadingDialog loadingDialog;
    private Toast toast;

    public abstract String getFragmentName();

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

    protected Window getWindow() {
        return getActivity().getWindow();
    }

    protected void switchFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(params);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(params);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 获取数据库session
     *
     * @return
     */
    protected DaoSession getDaoSession() {
        return FRApplication.getInstance().getDaoSession();
    }

    protected void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * 显示加载动画
     *
     * @param message
     */
    protected void showLoadingDialog(String message) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
        }

        loadingDialog.show();
    }

    /**
     * 关闭加载动画
     */
    protected void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
