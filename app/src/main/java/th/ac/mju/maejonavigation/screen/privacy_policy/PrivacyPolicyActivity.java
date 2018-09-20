package th.ac.mju.maejonavigation.screen.privacy_policy;

import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import th.ac.mju.maejonavigation.R;
import th.ac.mju.maejonavigation.app.MjnActivity;

public class PrivacyPolicyActivity extends MjnActivity {

    private static final String URL = "https://maejonavigation-6d39d.firebaseapp.com/";

    @InjectView(R.id.webview)
    WebView webView;
    @InjectView(R.id.privacy_policy_tool_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.inject(this);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.mjn_while));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.privacy_policy_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(
                ContextCompat.getDrawable(this, R.drawable.ic_close_white));
        setWebView();
    }

    private void setWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
