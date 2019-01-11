package com.example.dell.mytour.uis.activity.register_activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.google_map.MapsActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;

import java.util.Calendar;

public class AddressAndBirthdayRegisterActivity extends BaseActivity implements View.OnClickListener{
    Button btn_next_phone, btn_delete_address;
    TextView txt_birthday;
    TextView edt_address;

    DatePickerDialog date_picker_dialog;
    int year, month, day_of_month;
    Calendar calendar;

    //
    private GoogleMap mMap;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected int injectLayout() {
        return R.layout.activity_register_address_and_birthday;
    }

    @Override
    protected void injectView() {
        btn_next_phone = findViewById(R.id.btn_next_phone);
        btn_delete_address = findViewById(R.id.btn_delete_address);

        edt_address = findViewById(R.id.edt_address);
        txt_birthday = findViewById(R.id.txt_birthday);

        // goi phuong thuc chon ngay thang nam sinh
        setDataForTxtBirthday();

        btn_next_phone.setOnClickListener(this);
        btn_delete_address.setOnClickListener(this);
        edt_address.setOnClickListener(this);


        edt_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    btn_delete_address.setVisibility(View.VISIBLE);
                } else {
                    btn_delete_address.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void injectVariables() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next_phone:
                String address = edt_address.getText().toString().trim();
                String birthday = txt_birthday.getText().toString().trim();

                if(TextUtils.isEmpty(address)){
                    edt_address.setError("Bạn hãy điền dịa chỉ của mình");
                    return;
                }
                gotoDescriptionScreen(address, birthday);
                break;
            case R.id.btn_delete_address:
                edt_address.setText("");
                break;
            case R.id.edt_address:
                gotoGooglePlaceAPI();
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    /*phuong thuc show place google API*/
    private void gotoGooglePlaceAPI(){
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(AddressAndBirthdayRegisterActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }
    }
    /*phuong thuc show place google API*/

    //----------------------------------------------------------------------------------------------
    /*ke thua phuong thuc result de nhan ve ket qua*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                edt_address.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    /*ke thua phuong thuc result de nhan ve ket qua*/


    // phuong thuc chon ngay sinh
    private void setDataForTxtBirthday(){
        /*code thao tac khi bam vao muc ngay sinh*/

        txt_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
                date_picker_dialog = new DatePickerDialog(AddressAndBirthdayRegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                txt_birthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, 1990, month, day_of_month);
                txt_birthday.setText(day_of_month+"/"+(month + 1)+"/"+1990);
                //date_picker_dialog.getDatePicker().setMinDate(); // set ngay chon khong duoc nho hon ngay hien tai
                date_picker_dialog.show();
            }
        });
        /*code thao tac khi bam vao muc ngay sinh*/
    }

    private void gotoDescriptionScreen(String address, String birthday){
        Intent intent = getIntent();
        String email = intent.getStringExtra("email_register");
        String password = intent.getStringExtra("password_register");
        String full_name = intent.getStringExtra("full_name_register");
        String phone = intent.getStringExtra("phone_register");


        Intent intent_next_phone = new Intent(AddressAndBirthdayRegisterActivity.this, DescriptionRegisterActivity.class);
        intent_next_phone.putExtra("email_register",email);
        intent_next_phone.putExtra("password_register",password);
        intent_next_phone.putExtra("full_name_register",full_name);
        intent_next_phone.putExtra("phone_register",phone);
        intent_next_phone.putExtra("address_register",address);
        intent_next_phone.putExtra("birthday_register",birthday);

        startActivity(intent_next_phone);
    }
}
