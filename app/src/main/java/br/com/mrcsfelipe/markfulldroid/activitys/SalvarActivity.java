package br.com.mrcsfelipe.markfulldroid.activitys;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.mrcsfelipe.markfulldroid.R;
import br.com.mrcsfelipe.markfulldroid.model.Person;

public class SalvarActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etDtNascimento;
    private Person person;
    private static final String url = "http://10.0.2.2:8080/MarkFullOne/rest/persons";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salvar);

        etNome = (EditText) findViewById(R.id.et_nome);
        etDtNascimento = (EditText) findViewById(R.id.ed_dt_nascimento);
        person = new Person();
    }


    public void cadastrar(View view){
        try {
            //DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            String data = etDtNascimento.getText().toString();
            person.setIdentify(0);
            person.setNome(etNome.getText().toString());
            person.setDataNascimento(data);
            new HttpRequestTask().execute();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *
     * POST -- Conectando com o Servidor
     *
     * */

    private class HttpRequestTask extends AsyncTask<Void, Void, Person> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                dialog = new ProgressDialog(SalvarActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
            }else{
                dialog = new ProgressDialog(SalvarActivity.this);
            }
            dialog.setMessage(Html.fromHtml("<b>" + "Conectando com Servidor" + "</b>"));
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Person doInBackground(Void... params) {
            try {


                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Person greeting = restTemplate.postForObject(url,person ,Person.class);

                Log.i("POSTpreparo", person.toString());

                //ResponseEntity<Person> response = restTemplate.postForEntity(url, person, Person.class);
                /*
                int codigoResult = response.getStatusCode().value();

                if(codigoResult == 500){
                    return null;
                } else {
                    Log.d("BODY", response.getBody().toString());
                    return response.getBody();
                }*/
                return greeting;

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                dialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Person person) {

            if(person == null){
                dialog.dismiss();
            }else {
                Log.d("POST", person.toString());
                dialog.dismiss();
            }


        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
