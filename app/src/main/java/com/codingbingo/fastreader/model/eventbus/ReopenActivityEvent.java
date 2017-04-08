package com.codingbingo.fastreader.model.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/8.
 *
 * 在操作过程中，可能需要某些activity直接完成重新打开，已完成设置刷新等效果
 *
 * activityName全部采用如： BaseActivity.class.getName();
 */
public class ReopenActivityEvent {
    private List<String> activityList;

    public void addActivity(String activityName){
        if (activityList == null){
            activityList = new ArrayList<>();
        }

        activityList.add(activityName);
    }

    public List<String> getActivityList() {
        return activityList != null ? activityList : new ArrayList<String>();
    }
}
