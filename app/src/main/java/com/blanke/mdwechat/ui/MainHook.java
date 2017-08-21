package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blanke.mdwechat.R;
import com.blanke.mdwechat.WeChatHelper;
import com.blanke.mdwechat.util.ConvertUtils;
import com.blanke.mdwechat.widget.MaterialSearchView;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.MD_CONTEXT;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.HomeUI_ViewPagerChangeListener;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.LauncherUI;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.LauncherUIBottomTabView;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.PopWindowAdapter_Bean_C;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.PopWindowAdapter_Bean_D;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.Search_FTSMainUI;
import static com.blanke.mdwechat.WeChatHelper.WCField.HomeUi_PopWindowAdapter;
import static com.blanke.mdwechat.WeChatHelper.WCField.HomeUi_PopWindowAdapter_SparseArray;
import static com.blanke.mdwechat.WeChatHelper.WCField.LauncherUI_mHomeUi;
import static com.blanke.mdwechat.WeChatHelper.WCId.ActionBar_id;
import static com.blanke.mdwechat.WeChatHelper.WCId.SearchUI_EditText_id;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.HomeUi_StartSearch;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.LauncherUiTabView_setContactTabUnread;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.LauncherUiTabView_setFriendTabUnread;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.LauncherUiTabView_setMainTabUnread;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.WxViewPager_onPageScrolled;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.WxViewPager_onPageSelected;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.WxViewPager_setCurrentItem;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

/**
 * Created by blanke on 2017/8/1.
 */

