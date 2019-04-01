package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import retrofit2.Call;
import android.support.v4.app.Fragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginActivityListener
{
    public static PreferenceConfiguration prefConfig;
    public static ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        prefConfig= new PreferenceConfiguration(this);
        apiInterface=ApiClient.getApiClient().create(ApiInterface.class);

        if (findViewById(R.id.fragment_container)!=null)
        {
            if (savedInstanceState!=null)
            {
                return;
            }
            if(prefConfig.readLoginStatus())
            {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);

            }
            else
            {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new LoginFragment()).commit();
            }
        }
    }

    @Override
    public void performRegister()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RegistrationFragment()).addToBackStack(null).commit();
    }

    @Override
    public void performLogin(String Name)
    {
        prefConfig.writeName(Name);

        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
}

