package net.osmand.plus.dashboard;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.audionotes.DashAudioVideoNotesFragment;
import net.osmand.plus.helpers.ScreenOrientationHelper;
import net.osmand.plus.monitoring.DashTrackFragment;
import net.osmand.plus.views.controls.FloatingActionButton;

/**
 * Created by dummy on 03.03.15.
 */
public class DashboardOnMap {


	private MapActivity ma;
	FloatingActionButton fabButton;
	boolean floatingButtonVisible = false;
	private FrameLayout dashboardView;


	public DashboardOnMap(MapActivity ma) {
		this.ma = ma;
	}


	public void createDashboardView() {
		dashboardView = (FrameLayout) ma.getLayoutInflater().inflate(R.layout.dashboard_over_map, null, false);
		dashboardView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setDashboardVisibility(false);
			}
		};
		dashboardView.findViewById(R.id.content).setOnClickListener(listener);
		dashboardView.setOnClickListener(listener);
		((FrameLayout)ma.getMapView().getParent()).addView(dashboardView);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			fabButton = new FloatingActionButton.Builder(ma)
					.withDrawable(ma.getResources().getDrawable(R.drawable.ic_action_map))
					.withButtonColor(Color.parseColor("#ff8f00"))
					.withGravity(Gravity.BOTTOM | Gravity.RIGHT)
					.withMargins(0, 0, 16, 16)
					.create();
			fabButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					setDashboardVisibility(false);
					fabButton.hideFloatingActionButton();
				}
			});
			fabButton.hideFloatingActionButton();
		}

		if (ScreenOrientationHelper.isOrientationPortrait(ma)) {
			((NotifyingScrollView)dashboardView.findViewById(R.id.main_scroll)).setOnScrollChangedListener(onScrollChangedListener);
		}

	}


	public void setDashboardVisibility(boolean visible) {
		if (visible) {
			addDashboardFragments();
			dashboardView.setVisibility(View.VISIBLE);
			if (floatingButtonVisible) {
				fabButton.showFloatingActionButton();
			}
			//View close = dashboardView.findViewById(R.id.close_dashboard);
			if (ScreenOrientationHelper.isOrientationPortrait(ma)) {
				//close.setVisibility(View.VISIBLE);
			} else {
				//close.setVisibility(View.GONE);
			}
		} else {
			ma.findViewById(R.id.dashboard).setVisibility(View.GONE);
		}
	}

	private void addDashboardFragments(){
		FragmentManager manager =ma. getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		if (manager.findFragmentByTag(DashSearchFragment.TAG) == null) {
			fragmentTransaction.add(R.id.content, new DashSearchFragment(), DashSearchFragment.TAG);
		}
		if (manager.findFragmentByTag(DashRecentsFragment.TAG) == null) {
			fragmentTransaction.add(R.id.content, new DashRecentsFragment(), DashRecentsFragment.TAG);
		}
		if (manager.findFragmentByTag(DashFavoritesFragment.TAG) == null) {
			fragmentTransaction.add(R.id.content, new DashFavoritesFragment(), DashFavoritesFragment.TAG);
		}
		if (manager.findFragmentByTag(DashAudioVideoNotesFragment.TAG) == null) {
			fragmentTransaction.add(R.id.content, new DashAudioVideoNotesFragment(), DashAudioVideoNotesFragment.TAG);
		}
		if (manager.findFragmentByTag(DashTrackFragment.TAG) == null) {
			fragmentTransaction.add(R.id.content, new DashTrackFragment(), DashTrackFragment.TAG);
		}
		//fragmentTransaction.add(R.id.content, new DashUpdatesFragment(), DashUpdatesFragment.TAG);
		if (manager.findFragmentByTag(DashPluginsFragment.TAG) == null) {
			fragmentTransaction.add(R.id.content, new DashPluginsFragment(), DashPluginsFragment.TAG);
		}

		fragmentTransaction.commit();

	}

	private void addErrorFragment() {
		FragmentManager manager = ma.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		if (manager.findFragmentByTag(DashErrorFragment.TAG) == null) {
			DashErrorFragment errorFragment = new DashErrorFragment();
			fragmentTransaction.add(R.id.content, errorFragment, DashErrorFragment.TAG).commit();
		}
	}



	private NotifyingScrollView.OnScrollChangedListener onScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
		public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
			//making background of actionbar transparent with scroll
			final int imageHeight = 200;
			final int headerHeight = 200;
			final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
			int margintop = -(int)(ratio * 60);
			Resources r = ma.getResources();
			int px = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP,
					margintop,
					r.getDisplayMetrics());
			int margin = px + (int)ma.getResources().getDimension(R.dimen.dashboard_map_bottom_padding);
			if (headerHeight < t - margin){
				//hiding action bar - showing floating button
				//getSupportActionBar().hide();
				if (fabButton != null) {
					fabButton.showFloatingActionButton();
					floatingButtonVisible = true;
				}
			} else {
				//getSupportActionBar().show();
				if (fabButton != null) {
					fabButton.hideFloatingActionButton();
					floatingButtonVisible = false;
				}
			}
		}
	};

}
