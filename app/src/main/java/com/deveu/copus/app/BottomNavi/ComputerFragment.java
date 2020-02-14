package com.deveu.copus.app.BottomNavi;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deveu.copus.app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComputerFragment extends Fragment {


    EditText subject_edit, message_edit,to;
Button send;
    public ComputerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_computer, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        subject_edit = getView().findViewById(R.id.subject_edit);
        message_edit = getView().findViewById(R.id.message_edit);
        send = getView().findViewById(R.id.send);





        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(subject_edit.getText().toString())){
                    Toast.makeText(getActivity(), "Please fill required areas!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(message_edit.getText().toString())){
                    Toast.makeText(getActivity(), "Please fill required areas!", Toast.LENGTH_SHORT).show();

                }else{
                    sendMail();
                }


            }
        });

    }

    private void sendMail() {
    String to = "programmingbookscoder@gmail.com";
     //   String recipientList = to.getText().toString();
        String[] recipients = to.split(",");
        String subject = subject_edit.getText().toString();
        String message = message_edit.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

}
