package com.eligosoftware.notifon;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by mragl on 12.11.2016.
 */

public class EditTrunitsActivity extends SingleFragmentActivity implements EditTrunitsFragment.Callbacks {

    public boolean scrolldown;

    @Override
    protected Fragment createFragment() {
        return new EditTrunitsFragment();
    }

    @Override
    public void onEditTrunit(Trunit selectedValue, int operation_code, boolean use_list_value, boolean list_value) {
        Intent i=new Intent(this,TrunitEditActivity.class);
        i.putExtra(TrunitEditActivity.TRUNIT_ID,selectedValue);
        i.putExtra(TrunitEditActivity.OPERATION_CODE,operation_code);
        i.putExtra(TrunitEditActivity.USE_LIST_VALUE,use_list_value);
        i.putExtra(TrunitEditActivity.LIST_VALUE,list_value);
        startActivityForResult(i,1);
    }

    @Override
    public void OnCopyTrunit(Trunit selectedValue, int operation_code) {
        Intent i=new Intent(this,TrunitEditActivity.class);
        i.putExtra(TrunitEditActivity.TRUNIT_ID,selectedValue);
        i.putExtra(TrunitEditActivity.OPERATION_CODE,operation_code);
        startActivityForResult(i,2);
    }

    @Override
    public void onAddTrunit(Trunit selectedValue, int operation_code) {
        Intent i=new Intent(this,TrunitEditActivity.class);
        i.putExtra(TrunitEditActivity.TRUNIT_ID,selectedValue);
        i.putExtra(TrunitEditActivity.OPERATION_CODE,operation_code);
        startActivityForResult(i,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK){
            scrolldown=true;
        }
    }
}