public class MainHook extends BaseHookUi {
    private static boolean isMainInit = false;//主界面初始化 hook 完毕
    private static AdapterView.OnItemClickListener hookPopWindowAdapter;
    private MaterialSearchView searchView;
    private Object mHomeUi;
    private String searchKey;
    private FloatingActionMenu actionMenu;

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        hookWechatMain();
//        XposedHelpers.findAndHookConstructor("android.support.v7.widget.ActionBarContainer",
//                lpparam.classLoader, Context.class, AttributeSet.class, new XC_MethodHook() {
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//                    }
//                });
//        XposedHelpers.findAndHookMethod("android.support.v7.widget.ActionBarContainer",
//                lpparam.classLoader, "l", Drawable.class, new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        Drawable drawable = (Drawable) param.args[0];
//                        log("ActionBarContainer setBackground=" + drawable);
//                        if (drawable instanceof ColorDrawable) {
//                            ColorDrawable colorDrawable = (ColorDrawable) drawable;
//                            log("colorDrawable=" + colorDrawable.getColor());
//                        }
//                    }
//                });
//        findAndHookMethod(Resources.class, "getColor", int.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                int id = (int) param.args[0];
//                int color = (int) param.getResult();
//                Resources resources = (Resources) param.thisObject;
//                log("id=" + id + ",color=" + color);
//                int primaryColor = getPrimaryColor();
//                param.setResult(0x55FF0000);
//                if (getColorId("z") == id) {
//                    log("hook z id=" + id + " success");
//                    param.setResult(primaryColor);
//                }
//                if (id == 2131689497) {
//                    log("hook id=2131689497 success");
//                    param.setResult(primaryColor);
//                }
//            }
//        });
//        findAndHookMethod("com.tencent.mm.bf.a", lpparam.classLoader,
//                "b", Resources.class, int.class, new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        Drawable drawable = (Drawable) param.getResult();
//                        log("com.tencent.mm.bf.a # b()");
//                        if (drawable instanceof ColorDrawable) {
//                            log("com.tencent.mm.bf.a # b() ColorDrawable");
//                            ColorDrawable colorDrawable = (ColorDrawable) drawable;
//                            colorDrawable.setColor(0x5500FF00);
//                            param.setResult(colorDrawable);
//                        }
//                    }
//                });
//        findAndHookMethod("com.tencent.mm.bf.a", lpparam.classLoader,
//                "d", Drawable.class, int.class, new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        Drawable drawable = (Drawable) param.getResult();
//                        log("com.tencent.mm.bf.a # d()");
//                        if (drawable instanceof ColorDrawable) {
//                            log("com.tencent.mm.bf.a # d() ColorDrawable");
//                            ColorDrawable colorDrawable = (ColorDrawable) drawable;
//                            colorDrawable.setColor(0x550000FF);
//                            param.setResult(colorDrawable);
//                        }
//                    }
//                });
//        findAndHookMethod(Color.class, "parseColor", String.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                String colorStr = (String) param.args[0];
//                int color = (int) param.getResult();
//                log("color=" + colorStr + "," + color);
//            }
//        });
    }

    private void hookWechatMain() {
        findAndHookMethod(LauncherUI, WeChatHelper.WCMethods.startMainUI, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                log("LauncherUI startMainUI isMainInit=" + isMainInit);
                if (isMainInit) {
                    return;
                }
                final Activity activity = (Activity) param.thisObject;
//                printActivityWindowViewTree(activity);

                View viewPager = activity.findViewById(
                        getId(activity, WeChatHelper.WCId.LauncherUI_ViewPager_Id));
                ViewGroup linearLayoutContent = (ViewGroup) viewPager.getParent();

                //移除底部 tabview
                View tabView = linearLayoutContent.getChildAt(1);
                if (tabView != null && tabView instanceof RelativeLayout) {
                    linearLayoutContent.removeView(tabView);
                }
                /******************
                 *hook floatButton
                 *****************/
                //添加 floatbutton
                ViewGroup contentFrameLayout = (ViewGroup) linearLayoutContent.getParent();
                addFloatButton(contentFrameLayout);

                // hook floatbutton click
                mHomeUi = getObjectField(activity, LauncherUI_mHomeUi);
//                log("mHomeUI=" + mHomeUi);
                if (mHomeUi != null) {
                    Object popWindowAdapter = XposedHelpers.getObjectField(mHomeUi, HomeUi_PopWindowAdapter);
//                    log("popWindowAdapter=" + popWindowAdapter);
                    if (popWindowAdapter != null) {
                        hookPopWindowAdapter = (AdapterView.OnItemClickListener) popWindowAdapter;
                        SparseArray hookArrays = new SparseArray();
                        int[] mapping = {10, 20, 2, 1};
//                        log("mapping:" + Arrays.toString(mapping));
                        Constructor dConstructor = PopWindowAdapter_Bean_D.getConstructor(int.class, String.class, String.class, int.class, int.class);
                        Constructor cConstructor = PopWindowAdapter_Bean_C.getConstructor(PopWindowAdapter_Bean_D);
                        for (int i = 0; i < mapping.length; i++) {
                            Object d = dConstructor.newInstance(mapping[i], "", "", mapping[i], mapping[i]);
                            Object c = cConstructor.newInstance(d);
                            hookArrays.put(i, c);
                        }
//                        log("hookArrays=" + hookArrays);
                        XposedHelpers.setObjectField(popWindowAdapter, HomeUi_PopWindowAdapter_SparseArray, hookArrays);
                    }
                }
                /******************
                 *hook SearchView
                 *****************/
//                ViewGroup actionFrameLayout = (ViewGroup) contentFrameLayout.getParent();
//                ViewGroup rootFrameLayout = (ViewGroup) actionFrameLayout.getParent();
//                logSuperClass(rootFrameLayout.getClass());
                ViewGroup actionLayout = (ViewGroup) activity.findViewById(getId(activity, ActionBar_id));
                addSearchView(actionLayout);
                // hook click search
                findAndHookMethod(LauncherUI, "onOptionsItemSelected", MenuItem.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        MenuItem menuItem = (MenuItem) param.args[0];
                        if (menuItem.getItemId() == 1) {//search
//                            log("hook launcherUi click action search icon success");
                            if (searchView != null) {
                                searchView.show();
                            }
                            param.setResult(true);
                        }
                    }
                });
                /******************
                 *hook tabLayout
                 *****************/
                addTabLayout(linearLayoutContent, viewPager);

                isMainInit = true;
            }
        });
        findAndHookMethod(LauncherUI,
                "onDestroy", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        log("LauncherUI onDestroy()");
                        isMainInit = false;
                    }
                });
        //hide more icon in actionBar
        findAndHookMethod(LauncherUI, "onCreateOptionsMenu", Menu.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                log("homeUI=" + mHomeUi);
                if (mHomeUi != null) {
                    //hide actionbar more add icon
                    MenuItem moreMenuItem = (MenuItem) getObjectField(mHomeUi, "uxE");
//                    log("moreMenuItem=" + moreMenuItem);
                    moreMenuItem.setVisible(false);
                }
            }
        });
        //hook onKeyEvent
        findAndHookMethod(LauncherUI, "dispatchKeyEvent", KeyEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                KeyEvent keyEvent = (KeyEvent) param.args[0];
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MENU) {//hide menu
                    param.setResult(true);
                    return;
                }
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {//hook back
                    if (searchView != null && searchView.isSearchViewVisible()) {
                        searchView.hide();
                        param.setResult(true);
                        return;
                    }
                    if (actionMenu != null && actionMenu.isOpened()) {
                        actionMenu.close(true);
                        param.setResult(true);
                        return;
                    }
                }
            }
        });


    }

    private void addFloatButton(ViewGroup frameLayout) {
        int primaryColor = getPrimaryColor();
        Context context = MD_CONTEXT;
        actionMenu = new FloatingActionMenu(context);
        actionMenu.setMenuButtonColorNormal(primaryColor);
        actionMenu.setMenuButtonColorPressed(primaryColor);
        actionMenu.setMenuIcon(ContextCompat.getDrawable(context, R.drawable.ic_add));
        actionMenu.initMenuButton();

        actionMenu.setFloatButtonClickListener(new FloatingActionMenu.FloatButtonClickListener() {
            @Override
            public void onClick(FloatingActionButton fab, int index) {
                log("click fab,index=" + index + ",label" + fab.getLabelText());
                if (hookPopWindowAdapter != null) {
                    hookPopWindowAdapter.onItemClick(null, null, index, 0);
                }
            }
        });

        addFloatButton(actionMenu, context, "扫一扫", R.drawable.ic_scan, primaryColor);
        addFloatButton(actionMenu, context, "收付款", R.drawable.ic_money, primaryColor);
        addFloatButton(actionMenu, context, "群聊", R.drawable.ic_chat, primaryColor);
        addFloatButton(actionMenu, context, "添加好友", R.drawable.ic_friend_add, primaryColor);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = ConvertUtils.dp2px(frameLayout.getContext(), 12);
        params.rightMargin = margin;
        params.bottomMargin = margin;
        params.gravity = Gravity.END | Gravity.BOTTOM;
        frameLayout.addView(actionMenu, params);
    }

    private FloatingActionButton addFloatButton(FloatingActionMenu actionMenu, Context context,
                                                String label, int drawable, int primaryColor) {
        FloatingActionButton fab = new FloatingActionButton(context);
        fab.setImageDrawable(ContextCompat.getDrawable(context, drawable));
        fab.setColorNormal(primaryColor);
        fab.setColorPressed(primaryColor);
        fab.setButtonSize(SIZE_MINI);
        fab.setLabelText(label);
        actionMenu.addMenuButton(fab);
        fab.setLabelColors(primaryColor, primaryColor, primaryColor);
        return fab;
    }

    private void addSearchView(ViewGroup frameLayout) {
        Context context = MD_CONTEXT;
        searchView = new MaterialSearchView(context);
        searchView.setOnSearchListener(new MaterialSearchView.SimpleonSearchListener() {
            @Override
            public void onSearch(String query) {
                searchKey = query;
                if (mHomeUi != null) {
                    XposedHelpers.callMethod(mHomeUi, HomeUi_StartSearch);
                }
                searchView.hide();
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(searchView, params);
        findAndHookMethod(Search_FTSMainUI, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!TextUtils.isEmpty(searchKey)) {
                    final Activity activity = (Activity) param.thisObject;
                    log("searchkey=" + searchKey);
                    final Handler handler = new Handler(activity.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int id = getId(activity, SearchUI_EditText_id);
                            View editView = activity.getWindow().findViewById(id);
//                            log("id=" + id);
                            if (editView == null) {
//                                printActivityWindowViewTree(activity);
                                handler.postDelayed(this, 200);
                                return;
                            }
//                            log("editview=" + editView);
                            if (editView instanceof EditText) {
                                EditText editText = (EditText) editView;
                                editText.setHintTextColor(Color.WHITE);
//                                log("editText=" + editText);
                                editText.setText(searchKey);
                            }
                            searchKey = null;
                        }
                    }, 200);
                }
            }
        });
    }

    private void addTabLayout(ViewGroup viewPagerLinearLayout, final View pager) {
        Context context = MD_CONTEXT;
        final CommonTabLayout tabLayout = new CommonTabLayout(context);
        int primaryColor = getPrimaryColor();
        tabLayout.setBackgroundColor(primaryColor);
        tabLayout.setTextSelectColor(Color.WHITE);
        tabLayout.setTextUnselectColor(0x1acccccc);
        int dp2 = ConvertUtils.dp2px(pager.getContext(), 1F);
        tabLayout.setIndicatorHeight(dp2);
        tabLayout.setIndicatorColor(Color.WHITE);
        tabLayout.setIndicatorCornerRadius(dp2);
        tabLayout.setIndicatorAnimDuration(200);
        tabLayout.setElevation(5);
        tabLayout.setTextsize(context.getResources().getDimension(R.dimen.tabTextSize));
        tabLayout.setUnreadBackground(Color.WHITE);
        tabLayout.setUnreadTextColor(primaryColor);
        tabLayout.setSelectIconColor(Color.WHITE);
        tabLayout.setUnSelectIconColor(0x1acccccc);

//        String[] titles = {"消息", "通讯录", "朋友圈", "设置"};
        final int[] tabIcons = {R.drawable.ic_chat_tab, R.drawable.ic_contact_tab,
                R.drawable.ic_explore_tab, R.drawable.ic_person_tab};
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        for (int i = 0; i < tabIcons.length; i++) {
            final int finalI = i;
            mTabEntities.add(new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return null;
                }

                @Override
                public int getTabSelectedIcon() {
                    return tabIcons[finalI];
                }

                @Override
                public int getTabUnselectedIcon() {
                    return 0;
                }
            });
        }
        tabLayout.setTabData(mTabEntities);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
