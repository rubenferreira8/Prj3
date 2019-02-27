package estg.ipvc.prj3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class tipogrupo extends AppCompatActivity {

    ImageButton cadeirarodas;
    ImageButton invisual;
    ImageButton surdo;
    ImageButton gravidascriancas;
    ImageButton idosos;
    ImageButton autistas;
    int valor;
    SharedPreferences a;
    Boolean firsttime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipogrupo);
        a = getSharedPreferences("myPrefs",MODE_PRIVATE);
        firsttime = a.getBoolean("firsttime",true);

        if(firsttime){
            SharedPreferences.Editor editor2 = a.edit();
            firsttime = false;
            editor2.putBoolean("firsttime", firsttime);
            editor2.apply();
        }
        else{
            Intent intent = new Intent(tipogrupo.this, mapa.class);
            startActivity(intent);
        }


        int valor1 = getValor();
        Context context = getApplicationContext();
        CharSequence text = "o valor é:" + valor1;
        int duration = Toast.LENGTH_SHORT;
        cadeirarodas = (ImageButton) findViewById(R.id.cadeira_rodas);
        invisual = (ImageButton) findViewById(R.id.cegos);
        surdo = (ImageButton) findViewById(R.id.surdos);
        gravidascriancas = (ImageButton) findViewById(R.id.gravidas_criancas);
        idosos = (ImageButton) findViewById(R.id.idoso);
        //autistas = (ImageButton) findViewById(R.id.autistas);



        cadeirarodas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                valor = 1;
                Context context = getApplicationContext();
                CharSequence text = "o valor é:" + valor;
                int duration = Toast.LENGTH_SHORT;

                guardaTipo(valor);

                Intent intent = new Intent(tipogrupo.this, mapa.class);
                startActivity(intent);
                           }
        });



        invisual.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                valor = 2;
                Context context = getApplicationContext();
                CharSequence text = "o valor é:" + valor;
                int duration = Toast.LENGTH_SHORT;

                //Toast toast = Toast.makeText(context, text, duration);
                //toast.show();
                guardaTipo(valor);

                Intent intent = new Intent(tipogrupo.this, mapa.class);
                startActivity(intent);
            }
        });
        /*autistas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                valor = 3;
                Context context = getApplicationContext();
                CharSequence text = "o valor é:" + valor;
                int duration = Toast.LENGTH_SHORT;
                guardaTipo(valor);

                Intent intent = new Intent(tipogrupo.this, mapa.class);
                startActivity(intent);
            }
        });*/



        surdo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                valor = 4;

                Context context = getApplicationContext();
                CharSequence text = "o valor é:" + valor;
                int duration = Toast.LENGTH_SHORT;
                guardaTipo(valor);
                Intent intent = new Intent(tipogrupo.this, mapa.class);
                startActivity(intent);
            }
        });



        gravidascriancas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                valor = 5;

                Context context = getApplicationContext();
                CharSequence text = "o valor é:" + valor;
                int duration = Toast.LENGTH_SHORT;

                //Toast toast = Toast.makeText(context, text, duration);
                //toast.show();
                guardaTipo(valor);


                Intent intent = new Intent(tipogrupo.this, mapa.class);
                startActivity(intent);
            }
        });


        idosos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                valor = 6;

                Context context = getApplicationContext();
                CharSequence text = "o valor é:" + valor;
                int duration = Toast.LENGTH_SHORT;

                //Toast toast = Toast.makeText(context, text, duration);
                //toast.show();
                guardaTipo(valor);

                Intent intent = new Intent(tipogrupo.this, mapa.class);
                startActivity(intent);
            }
        });


    }

    private void guardaTipo( int valor){
        SharedPreferences mSharedPreferences = getSharedPreferences("escolha", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("valor", valor);
        mEditor.apply();
    }
    private int getValor(){
        SharedPreferences mSharedPreferences = getSharedPreferences("escolha", MODE_PRIVATE);
        int selectValor = mSharedPreferences.getInt("valor", valor);
        return selectValor;


    }
}