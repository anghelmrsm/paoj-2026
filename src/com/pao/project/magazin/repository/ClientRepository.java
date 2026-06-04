package com.pao.project.magazin.repository;

import com.pao.project.magazin.model.Client;
import java.sql.*;
import java.util.*;

public class ClientRepository extends JdbcRepositorySupport implements Repository<Client,Integer>{
    public void save(Client c){write("INSERT INTO clienti(id,nume,email,puncte) VALUES(?,?,?,?)",c);}
    public Optional<Client> findById(Integer id){try(PreparedStatement s=connection().prepareStatement("SELECT * FROM clienti WHERE id=?")){s.setInt(1,id);try(ResultSet r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}catch(SQLException e){throw failure("cautare client",e);}}
    public List<Client> findAll(){List<Client> l=new ArrayList<>();try(PreparedStatement s=connection().prepareStatement("SELECT * FROM clienti ORDER BY nume");ResultSet r=s.executeQuery()){while(r.next())l.add(map(r));return l;}catch(SQLException e){throw failure("listare clienti",e);}}
    public void update(Client c){write("UPDATE clienti SET nume=?,email=?,puncte=? WHERE id=?",c);}
    public void delete(Integer id){try(PreparedStatement s=connection().prepareStatement("DELETE FROM clienti WHERE id=?")){s.setInt(1,id);s.executeUpdate();}catch(SQLException e){throw failure("stergere client",e);}}
    private void write(String sql,Client c){try(PreparedStatement s=connection().prepareStatement(sql)){if(sql.startsWith("INSERT")){s.setInt(1,c.getId());s.setString(2,c.getNume());s.setString(3,c.getEmail());s.setInt(4,c.getPuncteFidelitate());}else{s.setString(1,c.getNume());s.setString(2,c.getEmail());s.setInt(3,c.getPuncteFidelitate());s.setInt(4,c.getId());}s.executeUpdate();}catch(SQLException e){throw failure("salvare client",e);}}
    private Client map(ResultSet r)throws SQLException{return new Client(r.getInt("id"),r.getString("nume"),r.getString("email"),r.getInt("puncte"));}
}
