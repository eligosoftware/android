package com.eligosoftware.notifon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by mragl on 12.11.2016.
 */

public class TrunitEditActivity extends SingleFragmentActivity {

    public static final String TRUNIT_ID="trunit_id";
    public static final String OPERATION_CODE="operation_code";
    public static final String USE_LIST_VALUE="use_list_value";
    public static final String LIST_VALUE="list_value";

    @Override
    protected Fragment createFragment() {
        return new TrunitEditFragment();
    }


}
