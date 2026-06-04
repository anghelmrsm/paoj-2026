package com.pao.project.magazin.repository;

import com.pao.project.magazin.model.*;
import java.sql.*;
import java.util.*;

public class ProdusRepository extends JdbcRepositorySupport implements Repository<Produs,CodProdus>{
    private final CategorieRepository categorii=new CategorieRepository();
    private final FurnizorRepository furnizori=new FurnizorRepository();
    public void save(Produs p){write("INSERT INTO produse(cod,nume,pret,stoc,categorie_id,furnizor_id) VALUES(?,?,?,?,?,?)",p);}
    public Optional<Produs> findById(CodProdus cod){try(PreparedStatement s=connection().prepareStatement("SELECT * FROM produse WHERE cod=?")){s.setString(1,cod.toString());try(ResultSet r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}catch(SQLException e){throw failure("cautare produs",e);}}
    public List<Produs> findAll(){return query("SELECT * FROM produse ORDER BY nume",null);}
    public List<Produs> findByCategorie(int id){return query("SELECT * FROM produse WHERE categorie_id=? ORDER BY nume",id);}
    public List<Produs> findLowStock(int prag){return query("SELECT * FROM produse WHERE stoc<=? ORDER BY stoc",prag);}
    public void update(Produs p){write("UPDATE produse SET nume=?,pret=?,stoc=?,categorie_id=?,furnizor_id=? WHERE cod=?",p);}
    public void delete(CodProdus cod){try(PreparedStatement s=connection().prepareStatement("DELETE FROM produse WHERE cod=?")){s.setString(1,cod.toString());s.executeUpdate();}catch(SQLException e){throw failure("stergere produs",e);}}
    public void scadeStoc(Connection c,CodProdus cod,int cantitate)throws SQLException{try(PreparedStatement s=c.prepareStatement("UPDATE produse SET stoc=stoc-? WHERE cod=? AND stoc>=?")){s.setInt(1,cantitate);s.setString(2,cod.toString());s.setInt(3,cantitate);if(s.executeUpdate()!=1)throw new SQLException("Stoc insuficient pentru "+cod);}}
    private List<Produs> query(String sql,Integer value){List<Produs> l=new ArrayList<>();try(PreparedStatement s=connection().prepareStatement(sql)){if(value!=null)s.setInt(1,value);try(ResultSet r=s.executeQuery()){while(r.next())l.add(map(r));}return l;}catch(SQLException e){throw failure("listare produse",e);}}
    private void write(String sql,Produs p){try(PreparedStatement s=connection().prepareStatement(sql)){if(sql.startsWith("INSERT")){s.setString(1,p.getCod().toString());s.setString(2,p.getNume());s.setDouble(3,p.getPret());s.setInt(4,p.getStoc());s.setInt(5,p.getCategorie().getId());s.setInt(6,p.getFurnizor().getId());}else{s.setString(1,p.getNume());s.setDouble(2,p.getPret());s.setInt(3,p.getStoc());s.setInt(4,p.getCategorie().getId());s.setInt(5,p.getFurnizor().getId());s.setString(6,p.getCod().toString());}s.executeUpdate();}catch(SQLException e){throw failure("salvare produs",e);}}
    private Produs map(ResultSet r)throws SQLException{return new Produs(new CodProdus(r.getString("cod")),r.getString("nume"),r.getDouble("pret"),r.getInt("stoc"),categorii.findById(r.getInt("categorie_id")).orElseThrow(),furnizori.findById(r.getInt("furnizor_id")).orElseThrow());}
}
