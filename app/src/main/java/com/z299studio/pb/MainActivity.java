/*
* Copyright 2014 Qianqian Zhu <zhuqianqian.299@gmail.com> All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.z299studio.pb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks{

    private static String[] sCategoryNames;
    private static int[] sCategoryIcons;
    private static int[] sCategoryIds;
    public static String[] getSortedCategoryNames() {
        if(sCategoryNames == null) {
            int size;
            ArrayList<AccountManager.Category> categories = AccountManager.getInstance()
                    .getCategoryList(false, true);
            size = categories.size() + 1;
            sCategoryNames = new String[size];
            sCategoryIds = new int[size];
            sCategoryIcons = new int[size];
            int i = 0;
            AccountManager.Category defaultCategory = AccountManager.getInstance()
                    .getCategory(AccountManager.DEFAULT_CATEGORY_ID);
            sCategoryNames[i] = defaultCategory.mName;
            sCategoryIds[i] = defaultCategory.mId;
            sCategoryIcons[i++] = defaultCategory.mImgCode;

            for(AccountManager.Category category : categories) {
                sCategoryNames[i] = category.mName;
                sCategoryIds[i] = category.mId;
                sCategoryIcons[i++] = category.mImgCode;
            }
        }
        return sCategoryNames;
    }

    public static int[] getSortedCategoryIds() {
        if(sCategoryIds == null) {
            getSortedCategoryNames();
        }
        return sCategoryIds;
    }

    public static int[] getSortedCatregoryIcons() {
        if(sCategoryIcons == null) {
            getSortedCategoryNames();
        }
        return sCategoryIcons;
    }

    private Application mApp;
    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawer;
    private DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState!=null && AccountManager.getInstance() == null) {
            super.onCreate(savedInstanceState);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        mApp = Application.getInstance(this);
        this.setTheme(C.THEMES[Application.Options.mTheme]);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        setupToolbar();
        mNavigationDrawer = (NavigationDrawerFragment)getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationDrawer.setUp(R.id.navigation_drawer, mDrawerLayout);
    }

    private void setupToolbar() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        float elevation = getResources().getDimension(R.dimen.toolbar_elevation) + 0.5f;
        ViewCompat.setElevation(mToolbar, elevation);
    }

    @Override
    public void onNavigationDrawerItemSelected(int type, int id) {

    }
}
