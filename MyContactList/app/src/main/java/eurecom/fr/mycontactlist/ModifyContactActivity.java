package eurecom.fr.mycontactlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class ModifyContactActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    private Contact contact;
    private EditText name;
    private EditText email;
    private EditText number;
    private EditText url;
    private Button modify;
    private  Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent= getIntent();
        contact = (Contact) intent.getSerializableExtra("contact");
        fillForm();
        modify = (Button) findViewById(R.id.modify);
        delete = (Button) findViewById(R.id.delete);
        modify.setOnClickListener(this);
        delete.setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void fillForm(){
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        number = (EditText) findViewById(R.id.number);
        url = (EditText) findViewById(R.id.url);

        name.setText(contact.getName());
        email.setText(contact.getEmail());
        number.setText(contact.getPhone());
        url.setText("None");

        name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        email.setImeOptions(EditorInfo.IME_ACTION_DONE);
        number.setImeOptions(EditorInfo.IME_ACTION_DONE);
        url.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    @Override
    public void onClick(View v){
        Handler handler= new Handler();
        ResponseHandler rh = null;
        if (v.getId()==R.id.modify){
            Log.i("main", "I clicked modify");
            Map<String,String> data = new HashMap<String,String>();
            data.put("name", name.getText().toString());
            data.put("email",email.getText().toString());
            data.put("phone", number.getText().toString());
            if (contact.getId().isEmpty()) {
                rh = new ResponseHandler(this, "save", "POST", handler, this, data);
            } else {
                rh = new ResponseHandler(this, "save?id=" + contact.getId(), "POST", handler, this, data);
            }
            new ModifyTask().execute(rh);

        }
        else{
            if (contact.getId().isEmpty()) {
                popUp("Canceled!");
                this.finish();
                return;
            } else {
                rh = new ResponseHandler(this,"delete?id=" + contact.getId(), "DELETE", handler, this, null);
            }
            new ModifyTask().execute(rh);
            Log.i("main", "I clicked delete");
        }
    }

    private void popUp(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
