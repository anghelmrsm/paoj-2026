package com.pao.project.magazin.service;

import com.pao.project.magazin.exception.PersistenceException;
import com.pao.project.magazin.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public final class RaportService {
    private static final RaportService INSTANCE=new RaportService();
    private RaportService(){}
    public static RaportService getInstance(){return INSTANCE;}
    public List<String> produseCuCategorieSiFurnizor(){return query("SELECT p.nume,c.nume categorie,f.nume furnizor,p.stoc FROM produse p JOIN categorii c ON c.id=p.categorie_id JOIN furnizori f ON f.id=p.furnizor_id",r->r.getString("nume")+" | "+r.getString("categorie")+" | "+r.getString("furnizor")+" | stoc="+r.getInt("stoc"));}
    public List<String> comenziClienti(){return query("SELECT cl.nume,co.id,co.total FROM comenzi co JOIN clienti cl ON cl.id=co.client_id ORDER BY co.id",r->"Comanda "+r.getInt("id")+" | "+r.getString("nume")+" | total="+r.getDouble("total"));}
    public List<String> topProduse(){return query("SELECT p.nume,SUM(l.cantitate) vandute FROM linii_comanda l JOIN produse p ON p.cod=l.produs_cod GROUP BY p.cod,p.nume ORDER BY vandute DESC",r->r.getString("nume")+" | vandute="+r.getInt("vandute"));}
    private List<String> query(String sql,Mapper mapper){List<String> l=new ArrayList<>();try(PreparedStatement s=DatabaseConnection.getInstance().getConnection().prepareStatement(sql);ResultSet r=s.executeQuery()){while(r.next())l.add(mapper.map(r));AuditService.getInstance().log("genereaza_raport");return l;}catch(SQLException e){throw new PersistenceException("Raportul nu a putut fi generat.",e);}}
    private interface Mapper{String map(ResultSet r)throws SQLException;}
}
