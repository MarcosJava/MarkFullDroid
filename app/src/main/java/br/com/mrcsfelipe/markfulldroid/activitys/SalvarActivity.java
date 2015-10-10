package br.com.mrcsfelipe.markfulldroid.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import br.com.mrcsfelipe.markfulldroid.R;
import br.com.mrcsfelipe.markfulldroid.model.Person;

public class SalvarActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etDtNascimento;
    private Person person;
    public static final String url = "http://10.0.2.2:8080/MarkFullOne/rest/persons";

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
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String data = etDtNascimento.getText().toString();
            person.setIdentify(0);
            person.setNome(etNome.getText().toString());
            person.setDataNascimento(new Date());
            Log.i("Post", "enviando para o servidor");
            new HttpRequestTask().execute(MediaType.APPLICATION_JSON);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *
     * POST -- Conectando com o Servidor
     *
     * */
    // doInBackground , onPreExecute, onPostExecute
    private class HttpRequestTask extends AsyncTask<MediaType, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                dialog = new ProgressDialog(SalvarActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
            } else {
                dialog = new ProgressDialog(SalvarActivity.this);
            }
            dialog.setMessage(Html.fromHtml("<b>" + "Conectando com Servidor" + "</b>"));
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(MediaType... params) {
            try {

                MediaType mediaType = params[0];

                HttpHeaders requestHeaders = new HttpHeaders();

                // Sending a JSON or XML object i.e. "application/json" or "application/xml"
                requestHeaders.setContentType(mediaType);

                // Populate the Message object to serialize and headers in an
                // HttpEntity object to use for the request
                HttpEntity<Person> requestEntity = new HttpEntity<Person>(person, requestHeaders);

                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                if (mediaType == MediaType.APPLICATION_JSON) {
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                } else if (mediaType == MediaType.APPLICATION_XML) {
                    restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
                }


                ResponseEntity<String> response =
                        restTemplate.postForEntity(SalvarActivity.url, requestEntity, String.class);

                Log.i("POST", response.getBody().toString());

                if(response.getStatusCode().value() == 400){
                    return "";
                } else if (response.getStatusCode().value() == 500){
                    return "erroServer";
                } else if (response.getStatusCode().value() == 404) {
                    return "serverOff";
                }

                // Return the response body to display to the user
                return response.getBody();



            } catch (Exception e) {

                dialog.dismiss();
                return "erroServer";
            }
        }

        @Override
        protected void onPostExecute(String person) {

            if(person == ""){
                dialog.dismiss();
                Toast.makeText(SalvarActivity.this, "Dados invalidos", Toast.LENGTH_LONG).show();
            }else if(person == "erroServer"){
                dialog.dismiss();
                naoEnviadoSalvaSQL();
                Toast.makeText(SalvarActivity.this, "Servidor dando erro", Toast.LENGTH_LONG).show();
            }else if(person == "serverOff"){
                dialog.dismiss();
                naoEnviadoSalvaSQL();
                Toast.makeText(SalvarActivity.this, "Servidor Off", Toast.LENGTH_LONG).show();
            }else {
                Log.d("POST", person.toString());
                dialog.dismiss();
                Toast.makeText(SalvarActivity.this, "Dados enviado com Sucesso", Toast.LENGTH_LONG).show();
            }
        }

        public void naoEnviadoSalvaSQL(){
            person.setIdentify(null);
            person.setFoiEnviado("false");
            Log.i("NAO SALVO", person.toString());
           // person.save();

            Person p = new Person(null,person.getNome(), person.getDataNascimento(), person.getFoiEnviado());
            p.save();
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
