package ru.spbau.mit.swys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

abstract public class BaseActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Utils.prepareTempDir()) {
            Toast.makeText(this, getString(R.string.prepare_temp_dir_error), Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
