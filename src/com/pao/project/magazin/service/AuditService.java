package com.pao.project.magazin.service;

import java.io.*;
import java.time.LocalDateTime;

public final class AuditService {
    private static final AuditService INSTANCE=new AuditService();
    private AuditService(){}
    public static AuditService getInstance(){return INSTANCE;}
    public synchronized void log(String actiune){
        String path=System.getProperty("magazin.audit.file","src/com/pao/project/magazin/audit.csv");
        try(BufferedWriter w=new BufferedWriter(new FileWriter(path,true))){w.write(actiune+","+ LocalDateTime.now());w.newLine();}
        catch(IOException e){System.err.println("Eroare audit: "+e.getMessage());}
    }
}
