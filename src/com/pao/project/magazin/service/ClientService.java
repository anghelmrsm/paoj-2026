package com.pao.project.magazin.service;

import com.pao.project.magazin.model.Client;
import com.pao.project.magazin.repository.ClientRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientService {
    private static final ClientService INSTANCE=new ClientService();
    private final ClientRepository repository=new ClientRepository();
    private final Map<Integer, Client> clienti=new HashMap<>();
    private ClientService(){}
    public static ClientService getInstance(){return INSTANCE;}
    public void adauga(Client c){clienti.put(c.getId(),c);repository.save(c);AuditService.getInstance().log("adauga_client");}
    public void actualizeaza(Client c){clienti.put(c.getId(),c);repository.update(c);AuditService.getInstance().log("actualizeaza_client");}
    public void sterge(int id){clienti.remove(id);repository.delete(id);AuditService.getInstance().log("sterge_client");}
    public Client cauta(int id){AuditService.getInstance().log("cauta_client");return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Client inexistent."));}
    public List<Client> listeaza(){AuditService.getInstance().log("listeaza_clienti");return repository.findAll();}
}
