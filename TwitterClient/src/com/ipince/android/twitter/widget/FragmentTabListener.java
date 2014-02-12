package com.ipince.android.twitter.widget;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class FragmentTabListener implements TabListener {

    private final FragmentActivity parent;
    private final int containerId;
    private final Fragment fragment;
    private boolean added = false;

    public FragmentTabListener(FragmentActivity parent, int containerId, Fragment fragment) {
        this.parent = parent;
        this.containerId = containerId;
        this.fragment = fragment;
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        FragmentManager manager = parent.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
        if (!added) {
            fts.add(containerId, fragment);
        } else {
            fts.attach(fragment);
        }
        fts.commit();
        added = true;
    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction ft) {
        if (added) {
            FragmentManager manager = parent.getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
            fts.detach(fragment);
            fts.commit();
        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // Do nothing.
    }
}
