package com.example.attendclasstpad.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * 为fragment布局量身定做的ViewPager适配器
 * 
 * 
 */
public class FragmentVPagerAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> fragmentList;

	public FragmentVPagerAdapter(FragmentManager manager, List<Fragment> fragmentList) {
		super(manager);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}
}
