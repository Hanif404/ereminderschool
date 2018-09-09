package org.karungkung.ereminderschool.guru;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import org.karungkung.ereminderschool.guru.models.Siswa;

import java.util.List;

/**
 * Created by hanif on 19/08/18.
 */

public class AbsensiDiffCallBack extends DiffUtil.Callback {
    private final List<Siswa> mOldTaskList;
    private final List<Siswa> mNewTaskList;

    public AbsensiDiffCallBack(List<Siswa> mOldTaskList, List<Siswa> mNewTaskList) {
        this.mOldTaskList = mOldTaskList;
        this.mNewTaskList = mNewTaskList;
    }

    @Override
    public int getOldListSize() {
        return mOldTaskList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewTaskList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldTaskList.get(oldItemPosition).getId() == mNewTaskList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Siswa oldTask = mOldTaskList.get(oldItemPosition);
        final Siswa newTask = mNewTaskList.get(newItemPosition);

        Log.d("test", oldTask.getStatus() + " "+ newTask.getStatus());
        return oldTask.getStatus() == newTask.getStatus();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
