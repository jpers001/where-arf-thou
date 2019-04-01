package edu.odu.cs.air411.wherearfthou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private EditText Name,UserName,UserPassword;
    private Button BnRegister;



    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        Name= view.findViewById(R.id.txt_name);
        UserName=view.findViewById(R.id.User_Name);
        UserPassword=view.findViewById(R.id.Password);
        BnRegister = view.findViewById(R.id.register_bn);

        ((View) BnRegister).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                performRegistration();
            }
        });

        return view;
    }

    public void performRegistration()
    {
        String name= Name.getText().toString();
        String username= UserName.getText().toString();
        String password= UserPassword.getText().toString();
        Call<User> call= LoginActivity.apiInterface.performRegistration(name,username,password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.body().getRegistration().equals("successful"))
                {
                    LoginActivity.prefConfig.Display("Registration Success....");
                }
                else if(response.body().getRegistration().equals("user exist"))
                {
                    LoginActivity.prefConfig.Display("User already exist....");
                }
                else if (response.body().getRegistration().equals("error"))
                {
                    LoginActivity.prefConfig.Display("Something went Wrong....");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        Name.setText("");
        UserPassword.setText("");
        UserName.setText("");

    }



}