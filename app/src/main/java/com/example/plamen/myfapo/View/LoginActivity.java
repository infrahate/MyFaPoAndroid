package com.example.plamen.myfapo.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.plamen.myfapo.Data.DataLinks;
import com.example.plamen.myfapo.Logic.LoginController;
import com.example.plamen.myfapo.MySingleton;
import com.example.plamen.myfapo.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements LoginInterface {

    LoginButton facebookLogin;
    TwitterLoginButton twLogin;
    CallbackManager cbManager;
    BufferedReader in = null;
    String data = null;
    TwitterSession session;

    @Override
    public void onLoginSuccess(String respone) {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login_hash",respone);
        Log.i("a",respone);
        editor.commit();
        startActivity(i);
        finish();
    }

    TwitterAuthToken authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("LdN33qgJkrMKRdhqZFU0sIb1N", "Yx1KgWwt3dp4Sw3dTcoiovR9jCMxVFAZRKwP09SElZLMqC0iVz"))
                .debug(true)
                .build();
        Twitter.initialize(config);

        setContentView(R.layout.activity_login);

        final LoginController loginController = new LoginController(new DataLinks(),this);

        twLogin = (TwitterLoginButton) findViewById(R.id.login_buttonTwitter);
        twLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                session = result.data;
                authToken = session.getAuthToken();
                final String token = authToken.token;
                final String secret = authToken.secret;

                String url = loginController.getTWloginLink();


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject json = new JSONObject(response);
                                    loginController.onLoginSuccess(json.getString("login_hash"));
                                } catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Response",error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("oauth_token",token);
                        params.put("oauth_token_secret",secret);
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.i("Something","Go boom");
            }
        });


        LoginManager.getInstance().logOut();
        facebookLogin = (LoginButton) findViewById(R.id.login_button);
        cbManager = CallbackManager.Factory.create();
        facebookLogin.registerCallback(cbManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Log.i("Status","yey : "+loginResult.getAccessToken().getToken()+" : "+loginResult.getAccessToken().getUserId());
                String url = loginController.getFBloginLink();
                final String token = loginResult.getAccessToken().getToken();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Log.i("Response",response);
                                try {
                                    JSONObject json = new JSONObject(response);
                                    //Log.i("Response",json.getString("login_hash"));
                                    loginController.onLoginSuccess(json.getString("login_hash"));
                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Response",error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("access_token",token);
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
            }

            @Override
            public void onCancel() {
                Log.i("Status","Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("Status","Error");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cbManager.onActivityResult(requestCode, resultCode, data);
        twLogin.onActivityResult(requestCode, resultCode, data);
    }


}
