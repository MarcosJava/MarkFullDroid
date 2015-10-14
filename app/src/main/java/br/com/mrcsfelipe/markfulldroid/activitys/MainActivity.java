package br.com.mrcsfelipe.markfulldroid.activitys;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
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

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener,
        DialogInterface.OnClickListener{

    private List<Map<String, Object>> personsMAP = new ArrayList<>();
    private Persons persons;
    private SimpleAdapter adapter;
    static final String url = "http://10.0.2.2:8080/MarkFullOne/rest/persons";
    static final String url_delete = "http://10.0.2.2:8080/MarkFullOne/rest/persons/delete";
    static final MediaType mediaType = MediaType.APPLICATION_JSON;


    private AlertDialog alertDialog;
    private AlertDialog dialogConfirmacao;
    private int posicaoClickPerson = 0; // POSICAO NA LISTA
    private Integer personDeleted = 0; // ID DO DELETE



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //new HttpRequestTask().execute();
        popularAdpter();
        adapter.areAllItemsEnabled();
        getListView().setOnItemClickListener(this);

        this.alertDialog = criaAlertDialog();
        this.dialogConfirmacao = criaDialogConfirmacao();

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

        personsMAP = new ArrayList<>();

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
            new GETPerson().execute();
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





    /*
    *
    *
    * Click DIALOG MENU
    *
    * */
    private AlertDialog criaAlertDialog() {
        final CharSequence[] items = {
                getString(R.string.excluir)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);
        return builder.create();
    }


    /**
     *
     *  IMPLEMENTACAO DA INTERFACE DE CLICK NO ITEM
     *
     * */
    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position, long id) {

        Map<String, Object> map = personsMAP.get(position);

        String destino = (String) map.get("identify").toString();
        String mensagem = "Person seleciondo : "+ destino;
        personDeleted = (Integer) map.get("identify");
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        this.posicaoClickPerson = position;
        alertDialog.show();
    }
    /**
     *
     * DIALOG CONFIRMACAO
     *
     */

    private AlertDialog criaDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);
        return builder.create();
    }


/******
 *
 *  Resposta de todos os dialogs
 *
 * ******/
    @Override
    public void onClick(DialogInterface dialog, int which) {

        Log.i("valor On Click no Which", "" + which);

        switch (which){
            case 0:
                dialogConfirmacao.show();
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                personsMAP.remove(this.posicaoClickPerson);
                new DELETEPerson().execute();
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;

        }
    }

    /***
     *
     *
     *  FAZENDO O GET PARA O SERVER
     *
     * ***/
    private class GETPerson extends AsyncTask<Void, Void, Persons> {
        @Override
        protected Persons doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Persons person = new Persons();
                person = restTemplate.getForObject(url, Persons.class);
                return person;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Persons person) {
            persons = new Persons();
            persons = person;
            for(Person p : persons.getPersons()) {
                Log.i("REST", p.toString());
            }

            populandoListView();
        }

    }


    /******
     *
     *
     *  DELETE PARA O SERVIDOR
     *
     * *******/
    private class DELETEPerson extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {

                //HttpHeaders headers = new HttpHeaders();
                //headers.set("Accept", String.valueOf(MediaType.APPLICATION_JSON));



                //HttpEntity<?> entity = new HttpEntity<>(headers);
                Log.i("Person deleted", personDeleted + "");
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("id", personDeleted);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.delete(url_delete + "/?id=" + personDeleted);

                return "Deletado Com Sucesso";

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String person) {

            String mensagem =  getString(R.string.erro_deletar);

            if("Deletado Com Sucesso".equalsIgnoreCase(person.trim())){
                posicaoClickPerson = 0;
            } else {
                Toast toast = Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_SHORT);
                toast.show();
                populandoListView();
            }
        }

    }


    public void populandoListView(){

        adapter.notifyDataSetChanged();


        String[] de = {"identify", "nome", "nascimento"};
        int[] para = {R.id.tv_identify, R.id.tv_nome, R.id.tv_dt_nascimento};
        adapter = new SimpleAdapter(this, null, R.layout.activity_main, de, para); // limpando
        adapter = new SimpleAdapter(this, getPersons(), R.layout.activity_main, de, para);
        setListAdapter(adapter);

        adapter.areAllItemsEnabled();
        adapter.notifyDataSetChanged();
    }

}
