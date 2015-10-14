package br.com.mrcsfelipe.markfulldroid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by markFelipe on 06/10/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person{

    @JsonProperty
    private Integer identify;

    @JsonProperty
    private String nome;

    @JsonProperty
    private Date dataNascimento;

    @JsonIgnore
    private String foiEnviado;

    public Person(Integer identify, String nome, Date dataNascimento, String foiEnviado) {
        this.identify = identify;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.foiEnviado = foiEnviado;
    }

    public Person() {
    }


    public String getFoiEnviado() {
        return foiEnviado;
    }

    public void setFoiEnviado(String foiEnviado) {
        this.foiEnviado = foiEnviado;
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

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
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
