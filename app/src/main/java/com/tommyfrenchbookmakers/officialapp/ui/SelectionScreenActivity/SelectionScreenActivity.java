package com.tommyfrenchbookmakers.officialapp.ui.SelectionScreenActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.View;

import com.android.tighearnan.frenchsscanner.R;
import com.tommyfrenchbookmakers.officialapp.ui.BaseActivity;

public class SelectionScreenActivity extends BaseActivity {

    public void launchActivity(Class c) {
        start(c);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        DrawerLayout drawerLayout = getDrawerLayout();

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                SelectionScreenActivity.this,
                drawerLayout,
                getActionBarToolbar(),
                R.string.nav_drawer_opened,
                R.string.nav_drawer_closed
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_HOME;
    }

    @Override
    protected void onResume() {
        super.onResume();
        View view = findViewById(R.id.main_content);
        if(view != null) {
            view.setAlpha(1f);
        }
    }
}
