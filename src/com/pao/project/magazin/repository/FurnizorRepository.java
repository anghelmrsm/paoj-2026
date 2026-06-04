package com.pao.project.magazin.repository;

import com.pao.project.magazin.model.Furnizor;
import java.sql.*;
import java.util.*;

public class FurnizorRepository extends JdbcRepositorySupport implements Repository<Furnizor,Integer>{
    public void save(Furnizor f){write("INSERT INTO furnizori(id,nume,telefon) VALUES(?,?,?)",f);}
    public Optional<Furnizor> findById(Integer id){try(PreparedStatement s=connection().prepareStatement("SELECT * FROM furnizori WHERE id=?")){s.setInt(1,id);try(ResultSet r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}catch(SQLException e){throw failure("cautare furnizor",e);}}
    public List<Furnizor> findAll(){List<Furnizor> l=new ArrayList<>();try(PreparedStatement s=connection().prepareStatement("SELECT * FROM furnizori ORDER BY nume");ResultSet r=s.executeQuery()){while(r.next())l.add(map(r));return l;}catch(SQLException e){throw failure("listare furnizori",e);}}
    public void update(Furnizor f){write("UPDATE furnizori SET nume=?,telefon=? WHERE id=?",f);}
    public void delete(Integer id){try(PreparedStatement s=connection().prepareStatement("DELETE FROM furnizori WHERE id=?")){s.setInt(1,id);s.executeUpdate();}catch(SQLException e){throw failure("stergere furnizor",e);}}
    private void write(String sql,Furnizor f){try(PreparedStatement s=connection().prepareStatement(sql)){if(sql.startsWith("INSERT")){s.setInt(1,f.getId());s.setString(2,f.getNume());s.setString(3,f.getTelefon());}else{s.setString(1,f.getNume());s.setString(2,f.getTelefon());s.setInt(3,f.getId());}s.executeUpdate();}catch(SQLException e){throw failure("salvare furnizor",e);}}
    private Furnizor map(ResultSet r)throws SQLException{return new Furnizor(r.getInt("id"),r.getString("nume"),r.getString("telefon"));}
}
