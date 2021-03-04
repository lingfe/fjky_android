package com.fjkyly.paradise.ui.views;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author A lonely cat
 *
 * Provide a convenient way to bind BottomNavigationView and ViewPager2
 */
public final class NavigationMediator extends ViewPager2.OnPageChangeCallback {

    private static final String TAG = NavigationMediator.class.getSimpleName();
    //第一个是MenuItem的id，第二个是下标
    private final Map<Integer, Integer> mMap = new HashMap<>();
    private final BottomNavigationView mBottomNavigationView;
    private final ViewPager2 mViewPager2;
    //是否平滑滚动（默认为true）
    private boolean mSmoothScroll;
    //是否已经绑定过了
    private boolean attached;
    private NavigationListener mNavigationListener;

    public NavigationMediator(BottomNavigationView navigationView, ViewPager2 viewPager) {
        this(navigationView, viewPager, true);
    }

    public NavigationMediator(@NonNull BottomNavigationView navigationView, @NonNull ViewPager2 viewPager, boolean smoothScroll) {
        mBottomNavigationView = navigationView;
        mViewPager2 = viewPager;
        mSmoothScroll = smoothScroll;
    }

    /**
     * bind mBottomNavigationView 和 mViewPager2
     *
     * Before using, you must set the ID for each menu，Otherwise it won't work.
     */
    public void attach() {
        //避免重复绑定
        if (attached) throw new IllegalStateException("NavigationMediator is already attached");
        attached = true;
        mNavigationListener = new NavigationListener(mViewPager2);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mNavigationListener);
        mViewPager2.registerOnPageChangeCallback(this);
        int menuCount = mBottomNavigationView.getMenu().size();
        //Log.d(TAG, "attach: ========> menuCount is " + menuCount);
        for (int i = 0; i < menuCount; i++) {
            mMap.put(mBottomNavigationView.getMenu().getItem(i).getItemId(), i);
        }
    }

    /**
     * unbind
     */
    public void detach() {
        mNavigationListener = null;
        mViewPager2.unregisterOnPageChangeCallback(this);
        attached = false;
    }

    /**
     * current state whether attached
     *
     * @return isAttached
     */
    public boolean isAttached() {
        return attached;
    }

    /**
     * current state whether isSmoothScroll
     *
     * @return isSmoothScroll
     */
    public boolean isSmoothScroll() {
        return mSmoothScroll;
    }

    /**
     * set current state whether smoothScroll
     *
     * @param smoothScroll
     */
    public void setSmoothScroll(boolean smoothScroll) {
        mSmoothScroll = smoothScroll;
    }

    /**
     * getIndexByItem
     *
     * @param itemView
     * @return
     */
    public int getIndexByItem(MenuItem itemView) {
        Integer index = mMap.get(itemView.getItemId());
        return index == null ? -1 : index;
    }

    @Override
    public void onPageSelected(int position) {
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    private class NavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @NonNull
        private final WeakReference<ViewPager2> ViewPager2Ref;

        public NavigationListener(ViewPager2 viewPager2) {
            ViewPager2Ref = new WeakReference<>(viewPager2);
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Integer index = mMap.get(item.getItemId());
            //Log.d(TAG, "onNavigationItemSelected: ======> index is " + index);
            if (index == null) return false;
            ViewPager2 viewPager2 = ViewPager2Ref.get();
            if (viewPager2 != null && attached) viewPager2.setCurrentItem(index, mSmoothScroll);
            return true;
        }
    }
}
