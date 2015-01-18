package com.elijahgill.todotutorial;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.elijahgill.todotutorial.com.elijahgill.todotutorial.contentprovider.MyTodoContentProvider;
import com.elijahgill.todotutorial.database.TodoTable;


public class TodoDetailActivity extends FragmentActivity
        implements DatePickerDialog.OnDateSetListener {

    private Spinner mPriority;
    private EditText mTitleText;
    private EditText mBodyText;
    private EditText mDueDate;
    private CheckBox mIsDone;
    private TextView mDateCreated;

    private Uri todoUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.todo_edit);

        mPriority = (Spinner) findViewById(R.id.priority);
        mTitleText = (EditText) findViewById(R.id.todo_edit_summary);
        mBodyText = (EditText) findViewById(R.id.todo_edit_description);

        mDueDate = (EditText) findViewById(R.id.due_date);
        mIsDone = (CheckBox) findViewById(R.id.is_done);
        mDateCreated = (TextView) findViewById(R.id.date_created);

        ConfirmOnActionListener actionListener = new ConfirmOnActionListener();

        mBodyText.setOnEditorActionListener(actionListener);

        Button confirmButton = (Button) findViewById(R.id.todo_edit_button);

        Bundle extras = getIntent().getExtras();

        // check from saved instance
        todoUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);

        if (extras != null) {
            todoUri = extras
                    .getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);
            fillData(todoUri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
            }
        });

        mDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }

    public void saveEntry(){
        if (TextUtils.isEmpty(mTitleText.getText().toString())) {
            makeToast( getResources().getString(R.string.warning_summary) );
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    public void selectDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Called when the date picker is closed
        // Set the DueDate field to the date

        mDueDate.setText(String.format("%1$s-%2$s-%3$s", year, month+1, day));
    }

    private void fillData(Uri uri) {
        String[] projection = {TodoTable.COLUMN_SUMMARY,
                TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_CATEGORY,
                TodoTable.COLUMN_DUE_DATE, TodoTable.COLUMN_IS_DONE, TodoTable.COLUMN_DATE_CREATED};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String category = cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_CATEGORY));

            for (int i = 0; i < mPriority.getCount(); i++) {
                String s = (String) mPriority.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    mPriority.setSelection(i);
                }
            }

            mTitleText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_SUMMARY)));
            mBodyText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)));
            mIsDone.setChecked( cursor.getInt(cursor
                .getColumnIndexOrThrow(TodoTable.COLUMN_IS_DONE)) !=0 );
            mDueDate.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(TodoTable.COLUMN_DUE_DATE)));
            mDateCreated.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(TodoTable.COLUMN_DATE_CREATED)));

            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String category = (String) mPriority.getSelectedItem();
        String summary = mTitleText.getText().toString();
        String description = mBodyText.getText().toString();
        // New fields
        String dueDate = mDueDate.getText().toString();
        int isDone = (mIsDone.isChecked())?1:0;

        if (description.length() == 0 && summary.length() == 0) {
            return; // nothing to see here...
        }

        ContentValues values = new ContentValues();
        values.put(TodoTable.COLUMN_CATEGORY, category);
        values.put(TodoTable.COLUMN_SUMMARY, summary);
        values.put(TodoTable.COLUMN_DESCRIPTION, description);
        values.put(TodoTable.COLUMN_DUE_DATE, dueDate);
        values.put(TodoTable.COLUMN_IS_DONE, isDone);

        if (todoUri == null) {
            //new to do
            todoUri = getContentResolver().insert(MyTodoContentProvider.CONTENT_URI, values);
        } else {
            getContentResolver().update(todoUri, values, null, null);
        }
    }

    private void makeToast(String str) {
        Toast.makeText(TodoDetailActivity.this, str,
                Toast.LENGTH_LONG).show();
    }

    public class ConfirmOnActionListener implements TextView.OnEditorActionListener {
        // called when an action happens.
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean retVal = false;

            switch( actionId ){
                case EditorInfo.IME_ACTION_DONE:
                    saveEntry();
                    retVal = true; // event handled
                    break;
            }
            return retVal;
        }
    }
}
