package am.getchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_CODE = 1;
    private RelativeLayout root;
    private TextView grp_name;
    private ImageView settingsButton;
    private ListView listOfMessages;
    private EmojiconEditText emojiconEditText;
    private ImageView emojiButton;
    private ImageView sendButton;
    private EmojIconActions emojIconActions;
    private FirebaseListAdapter<Message> fbAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(root, "You are authorized", Snackbar.LENGTH_SHORT).show();
                displayAllMessages();
            } else {
                Snackbar.make(root, "You aren't authorized", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ad();

        init();
        checkUser();
        settings();
        sendBtn();
    }

    @SuppressLint("WrongViewCast")
    public void init() {
        root = findViewById(R.id.activity_main);
        grp_name = findViewById(R.id.grp_name);
        settingsButton = findViewById(R.id.btn_settings);
        listOfMessages = findViewById(R.id.messageList);
        sendButton = findViewById(R.id.btn_send);
        emojiButton = findViewById(R.id.emoji_btn);
        emojiconEditText = findViewById(R.id.text_layout);
        emojIconActions = new EmojIconActions(getApplicationContext(), root, emojiconEditText, emojiButton);
        emojIconActions.ShowEmojIcon();
    }

    public void settings() {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("am.getchat.SettingsActivity");
                startActivity(intent);
            }
        });
    }

    public void checkUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        } else {
            Snackbar.make(root, "You are authorized", Snackbar.LENGTH_SHORT).show();

            displayAllMessages();
        }
    }

    public void sendBtn() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiconEditText.getText().toString().equals("")) {
                    return;
                }
                FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(
                        new Message(
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                emojiconEditText.getText().toString()
                        )
                );
                emojiconEditText.setText("");
            }
        });
    }

    private void displayAllMessages() {
        fbAdapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView mess_user;
                TextView mess_time;
                BubbleTextView mess_text;

                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);

                mess_user.setText(model.getUserName());
                mess_time.setText(DateFormat.format("dd-MM-yyyy HH:mm:ss", model.getMessageTime()));
                mess_text.setText(model.getTextMessage());
            }
        };

        listOfMessages.setAdapter(fbAdapter);
    }
}
