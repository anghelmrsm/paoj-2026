package com.pao.project.magazin.service;

import com.pao.project.magazin.exception.ProdusInexistentException;
import com.pao.project.magazin.model.*;
import com.pao.project.magazin.repository.*;
import java.util.*;

public final class ProdusService {
    private static final ProdusService INSTANCE=new ProdusService();
    private final Map<CodProdus,Produs> produse=new HashMap<>();
    private final ProdusRepository produsRepository=new ProdusRepository();
    private final CategorieRepository categorieRepository=new CategorieRepository();
    private final FurnizorRepository furnizorRepository=new FurnizorRepository();
    private ProdusService(){}
    public static ProdusService getInstance(){return INSTANCE;}
    public void adaugaCategorie(Categorie c){categorieRepository.save(c);AuditService.getInstance().log("adauga_categorie");}
    public void adaugaFurnizor(Furnizor f){furnizorRepository.save(f);AuditService.getInstance().log("adauga_furnizor");}
    public void adaugaProdus(Produs p){produse.put(p.getCod(),p);produsRepository.save(p);AuditService.getInstance().log("adauga_produs");}
    public void actualizeazaProdus(Produs p){produse.put(p.getCod(),p);produsRepository.update(p);AuditService.getInstance().log("actualizeaza_produs");}
    public void stergeProdus(CodProdus cod){produse.remove(cod);produsRepository.delete(cod);AuditService.getInstance().log("sterge_produs");}
    public Produs cautaProdus(CodProdus cod){AuditService.getInstance().log("cauta_produs");return produsRepository.findById(cod).orElseThrow(()->new ProdusInexistentException("Produs inexistent: "+cod));}
    public List<Produs> listeazaProduse(){AuditService.getInstance().log("listeaza_produse");return new ArrayList<>(new TreeSet<>(produsRepository.findAll()));}
    public List<Produs> produseDinCategorie(int id){AuditService.getInstance().log("produse_din_categorie");return produsRepository.findByCategorie(id);}
    public List<Produs> produseCuStocMic(int prag){AuditService.getInstance().log("produse_cu_stoc_mic");return produsRepository.findLowStock(prag);}
}
