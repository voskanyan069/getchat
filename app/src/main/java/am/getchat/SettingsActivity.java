package am.getchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

//    SETTINGS_ACTIVITY
    private TextView themes;
    private TextView appInfo;
    private TextView grpInfo;
    private TextView creators;
    private TextView feedback;
    private TextView logOut;

//    APP_INFO_ACTIVITY
    private WebView web_page;

//    GRP_INFO_ACTIVITY
    private TextView grp_name;
    private TextView grp_email;
    private TextView grp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        appInfo();
        grpInfo();
        creators();
        feedback();
        logOut();
    }

    public void init() {
        themes = findViewById(R.id.setting_themes);
        appInfo = findViewById(R.id.setting_app_info);
        grpInfo = findViewById(R.id.setting_grp_info);
        creators = findViewById(R.id.setting_creators);
        feedback = findViewById(R.id.setting_fb);
        logOut = findViewById(R.id.setting_log_out);
    }

    public void appInfo() {
        appInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_app_info);

                web_page = findViewById(R.id.web_page);

                WebSettings ws = web_page.getSettings();
                ws.setJavaScriptEnabled(true);
                web_page.loadUrl("https://get-chat-pad.000webhostapp.com/");
                web_page.setWebViewClient(new WebViewClient());
            }
        });
    }

    public void onBackPressed() {
        if (web_page.canGoBack()) {
            web_page.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void grpInfo() {
        grpInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_grp_info);

                grp_name = findViewById(R.id.setting_grp_info_name);
                grp_email = findViewById(R.id.setting_grp_info_mail);
                grp_id = findViewById(R.id.setting_grp_info_id);

                grp_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                grp_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                grp_id.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });
    }

    public void creators() {
        creators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_creators);
            }
        });
    }

    public void feedback() {
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_fb);
            }
        });
    }

    public void logOut() {
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });
    }
}
