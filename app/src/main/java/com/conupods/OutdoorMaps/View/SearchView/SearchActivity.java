package com.conupods.OutdoorMaps.View.SearchView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.conupods.OutdoorMaps.Services.ActivityComponentBuilder;
import com.conupods.OutdoorMaps.Models.Building.AbstractCampusLocation;
import com.conupods.OutdoorMaps.Services.CampusAbstractLocationCreationService;
import com.conupods.MapsActivity;
import com.conupods.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends FragmentActivity implements CampusLocationsAdapterListener {

    private final String TAG = "SEARCH_ACTIVITY";
    private AbstractCampusLocationAdapter mAdapter;
    private SearchView mSearchBar;
    private TextView mCurrentTextQueryField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        List<AbstractCampusLocation> mCampusLocationList = new ArrayList<>();
        mAdapter = new AbstractCampusLocationAdapter(mCampusLocationList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        initializeComponents();

        CampusAbstractLocationCreationService campusLocationCreationService = new CampusAbstractLocationCreationService(mCampusLocationList, mAdapter);
        campusLocationCreationService.prepareCampusLocationsForSearch();
    }

    private void initializeComponents() {
        ActivityComponentBuilder componentBuilder = new ActivityComponentBuilder();
        mSearchBar = componentBuilder.initializeSearchBarWithFocus(findViewById(R.id.searchBar), this, this, mAdapter);
        mCurrentTextQueryField = findViewById(R.id.current_search_text);
        LinearLayout searchDirectioButtons = (LinearLayout) findViewById(R.id.SearchDirectionsOptions);
        searchDirectioButtons.setFocusable(true);
        searchDirectioButtons.setFocusableInTouchMode(true);

    }

    @Override
    public void onCampusLocationSelected(AbstractCampusLocation abstractCampusLocation) {
        if(abstractCampusLocation.getmLongIdentifier() != null) {
            mSearchBar.setQuery(abstractCampusLocation.getmLongIdentifier(), false);
            mCurrentTextQueryField.setText(abstractCampusLocation.getmLongIdentifier());
        }
        else {
            mSearchBar.setQuery(abstractCampusLocation.getIdentifier(), false);
            mCurrentTextQueryField.setText(abstractCampusLocation.getIdentifier());

        }
        mSearchBar.clearFocus();


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SearchActivity.this, this.mSearchBar, ViewCompat.getTransitionName(this.mSearchBar));
        startActivity(intent, options.toBundle());
        finish();

    }


}

