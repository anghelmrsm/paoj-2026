package com.pao.project.magazin.model;

import java.time.LocalDateTime;
import java.util.List;

public class Comanda {
    private final int id;
    private final Client client;
    private final LocalDateTime data;
    private final List<LinieComanda> produse;

    public Comanda(int id, Client client, LocalDateTime data, List<LinieComanda> produse) {
        this.id = id;
        this.client = client;
        this.data = data;
        this.produse = List.copyOf(produse);
    }

    public int getId() { return id; }
    public Client getClient() { return client; }
    public LocalDateTime getData() { return data; }
    public List<LinieComanda> getProduse() { return produse; }
    public double getTotal() { return produse.stream().mapToDouble(LinieComanda::getSubtotal).sum(); }

    @Override
    public String toString() {
        return String.format("Comanda[id=%d, client=%s, total=%.2f]", id, client.getNume(), getTotal());
    }
}
