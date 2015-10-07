package br.com.mrcsfelipe.markfulldroid.model;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by markFelipe on 06/10/15.
 */
public class Person extends SugarRecord<Person>{

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
