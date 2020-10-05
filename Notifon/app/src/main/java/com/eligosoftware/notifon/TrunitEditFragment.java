package com.eligosoftware.notifon;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.eligosoftware.notifon.database.TranslatesBase;

/**
 * Created by mragl on 12.11.2016.
 */

public class TrunitEditFragment extends Fragment {
    String[] textArray = { "easy", "intermediate", "hard" };
    Integer[] imageArray = { R.mipmap.easy_icon, R.mipmap.medium_icon,
            R.mipmap.hard_icon};
    private Spinner mSpinner;
    private Trunit mTrunit;
    private int op_code;

    private EditText word_edit,definition_et,partsp_et;
    private CheckBox knowit;
    private Button save_btn;

    private TranslatesBase mTranslatesBase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mTranslatesBase= TranslatesBase.get(getActivity());
        mTrunit=(Trunit)getActivity().getIntent().getSerializableExtra(TrunitEditActivity.TRUNIT_ID);
        op_code=getActivity().getIntent().getIntExtra(TrunitEditActivity.OPERATION_CODE,1);
        boolean use_l_value=getActivity().getIntent().getBooleanExtra(TrunitEditActivity.USE_LIST_VALUE,false);
        boolean l_value=getActivity().getIntent().getBooleanExtra(TrunitEditActivity.LIST_VALUE,false);

        View v=inflater.inflate(R.layout.edit_trunit,container,false);
        DifficultySelectAdapter adapter=new DifficultySelectAdapter(getActivity(),R.layout.spinner_value_layout,textArray,imageArray);
        mSpinner=(Spinner)v.findViewById(R.id.diff_select_spinner);
        mSpinner.setAdapter(adapter);

        word_edit=(EditText)v.findViewById(R.id.word_edit);
        definition_et=(EditText)v.findViewById(R.id.definition_et);
        partsp_et=(EditText)v.findViewById(R.id.partsp_et);
        knowit=(CheckBox)v.findViewById(R.id.knowit);

        if (op_code==1)
        {
            word_edit.setEnabled(false);
        }

        if (mTrunit.getLevel()!=null) {
            word_edit.setText(mTrunit.getWord());
            definition_et.setText(mTrunit.getDescr());
            partsp_et.setText(mTrunit.getPartsp());

            if (use_l_value)
                knowit.setChecked(l_value);
            else
                knowit.setChecked(mTrunit.getKnow() == 1);

            int position = 1;
            switch (mTrunit.getLevel()) {
                case "easy":
                    position = 0;
                    break;
                case "hard":
                    position = 2;
                    break;
                default:
                    break;
            }

            mSpinner.setSelection(position);
        }else{
        mSpinner.setSelection(1);}
            save_btn=(Button)v.findViewById(R.id.edit_trunit_btn_save);
            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (op_code==1)//edit
                    {
                        if (word_edit.getText().toString().isEmpty() || definition_et.getText().toString().isEmpty() ||
                                partsp_et.getText().toString().isEmpty())
                        {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(getString(R.string.warning_title_enter_word_definition))
                                    .setMessage(getString(R.string.warning_contents_enter_word_definition))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                        }
                        else{
                        mTrunit.setWord(word_edit.getText().toString());
                        mTrunit.setDescr(definition_et.getText().toString());
                        mTrunit.setPartsp(partsp_et.getText().toString());
                        mTrunit.setKnow(knowit.isChecked() ? 1 : 0);
                        mTrunit.setLevel(textArray[mSpinner.getSelectedItemPosition()]);



                            mTranslatesBase.updateTrunit(mTrunit);
                        Toast.makeText(getActivity(), getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();
                        getActivity().setResult(Activity.RESULT_CANCELED);
                        getActivity().finish();
                    }

                    }
                    else if(op_code==2 || op_code==3){ //copy or add
                        if (word_edit.getText().toString().isEmpty() || definition_et.getText().toString().isEmpty() ||
                                partsp_et.getText().toString().isEmpty())
                        {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle(getString(R.string.warning_title_enter_word_definition))
                                    .setMessage(getString(R.string.warning_contents_enter_word_definition))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                        }
                        else {
                            mTrunit.setWord(word_edit.getText().toString());
                            mTrunit.setDescr(definition_et.getText().toString());
                            mTrunit.setPartsp(partsp_et.getText().toString());
                            mTrunit.setKnow(knowit.isChecked() ? 1 : 0);
                            mTrunit.setLevel(textArray[mSpinner.getSelectedItemPosition()]);
                            if (op_code==3){
                                mTrunit.setDescr_lang_id(1);
                                mTrunit.setLang_id(2);
                            }



                            mTranslatesBase.addTrunit(mTrunit);
                            Toast.makeText(getActivity(), getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        }
                    }

                }
            });

        return v;
    }


}
