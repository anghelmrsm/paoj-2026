package com.pao.project.magazin.service;

import com.pao.project.magazin.exception.*;
import com.pao.project.magazin.model.*;
import com.pao.project.magazin.repository.*;
import com.pao.project.magazin.util.DatabaseConnection;
import java.sql.*;

public final class ComandaService {
    private static final ComandaService INSTANCE=new ComandaService();
    private final ComandaRepository comenzi=new ComandaRepository();
    private final ProdusRepository produse=new ProdusRepository();
    private ComandaService(){}
    public static ComandaService getInstance(){return INSTANCE;}
    public void plaseaza(Comanda comanda){
        for(LinieComanda l:comanda.getProduse())if(l.getCantitate()>l.getProdus().getStoc())throw new StocInsuficientException("Stoc insuficient pentru "+l.getProdus().getNume());
        Connection c=DatabaseConnection.getInstance().getConnection();
        try{
            c.setAutoCommit(false);comenzi.save(c,comanda);
            for(LinieComanda l:comanda.getProduse())produse.scadeStoc(c,l.getProdus().getCod(),l.getCantitate());
            c.commit();AuditService.getInstance().log("plaseaza_comanda");
        }catch(SQLException e){try{c.rollback();}catch(SQLException rollback){e.addSuppressed(rollback);}throw new PersistenceException("Comanda nu a putut fi plasata.",e);}
        finally{try{c.setAutoCommit(true);}catch(SQLException e){throw new PersistenceException("Auto-commit nu a putut fi restaurat.",e);}}
    }
}
