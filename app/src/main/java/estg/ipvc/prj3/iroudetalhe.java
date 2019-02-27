package estg.ipvc.prj3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class iroudetalhe extends AppCompatActivity {
    private TextView nomeDestino;
    private ImageButton info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iroudetalhe);

        setUpUIViews();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String json = bundle.getString("movieModel"); // getting the model from MainActivity send via extras
            MovieModel movieModel = new Gson().fromJson(json, MovieModel.class);
            nomeDestino.setText(movieModel.getDESIGNACAO());

            info.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Bundle bundle2 = getIntent().getExtras();
                    String json2 = bundle2.getString("movieModel");
                    MovieModel movieModel2 = new Gson().fromJson(json2, MovieModel.class);
                    Intent intent = new Intent(iroudetalhe.this, DetailActivity.class);
                    intent.putExtra("movieModel", new Gson().toJson(movieModel2)); // converting model json into string type and sending it via intent
                    startActivity(intent);
                }
            });


        }
    }

    private void setUpUIViews() {
        nomeDestino = (TextView) findViewById(R.id.nomedestino);
        info = (ImageButton)findViewById(R.id.inforlocal);

    }
}
