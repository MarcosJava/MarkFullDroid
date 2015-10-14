package br.com.mrcsfelipe.markfulldroid.activitys;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.mrcsfelipe.markfulldroid.R;
import br.com.mrcsfelipe.markfulldroid.model.Person;
import br.com.mrcsfelipe.markfulldroid.model.Persons;

public class MainActivity extends ListActivity {

    private List<Map<String, Object>> personsMAP = new ArrayList<>();
    private Persons persons;
    private SimpleAdapter adapter;
    static final String url = "http://10.0.2.2:8080/MarkFullOne/rest/persons";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //new HttpRequestTask().execute();
        popularAdpter();
        adapter.areAllItemsEnabled();

    }

    public void popularAdpter() {
        String[] de = {"identify", "nome", "nascimento"};
        int[] para = {R.id.tv_identify, R.id.tv_nome, R.id.tv_dt_nascimento};

        if(persons == null)
            persons = new Persons();


        if(persons.getPersons().isEmpty()) {
            adapter = new SimpleAdapter(this, getPersons(), R.layout.activity_main, de, para);
        } else {
            adapter = new SimpleAdapter(this, null, R.layout.activity_main, de, para);
        }
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void salvar(){
        startActivity(new Intent(this, SalvarActivity.class));
    }



    public List<Map<String, Object>> getPersons(){

        Integer size = persons.getPersons().size();
        Log.i("SIZE", size.toString());


        Map<String, Object> item = new HashMap<String, Object>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        for (Person p : persons.getPersons()) {
            item = new HashMap<String, Object>();
            item.put("identify", p.getIdentify());
            item.put("nome", p.getNome());
            item.put("nascimento", df.format(p.getDataNascimento()));
            personsMAP.add(item);
        }
        return personsMAP;
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
        if (id == R.id.action_settings) {
            new HttpRequestTask().execute();
            return true;
        } else if (id == R.id.sair) {
            super.onDestroy();
            return true;
        } else if (id == R.id.salvar){
            salvar();
        } else if (id == R.id.notificacao){
            notificacao();
        }

        return super.onOptionsItemSelected(item);
    }

    private void notificacao() {

    }
    private class HttpRequestTask extends AsyncTask<Void, Void, Persons> {
        @Override
        protected Persons doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Persons person = restTemplate.getForObject(url, Persons.class);
                return person;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Persons person) {
            persons = person;
            for(Person p : persons.getPersons()) {
                Log.i("REST", p.toString());
            }

            teste();
        }

    }


    public void teste(){
        String[] de = {"identify", "nome", "nascimento"};
        int[] para = {R.id.tv_identify, R.id.tv_nome, R.id.tv_dt_nascimento};

        adapter = new SimpleAdapter(this, getPersons(), R.layout.activity_main, de, para);
        setListAdapter(adapter);

        adapter.areAllItemsEnabled();
        adapter.notifyDataSetChanged();
    }

}
