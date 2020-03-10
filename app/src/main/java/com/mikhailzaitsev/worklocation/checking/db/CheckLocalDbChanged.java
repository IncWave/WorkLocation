package com.mikhailzaitsev.worklocation.checking.db;

import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.mikhailzaitsev.worklocation.list.GroupsFragment;
import com.mikhailzaitsev.worklocation.db.Db;
import com.mikhailzaitsev.worklocation.map.MapFragment;

public class CheckLocalDbChanged extends AsyncTask<Fragment,Void,Void> {
    private MapFragment mapFragment;
    private GroupsFragment groupsFragment;
    @Override
    protected Void doInBackground(Fragment... fragments) {
        mapFragment = (MapFragment) fragments[0];
        groupsFragment = (GroupsFragment) fragments[1];
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (Db.newInstance().isDataHasBeenChanged()){
            mapFragment.dataHasBeenChanged();
            groupsFragment.dataHasBeenChanged();
            Db.newInstance().setDataHasBeenChanged(false);
        }
        if (Db.newInstance().isOnlineHasBeenChanged()){
            groupsFragment.dataHasBeenChanged();
            Db.newInstance().setOnlineHasBeenChanged(false);
        }
    }
}
