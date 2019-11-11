package my.testapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class Settings_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        (((EditText) findViewById(R.id.edittext_period))).setText(getIntent().getIntExtra("period", 10) + "");
        (((EditText) findViewById(R.id.age_from))).setText(getIntent().getIntExtra("age_from", 22) + "");
        (((EditText) findViewById(R.id.age_to))).setText(getIntent().getIntExtra("age_to", 35) + "");
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        String s=((EditText)findViewById(R.id.edittext_period)).getText().toString();
        data.putExtra("period",s);

        s=((EditText)findViewById(R.id.age_from)).getText().toString();
        data.putExtra("age_from",s);

        s=((EditText)findViewById(R.id.age_to)).getText().toString();
        data.putExtra("age_to",s);

        setResult(RESULT_OK, data);
        finish();

    }
}
