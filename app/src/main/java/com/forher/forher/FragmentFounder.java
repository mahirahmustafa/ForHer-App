package com.forher.forher;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFounder extends Fragment {
    ProgressBar progressBar;
     WebView webViewBody;
    String finalLink=null;
    public FragmentFounder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getActivity().requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //getActivity().requestWindowFeature(Window.FEATURE_PROGRESS);
        //getActivity().setProgressBarIndeterminateVisibility(true);
        //getActivity().setProgressBarVisibility(true);
        View v=inflater.inflate(R.layout.fragment_fragment_founder, container, false);
        //progressBar=(ProgressBar)v.findViewById(R.id.progressbar);
        //progressBar.setProgress(0);
        //progressBar.setVisibility(View.VISIBLE);

        finalLink="http://52.11.116.39/founders";
        webViewBody=(WebView)v.findViewById(R.id.founder_webview);
        webViewBody.loadUrl(finalLink );


        return v;
    }


}
