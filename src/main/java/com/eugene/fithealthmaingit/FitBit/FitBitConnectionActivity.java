package com.eugene.fithealthmaingit.FitBit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.UI.UserInformationFragment;
import com.eugene.fithealthmaingit.Utilities.Globals;
import com.temboo.Library.Fitbit.OAuth.FinalizeOAuth;
import com.temboo.Library.Fitbit.OAuth.InitializeOAuth;
import com.temboo.core.TembooSession;

@SuppressLint("JavascriptInterface")
public class FitBitConnectionActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private WebView webView;
    private ProgressBar circularProgressbar;
    public String secret;
    private String callbacks;
    private String oauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_bit);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        circularProgressbar = (ProgressBar) findViewById(R.id.circularProgressbar);

        webView = (WebView) findViewById(R.id.webview1);
        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Android");
        webView.getSettings().setSupportZoom(true);
        new GetOauthKey().execute();
        Toolbar toolbar_fitbit = (Toolbar) findViewById(R.id.toolbar_fitbit);
        toolbar_fitbit.setTitle("Connect Fitbit");
        toolbar_fitbit.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar_fitbit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadWebView() {
        try {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    Log.e("Denied", url);
                    if (url.startsWith("https://eugeneh.temboolive")) {
                        new Finialize().execute();
                    }
                    if (url.contains("deny=Deny")) {
                        savePreferences("FITBIT_CONNECTION_STATUS", "NOT_CONNECTED");
                        Intent data = new Intent();
                        data.putExtra(UserInformationFragment.ACTIVITY_ONE_RESULT, "DENY");
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    //hide loading image
                    circularProgressbar.setVisibility(View.GONE);
                    //show webview
                    webView.setVisibility(View.VISIBLE);
                }
            });
            webView.loadUrl(oauth);
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }


    public class Finialize extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                TembooSession session = new TembooSession("eugeneh", "FitHealth", "Gj74tL9HVPoTM84UvJaJjtMaMslhVWE7");
                FinalizeOAuth finalizeOAuthChoreo = new FinalizeOAuth(session);
                FinalizeOAuth.FinalizeOAuthInputSet finalizeOAuthInputs = finalizeOAuthChoreo.newInputSet();
                finalizeOAuthInputs.set_CallbackID(callbacks);
                finalizeOAuthInputs.set_OAuthTokenSecret(secret);
                finalizeOAuthInputs.set_ConsumerSecret(Globals.CUSTOMER_SECRET);
                finalizeOAuthInputs.set_ConsumerKey(Globals.CUSTOMER_KEY);
                FinalizeOAuth.FinalizeOAuthResultSet finalizeOAuthResults = finalizeOAuthChoreo.execute(finalizeOAuthInputs);
                Log.e("token", finalizeOAuthResults.get_AccessToken());
                Log.e("secret", finalizeOAuthResults.get_AccessTokenSecret());

                savePreferences("FITBIT_ACCESS_TOKEN", finalizeOAuthResults.get_AccessToken());
                savePreferences("FITBIT_ACCESS_TOKEN_SECRET", finalizeOAuthResults.get_AccessTokenSecret());
                savePreferences("FITBIT_ACCESS_TOKEN_ID", finalizeOAuthResults.get_UserID());

            } catch (Exception e) {
                Log.e("Test", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            savePreferences("FITBIT_CONNECTION_STATUS", "CONNECTED");
            Intent data = new Intent();
            data.putExtra(UserInformationFragment.ACTIVITY_ONE_RESULT, "CONNECTED");
            setResult(RESULT_OK, data); // passing the RESULT_OK parameter
            finish();
        }
    }

    public class GetOauthKey extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                TembooSession session = new TembooSession("eugeneh", "FitHealth", "Gj74tL9HVPoTM84UvJaJjtMaMslhVWE7");
                InitializeOAuth initializeOAuthChoreo = new InitializeOAuth(session);
                InitializeOAuth.InitializeOAuthInputSet initializeOAuthInputs = initializeOAuthChoreo.newInputSet();
                initializeOAuthInputs.set_ConsumerSecret(Globals.CUSTOMER_SECRET);
                initializeOAuthInputs.set_ConsumerKey(Globals.CUSTOMER_KEY);
                InitializeOAuth.InitializeOAuthResultSet initializeOAuthResults = initializeOAuthChoreo.execute(initializeOAuthInputs);
                secret = initializeOAuthResults.get_OAuthTokenSecret();
                callbacks = initializeOAuthResults.get_CallbackID();
                oauth = initializeOAuthResults.get_AuthorizationURL();
                initializeOAuthResults.getCompletionStatus();

                Log.e("secret", secret);
                Log.e("callbacks", callbacks);
                Log.e("oauth", oauth);
                Log.e("completition", initializeOAuthResults.getCompletionStatus().toString());
            } catch (Exception e) {
                Log.e("Test", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            loadWebView();
        }
    }

    private void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }
}
