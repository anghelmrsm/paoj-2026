package com.pao.project.magazin.repository;

import com.pao.project.magazin.model.*;
import java.sql.*;

public class ComandaRepository extends JdbcRepositorySupport {
    public void save(Connection connection, Comanda comanda) throws SQLException {
        try (PreparedStatement s=connection.prepareStatement("INSERT INTO comenzi(id,client_id,data,total) VALUES(?,?,?,?)")){
            s.setInt(1,comanda.getId());s.setInt(2,comanda.getClient().getId());s.setString(3,comanda.getData().toString());s.setDouble(4,comanda.getTotal());s.executeUpdate();
        }
        for(LinieComanda linie:comanda.getProduse()){
            try(PreparedStatement s=connection.prepareStatement("INSERT INTO linii_comanda(comanda_id,produs_cod,cantitate,pret_unitar) VALUES(?,?,?,?)")){
                s.setInt(1,comanda.getId());s.setString(2,linie.getProdus().getCod().toString());s.setInt(3,linie.getCantitate());s.setDouble(4,linie.getProdus().getPret());s.executeUpdate();
            }
        }
    }
}
