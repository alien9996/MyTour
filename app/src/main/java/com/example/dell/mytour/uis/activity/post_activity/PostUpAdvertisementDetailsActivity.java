package com.example.dell.mytour.uis.activity.post_activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.uis.BaseActivity;

public class PostUpAdvertisementDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView edt_time_service_post_up, edt_time_service_start_post_up,
            edt_transport_service_post_up, edt_price_old_post_up, edt_price_new_post_up,
            edt_title_advertisement_post_up;
    private Button btn_done;

    @Override
    protected int injectLayout() {
        return R.layout.activity_post_up_advenrtisement_details;
    }

    @Override
    protected void injectView() {

        // khoi tao text view
        edt_time_service_post_up = findViewById(R.id.edt_time_service_post_up);
        edt_time_service_start_post_up = findViewById(R.id.edt_time_service_start_post_up);
        edt_transport_service_post_up = findViewById(R.id.edt_transport_service_post_up);
        edt_price_old_post_up = findViewById(R.id.edt_price_old_post_up);
        edt_price_new_post_up = findViewById(R.id.edt_price_new_post_up);
        edt_title_advertisement_post_up = findViewById(R.id.edt_title_advertisement_post_up);

        // khoi tao button
        btn_done = findViewById(R.id.btn_done);

        btn_done.setOnClickListener(this);

    }

    @Override
    protected void injectVariables() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                getData();
                break;
        }
    }

    private void getData() {
        String time = edt_time_service_post_up.getText().toString().trim();
        String time_start = edt_time_service_start_post_up.getText().toString().trim();
        String tranport = edt_transport_service_post_up.getText().toString().trim();
        String price_old = edt_price_old_post_up.getText().toString().trim();
        String price_new = edt_price_new_post_up.getText().toString().trim();
        String title_advertisement = edt_title_advertisement_post_up.getText().toString().trim();

        if (TextUtils.isEmpty(time)) {
            edt_time_service_post_up.setError("Không được bỏ trống trường này");
            return;
        }
        if (TextUtils.isEmpty(time_start)) {
            edt_time_service_start_post_up.setError("Không được bỏ trống trường này");
            return;
        }
        if (TextUtils.isEmpty(tranport)) {
            edt_transport_service_post_up.setError("Không được bỏ trống trường này");
            return;
        }
        if (TextUtils.isEmpty(price_old)) {
            edt_price_old_post_up.setError("Không được bỏ trống trường này");
            return;
        }
        if (TextUtils.isEmpty(price_new)) {
            edt_price_new_post_up.setError("Không được bỏ trống trường này");
            return;
        }
        if (TextUtils.isEmpty(title_advertisement)) {
            edt_title_advertisement_post_up.setError("Không được bỏ trống trường này");
            return;
        }

        String[] list_str = new String[]{time, time_start, tranport, price_old, price_new, title_advertisement};
        Intent intent = new Intent();

        intent.putExtra("data_result", list_str);
        setResult(PostUpContentActivity.RESULT_CODE, intent);
        finish();

    }


}
