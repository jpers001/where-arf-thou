package edu.odu.cs.air411.wherearfthou;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.app.*;

public class RegisterActivity extends AppCompatActivity
{

EditText Name,User_Name,Password,conPassword;
Button reg_button;
AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Name=(EditText)findViewById(R.id.reg_name);
        User_Name=(EditText)findViewById(R.id.reg_username);
        Password=(EditText)findViewById(R.id.reg_password);
        conPassword=(EditText)findViewById(R.id.reg_con_password);
        reg_button=(Button)findViewById(R.id.register_button);
        reg_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Name.getText().toString().equals("")|| User_Name.getText().toString().equals("")||Password.getText().toString().equals(""))
                {
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Something went wrong...");
                    builder.setMessage("Please fill the fields..");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();
                }

                else if (!Password.getText().toString().equals(conPassword.getText().toString()))
                {
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Something went wrong...");
                    builder.setMessage("Your password does not match..");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            Password.setText("");
                            conPassword.setText("");
                        }
                    });
                    AlertDialog alertDialog= builder.create();
                    alertDialog.show();
                }

            }
        });
    }
}