//                log("tab click position=" + position);
                callMethod(pager, WxViewPager_setCurrentItem, position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        findAndHookMethod(HomeUI_ViewPagerChangeListener, WxViewPager_onPageScrolled,
                int.class, float.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        float positionOffset = (float) param.args[1];
                        int position = (int) param.args[0];
                        tabLayout.setStartScrollPosition(position);
                        tabLayout.setIndicatorOffset(positionOffset);
                    }
                });
        findAndHookMethod(HomeUI_ViewPagerChangeListener, WxViewPager_onPageSelected,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        int position = (int) param.args[0];
                        tabLayout.setCurrentTab(position);
                    }
                });
        findAndHookMethod(LauncherUIBottomTabView, LauncherUiTabView_setMainTabUnread,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        log("msg unread=" + param.args[0]);
                        tabLayout.showMsg(0, (Integer) param.args[0]);
                    }
                });
        findAndHookMethod(LauncherUIBottomTabView, LauncherUiTabView_setContactTabUnread,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        log("contact unread=" + param.args[0]);
                        tabLayout.showMsg(1, (Integer) param.args[0]);
                    }
                });
        findAndHookMethod(LauncherUIBottomTabView, LauncherUiTabView_setFriendTabUnread,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        log("friend unread=" + param.args[0]);
                        tabLayout.showMsg(2, (Integer) param.args[0]);
                    }
                });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = ConvertUtils.dp2px(viewPagerLinearLayout.getContext(), 48);
        viewPagerLinearLayout.addView(tabLayout, 0, params);
    }

    private int getPrimaryColor() {
        refreshPrefs();
        return WeChatHelper.colorPrimary;
    }
}