package com.feedback.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feedback.Adapters.ViewPagerAdapterWithTitle;
import com.feedback.R;

import butterknife.BindView;


public class FragmentHome extends BaseFragment {

    @BindView(R.id.homeTabs)
    TabLayout homeTabs;
    @BindView(R.id.vpHome)
    ViewPager vpHome;

    private ViewPagerAdapterWithTitle adapter;

    public FragmentHome() {
    }

    public static FragmentHome createInstance() {
        return new FragmentHome();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refresh();
    }

    public void refresh()
    {
        adapter = new ViewPagerAdapterWithTitle(getChildFragmentManager());

        adapter.add(FragmentPosts.newInstance("pending"), "Pending");
        adapter.add(FragmentPosts.newInstance("actiontaken"), "Action taken");
        adapter.add(FragmentPosts.newInstance("rejected"), "Rejected");

        vpHome.setAdapter(adapter);
        homeTabs.setupWithViewPager(vpHome);

    }



}
