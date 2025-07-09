package com.simulador.model.repository;
import java.util.List;

public interface ISubject {
    String DIRECTORY = "data";
    public void save(List<Subject> itens);
    public List<Subject> findAll();
}
