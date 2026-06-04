package com.pao.project.magazin.repository;

import com.pao.project.magazin.model.Categorie;
import java.sql.*;
import java.util.*;

public class CategorieRepository extends JdbcRepositorySupport implements Repository<Categorie, Integer> {
    public void save(Categorie c) { execute("INSERT INTO categorii(id,nume) VALUES(?,?)", c); }
    public Optional<Categorie> findById(Integer id) {
        try (PreparedStatement s=connection().prepareStatement("SELECT id,nume FROM categorii WHERE id=?")) {
            s.setInt(1,id); try(ResultSet r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}
        } catch(SQLException e){throw failure("cautare categorie",e);}
    }
    public List<Categorie> findAll() {
        List<Categorie> list=new ArrayList<>();
        try(PreparedStatement s=connection().prepareStatement("SELECT id,nume FROM categorii ORDER BY nume");ResultSet r=s.executeQuery()){
            while(r.next())list.add(map(r)); return list;
        }catch(SQLException e){throw failure("listare categorii",e);}
    }
    public void update(Categorie c){execute("UPDATE categorii SET nume=? WHERE id=?",c);}
    public void delete(Integer id){try(PreparedStatement s=connection().prepareStatement("DELETE FROM categorii WHERE id=?")){s.setInt(1,id);s.executeUpdate();}catch(SQLException e){throw failure("stergere categorie",e);}}
    private void execute(String sql,Categorie c){try(PreparedStatement s=connection().prepareStatement(sql)){if(sql.startsWith("INSERT")){s.setInt(1,c.getId());s.setString(2,c.getNume());}else{s.setString(1,c.getNume());s.setInt(2,c.getId());}s.executeUpdate();}catch(SQLException e){throw failure("salvare categorie",e);}}
    private Categorie map(ResultSet r)throws SQLException{return new Categorie(r.getInt("id"),r.getString("nume"));}
}
