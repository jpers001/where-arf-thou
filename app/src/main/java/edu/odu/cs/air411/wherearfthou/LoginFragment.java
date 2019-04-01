package edu.odu.cs.air411.wherearfthou;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment
{

    private TextView RegText;
    public EditText User_Name,Password;
    private Button LoginBn;
    OnLoginActivityListener LoginActivityListener;
    public interface OnLoginActivityListener
    {
        void performRegister();
        void performLogin(String name);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        RegText= view.findViewById(R.id.register_txt);
        User_Name= view.findViewById(R.id.txt_user_name);
        Password= view.findViewById(R.id.txt_user_name);
        LoginBn= view.findViewById(R.id.login_bn);

        LoginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        RegText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivityListener.performRegister();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        Activity activity= (Activity) context;
        LoginActivityListener = (OnLoginActivityListener) activity;

    }

    private void performLogin()
    {
        String username= User_Name.getText().toString();
        String password=Password.getText().toString();

        Call<User> call= LoginActivity.apiInterface.performLogin(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getLogin().equals("successful"))
                {

                    LoginActivity.prefConfig.writeLoginStatus(true);
                    LoginActivityListener.performLogin(response.body().getWelcome());
                }
                else if (response.body().getLogin().equals("failed"))
                {
                    LoginActivity.prefConfig.Display("Login Failed..Please try again...");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        User_Name.setText("");
        Password.setText("");
    }




}
