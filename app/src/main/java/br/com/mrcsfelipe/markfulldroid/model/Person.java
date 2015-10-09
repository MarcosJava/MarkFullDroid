package br.com.mrcsfelipe.markfulldroid.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by markFelipe on 06/10/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person{



    private Integer identify;

    private String nome;

    private String dataNascimento;

    public Person() {
    }

    public Integer getIdentify() {
        return identify;
    }

    public void setIdentify(Integer identify) {
        this.identify = identify;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return "Person{" +
                "identify=" + identify +
                ", nome='" + nome + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }
}
