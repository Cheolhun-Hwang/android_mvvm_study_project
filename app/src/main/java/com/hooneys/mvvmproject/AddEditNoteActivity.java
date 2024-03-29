package com.hooneys.mvvmproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.hooneys.mvvmproject.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.hooneys.mvvmproject.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.hooneys.mvvmproject.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.hooneys.mvvmproject.EXTRA_PRIORITY";

    private EditText title, description;
    private NumberPicker priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        init();
    }

    private void init() {
        title = findViewById(R.id.edit_text_title);
        description = findViewById(R.id.edit_text_description);
        priority = findViewById(R.id.number_picker_priority);

        priority.setMinValue(1);
        priority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            title.setText(intent.getStringExtra(EXTRA_TITLE));
            description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            priority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        }else{
            setTitle("Add Note");
        }

    }

    private void saveNote() {
        String s_title = title.getText().toString();
        String s_desc = description.getText().toString();
        int i_pri = priority.getValue();

        if(s_title.trim().isEmpty() || s_desc.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Please insert a title and description",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, s_title);
        data.putExtra(EXTRA_DESCRIPTION, s_desc);
        data.putExtra(EXTRA_PRIORITY, i_pri);
        int id = getIntent().getIntExtra(EXTRA_ID, -1); //ID 0부터 이기 때문에
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
