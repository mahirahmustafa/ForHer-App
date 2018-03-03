package com.forher.forher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

public class EventCardExpand extends AppCompatActivity {
    public String KEY="KEY";
    WebView webViewBody;
    String title=null,startDate=null,body=null,finalLink=null;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);
        setContentView(R.layout.activity_event_card_expand);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        finalLink="#";
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         Intent intent = getIntent();
            if (null != intent) {
                int numberData = intent.getIntExtra(KEY, 0);
                String fromActivity=intent.getStringExtra("FROM");
                System.out.println("Getting to webview from Activity "+fromActivity);
                numberData=numberData+1;
                if(fromActivity!=null)
                {
                    if(fromActivity.equalsIgnoreCase("SLIDER"))
                    {
                        finalLink="http://52.11.116.39/blog/posts/readmode/b";
                        finalLink=finalLink+numberData;
                    }
                    else if(fromActivity.equalsIgnoreCase("EVENTS"))
                    {
                        finalLink="http://52.11.116.39/blog/posts/readmode/b";
                        finalLink=finalLink+numberData;
                    }
                    else if(fromActivity.equalsIgnoreCase("TEAM"))
                    {
                        finalLink=intent.getStringExtra("LINK");
                    }
                    else if(fromActivity.equalsIgnoreCase("PROFILE"))
                    {
                        finalLink="http://52.11.116.39/blog/posts/readmode/b";
                    }
                    else if(fromActivity.equalsIgnoreCase("FAB")){
                        finalLink="https://52.11.116.39/donate";
                    }
                    else
                    {
                        finalLink="http://52.11.116.39/blog/posts/readmode/b";
                    }
                }
                else{
                    finalLink="http://52.11.116.39/blog/posts/readmode/b";
                }

                System.out.println("position is " + numberData);
                webViewBody=(WebView)findViewById(R.id.event_body_webview);
                //http://52.11.116.39/blog/posts/readmode/b1

                try{
                    final Activity activity = this;
                    webViewBody.setWebChromeClient(new WebChromeClient() {
                        public void onProgressChanged(WebView view, int progress) {
                            // Activities and WebViews measure progress with different scales.
                            // The progress meter will automatically disappear when we reach 100%
                            progressBar.setProgress(progress);
                            if(progress==100)
                            {
                                 setProgressBarIndeterminateVisibility(false);
                                 setProgressBarVisibility(false);
                                progressBar.setProgress(100);
                                progressBar.setVisibility(View.GONE);
                            }
                            //activity.setProgress(progress * 1000);
                        }
                    });
                    webViewBody.setWebViewClient(new WebViewClient() {
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                            Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                        }
                    });

                    webViewBody.loadUrl(finalLink );
                }catch (Exception e)
                {
                    Toast.makeText(this, "Unable to connect " , Toast.LENGTH_SHORT).show();
                }




            }
        else
            {
                Toast.makeText(getBaseContext(), "Unable to fetch data... Please try again later.", Toast.LENGTH_LONG).show();
            }

    }








}
