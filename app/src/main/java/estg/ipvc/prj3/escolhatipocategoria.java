package estg.ipvc.prj3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.analytics.ecommerce.Product;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class escolhatipocategoria extends AppCompatActivity {

    private final String URL_TO_HIT = "https://geo.cm-viana-castelo.pt/arcgis/rest/services/Viana_acessivel/MapServer/0/query?where=1%3D1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=CATEGORIA&returnGeometry=false&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=true&resultOffset=&resultRecordCount=&queryByDistance=&returnExtentsOnly=false&datumTransformation=&parameterValues=&rangeValues=&f=pjson";
    private ListView lvBTN;
    private ListView lvBTN2;
    private TextView btn;
    //SearchView search;
    EditText search;

    private ProgressDialog dialog;



    View myView;
    Typeface type;



    // Git error fix - http://stackoverflow.com/questions/16614410/android-studio-checkout-github-error-createprocess-2-windows

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_escolhatipocategoria);





        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        //search = (SearchView)findViewById(R.id.search);
        //search.setQueryHint("Pesquisa...");


        lvBTN = (ListView)findViewById(R.id.lvbtn);



        btn = (TextView) findViewById(R.id.btnn);






        // To start fetching the data when app start, uncomment below line to start the async task.
        new JSONTask().execute(URL_TO_HIT);
    }


    public class JSONTask extends AsyncTask<String,String, List<BtnModel> > {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<BtnModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);


                JSONArray parentArray = parentObject.getJSONArray("features");

                final List<BtnModel> btnModelList = new ArrayList<>();


                Gson gson = new Gson();

                for(int i=0; i<parentArray.length(); i++) {

                    JSONObject finalObject = parentArray.getJSONObject(i);

                    JSONObject attributes = finalObject.getJSONObject("attributes");

                    String lugares = attributes.getString("CATEGORIA");

                    BtnModel btnModel = gson.fromJson(attributes.toString(), BtnModel.class); // a single line json parsing using Gson
//
                    btnModelList.add(btnModel);

                }

                return btnModelList;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }

        @Override
        protected void onPostExecute(final List<BtnModel> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(result != null) {
               final BtnAdapter adapter = new BtnAdapter(getApplicationContext(), R.layout.row1, result);
                lvBTN.setAdapter(adapter);

                lvBTN.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // list item click opens a new detailed activity


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        BtnModel btnModel = result.get(position);
                        Intent intent1 = new Intent(escolhatipocategoria.this, MainActivity.class);
                        intent1.putExtra("valorcat", new Gson().toJson(btnModel));
                        startActivity(intent1);
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "Sem ligação à Internet, não foi possível carregar os dados.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public class BtnAdapter extends ArrayAdapter {

        private List<BtnModel> btnModelList;


        private int resource;
        private LayoutInflater inflater;
        public BtnAdapter(Context context, int resource, List<BtnModel> objects) {
            super(context, resource, objects);
            btnModelList = objects;

            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           // final ViewHolder holder = null
            ViewHolder holder = null;
            holder = new ViewHolder();
            if(convertView == null){

                convertView = inflater.inflate(resource, null);
                holder.btnn = (TextView)convertView.findViewById(R.id.btnn);
                holder.pricipal2 = (RelativeLayout)convertView.findViewById(R.id.principal2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.btnn.setText("  " + btnModelList.get(position).getCATEGORIA() + "  ");
            StringBuffer stringBuffer = new StringBuffer();


            return convertView;
        }


        class ViewHolder{

            private TextView btnn;
            private RelativeLayout pricipal2;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new JSONTask().execute(URL_TO_HIT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}