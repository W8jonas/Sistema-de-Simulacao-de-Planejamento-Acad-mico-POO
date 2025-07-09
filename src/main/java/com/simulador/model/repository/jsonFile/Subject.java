package com.simulador.model.repository;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class Subject implements ISubject {
    private static final String PATH = DIRECTORY+ File.separator +"contatos.json";
    @Override
    public void save(List<Contato> itens) {
        Gson gson = new Gson();
        String json = gson.toJson(itens);

        File diretorio = new File(DIRECTORY);
        if(!diretorio.exists())
            diretorio.mkdirs();

        Arquivo.salva(PATH, json);
    }

    @Override
    public List<Contato> findAll() {
        Gson gson = new Gson();

        String json = Arquivo.le(PATH);

        List<Contato> contatos = new ArrayList<>();
        if(!json.trim().equals("")) {

            Type tipoLista = new TypeToken<List<Contato>>() {
            }.getType();
            contatos = gson.fromJson(json, tipoLista);

            if (contatos == null)
                contatos = new ArrayList<>();
        }

        return contatos;
    }
}
