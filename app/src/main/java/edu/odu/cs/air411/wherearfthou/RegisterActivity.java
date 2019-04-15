package edu.odu.cs.air411.wherearfthou;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.app.*;
import helper.BackgroundTask;

public class RegisterActivity extends AppCompatActivity
{

EditText Name,User_Name,Password,conPassword;
Button reg_button;
AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        {
            android.support.v7.app.ActionBar actionBar = getSupportActionBar(); // or getActionBar();
            String title = actionBar.getTitle().toString(); // get the title
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_wat_icon);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

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
                else
                {
                    BackgroundTask backgroundTask= new BackgroundTask(RegisterActivity.this);
                    backgroundTask.execute("Regrister",Name.getText().toString(),User_Name.getText().toString(),Password.getText().toString());
                }
            }
        });

        Button skipBtn = findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(goToMain);
            }
        });
    }
}
