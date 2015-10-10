package br.com.mrcsfelipe.markfulldroid.activitys;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import br.com.mrcsfelipe.markfulldroid.model.Person;
import br.com.mrcsfelipe.markfulldroid.services.PersonsModel;

/**
 * Created by markFelipe on 08/10/15.
 */
public class CriarNotificacao extends Service {


    private Person person;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //START_STICKY --> starta sem a necessidade de uma intent caso o Android mate o Service por alguma configuração
        //START_NOT_STICKY --> O servico so deve ser reiniciado se houver uma intent que o chame

        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
        long delayInicial = 0L;
        long periodo = 10;
        TimeUnit unit = TimeUnit.MINUTES;
        pool.scheduleAtFixedRate(new VerificacaoSend(), delayInicial, periodo, unit);

        return START_STICKY;
    }

    private class VerificacaoSend implements Runnable{

        List<Person> persons = new ArrayList<>();

        @Override
        public void run() {

            /**
             *
             *  Verifica se estiver logado , caso aocontrario return;
             * **/

            try {

                enviarTodos();

            }catch (Exception e){
                Log.e(getPackageName(), e.getMessage(), e);
            }

        }


        public void enviarTodos(){
            persons = Person.findWithQuery(Person.class, "Select * from Person where foiEnviado = ?", "false");
            new HttpRequestTask().execute(MediaType.APPLICATION_JSON);

        }

        private class HttpRequestTask extends AsyncTask<MediaType, Void, String> {

            @Override
            protected String doInBackground(MediaType... params) {
                try {

                    MediaType mediaType = params[0];

                    HttpHeaders requestHeaders = new HttpHeaders();

                    // Sending a JSON or XML object i.e. "application/json" or "application/xml"
                    requestHeaders.setContentType(mediaType);

                    // Populate the Message object to serialize and headers in an
                    // HttpEntity object to use for the request
                    PersonsModel person = new PersonsModel();
                    person.setPersons(persons);
                    HttpEntity<PersonsModel> requestEntity = new HttpEntity<PersonsModel>(person, requestHeaders);

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
                    return "erroServer";
                }
            }

            @Override
            protected void onPostExecute(String person) {

                if(person == ""){

                }else if(person == "erroServer"){


                }else if(person == "serverOff"){


                }else {
                    enviadoSalvaSQL();
                    Log.d("POST", person.toString());
                }
            }

            public void enviadoSalvaSQL(){
                Person p = Person.findById(Person.class, person.getId());
                p.setFoiEnviado("true");
                p.save();
            }

        }
    }


}
