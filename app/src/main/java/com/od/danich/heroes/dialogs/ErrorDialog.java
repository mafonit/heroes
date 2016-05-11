package com.od.danich.heroes.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.od.danich.heroes.R;


public class ErrorDialog extends DialogFragment implements View.OnClickListener {

    String message;
    TextView tvMessage;
    boolean status;
    private static final String TAG = "WES/ErrorDialog";
    String key;

    public  static ErrorDialog newInstance(String message) {
        ErrorDialog f = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        f.setArguments(args);
        return f;
    }
    public  static ErrorDialog newInstance(String message,String key) {
        ErrorDialog f = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("key",key);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(key=="crash"){
            System.exit(10);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_error, null);
        v.findViewById(R.id.btnOk).setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvMessage=(TextView)v.findViewById(R.id.tvError);
        message = getArguments().getString("message");
        key=getArguments().getString("key");
        tvMessage.setMovementMethod(new ScrollingMovementMethod());
        tvMessage.setText(message);



        return v;
    }


    @Override
    public void onClick(View v) {

        dismiss();

    }


}
