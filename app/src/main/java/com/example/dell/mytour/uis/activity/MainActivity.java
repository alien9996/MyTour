package com.example.dell.mytour.uis.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.NewPageAdapter;
import com.example.dell.mytour.model.model_base.Friend;
import com.example.dell.mytour.model.model_base.Message;
import com.example.dell.mytour.service.MessagingService;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.result.ResultActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, SearchView.OnQueryTextListener {


    //private float get_x1, get_x2;
    private int[] lst_menu_id = new int[]{R.id.home_nemu, R.id.post_menu, R.id.add_post_menu, R.id.action_menu, R.id.library_menu};
    public AHBottomNavigation bottomNavigation;
    ViewPager view_pager;

    Toolbar toolbar;

    // các phuong thuc lam viec voi firebase
    private FirebaseDatabase firebase_database;
    private DatabaseReference database_reference;
    private FirebaseUser firebase_user;

    // cac bien int, String dung trong activity
    private int total_post = 0;

    @Override
    protected int injectLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void injectView() {

        // khoi tao view
        view_pager = findViewById(R.id.frm_layout_main);

        // custom navigationView de khong day cac icon khac sang 1 ben
        bottomNavigation = findViewById(R.id.bottom_navigationAH);

        // khoi tao bottom navigation bar
        toolbar = findViewById(R.id.tool_bar_home);
        toolbar.setTitle("Trang chủ");

        //khoi tao fire base
        firebase_database = FirebaseDatabase.getInstance();
        firebase_user = FirebaseAuth.getInstance().getCurrentUser();

        // khoi tao botton navigation
        NewPageAdapter adapter = new NewPageAdapter(getSupportFragmentManager());
        view_pager.setAdapter(adapter);

        loadViewPage(0);
        setSupportActionBar(toolbar);

        view_pager.setOnPageChangeListener(this);
        setBottomNavigationView();

        // goi cac phuong thuc
        if(firebase_user != null){
            startMessegingService();
            friendValueListener();
            messageValuesListener();
        }

    }

    @Override
    protected void injectVariables() {
        // thiet lap cac gia tri tham so va cac gia tri mac ding

    }


    //----------------------------------------------------------------------------------------------
    // init BottomNavigationViewAH
    public void setBottomNavigationView() {
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Trang chủ", R.drawable.ic_home_black_24dp, R.color.black);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Bài đăng", R.drawable.ic_local_post_office_black_24dp, R.color.black);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Hỗ trợ", R.drawable.ic_contacts_black_24dp, R.color.black);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Thông báo", R.drawable.ic_access_alarm_black_24dp, R.color.black);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Tài khoản", R.drawable.ic_menu_black_24dp, R.color.black);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        // Set background color

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#ffffff"));

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);


        //// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#3F51B5"));
        bottomNavigation.setInactiveColor(Color.parseColor("#808080"));

        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        // Display color under navigation bar (API 21+)
        // Don't forget these lines in your style-v21

        bottomNavigation.setTranslucentNavigationEnabled(true);

        // Manage titles
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(false);

        // Set current item programmatically
        bottomNavigation.setCurrentItem(0);

        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        /*// Add or remove notification for each item
        bottomNavigation.setNotification("3", 3);*/


        // Enable / disable item & set disable color
        bottomNavigation.enableItemAtPosition(2);
        //        bottomNavigation.disableItemAtPosition(2);
        // bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                switch (position) {
                    case 0:
                        toolbar.setTitle("Trang chủ");
                        toolbar.setNavigationIcon(R.drawable.ic_home_black_24dp);
                        loadViewPage(0);
                        bottomNavigation.setNotification("", 0);
                        return true;
                    case 1:
                        toolbar.setTitle("Bài đăng");
                        toolbar.setNavigationIcon(R.drawable.ic_local_post_office_black_24dp);
                        loadViewPage(1);
                        bottomNavigation.setNotification("", 1);
                        return true;
                    case 2:
                        toolbar.setTitle("Hỗ trợ");
                        toolbar.setNavigationIcon(R.drawable.ic_contacts_black_24dp);
                        loadViewPage(2);
                        bottomNavigation.setNotification("", 2);
                        return true;
                    case 3:
                        toolbar.setTitle("Thông báo");
                        toolbar.setNavigationIcon(R.drawable.ic_access_alarm_black_24dp);
                        loadViewPage(3);
                        bottomNavigation.setNotification("", 3);
                        return true;
                    case 4:
                        toolbar.setTitle("Tài khoản");
                        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
                        loadViewPage(4);
                        bottomNavigation.setNotification("", 4);
                        return true;
                }
                return false;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
                switch (y) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                }
            }
        });
    }


    public void loadViewPage(int position) {
        view_pager.setCurrentItem(position);
    }

    //------------------------------------------------------------------------------------------------
    // phuong thuc khoi tao cho viewpages
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigation.setCurrentItem(position);


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    // phuong thuc khoi tao cho viewpages

    //------------------------------------------------------------------------------------------------
    /*//phuong thuc khoi tao toolBar*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem menu_item = menu.findItem(R.id.menu_bt_search);
        SearchView search_view = (SearchView) menu_item.getActionView();
        search_view.setOnQueryTextListener(MainActivity.this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bt_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent_search = new Intent(MainActivity.this, ResultActivity.class);
        intent_search.putExtra("intent_type", "2");
        intent_search.putExtra("data", query);
        startActivity(intent_search);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //showToast(newText);
        return true;
    }

    /*//phuong thuc khoi tao toolBar*/

    //--------------------------------------------------------------------------------------------------
    /*phuong thuc lang nghe su kien trong object friend tren firebase*/
    private void friendValueListener() {

        database_reference = firebase_database.getReference("Friend");

        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Friend friend = snapshot.getValue(Friend.class);
                    if (firebase_user.getUid().equals(friend.getFriend_link()) && !friend.isFriend_status()) {
                        i++;
                    }
                }
                if (i != 0) {
                    bottomNavigation.setNotification(String.valueOf(i), 4);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /*phuong thuc lang nghe su kien trong object friend tren firebase*/


    //--------------------------------------------------------------------------------------------------
    /*phuong thuc lang nghe su thay doi tren firebase Message*/
    private void messageValuesListener() {

        database_reference = firebase_database.getReference("Message");

        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (firebase_user.getUid().equals(message.getFriend_id()) && !message.isMessage_status()) {
                        i++;
                    }
                }
                if (i != 0) {
                    bottomNavigation.setNotification(String.valueOf(i), 3);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /*phuong thuc lang nghe su thay doi tren firebase Message*/


    //--------------------------------------------------------------------------------------------------
    //cac phuong thuc goi service
    private void startMessegingService() {
        Intent intent_messaging = new Intent(MainActivity.this, MessagingService.class);
        this.startService(intent_messaging);
    }


}
