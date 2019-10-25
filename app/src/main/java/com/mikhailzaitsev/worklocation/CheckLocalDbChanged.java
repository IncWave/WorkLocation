package com.mikhailzaitsev.worklocation;

import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.mikhailzaitsev.worklocation.Db.Db;
import com.mikhailzaitsev.worklocation.Fragments.GroupsFragment;
import com.mikhailzaitsev.worklocation.Fragments.MapFragment;

public class CheckLocalDbChanged extends AsyncTask<Fragment,Void,Void> {
    @Override
    protected Void doInBackground(Fragment... fragments) {
        MapFragment mapFragment = (MapFragment) fragments[0];
        GroupsFragment groupsFragment = (GroupsFragment) fragments[1];
        if (Db.newInstance().isDataHasBeenChanged()){
            mapFragment.dataHasBeenChanged();
            groupsFragment.dataHasBeenChanged();
            Db.newInstance().setDataHasBeenChanged(false);
        }
        if (Db.newInstance().isOnlineHasBeenChanged()){
            groupsFragment.dataHasBeenChanged();
            Db.newInstance().setOnlineHasBeenChanged(false);
        }
        return null;
    }
}
