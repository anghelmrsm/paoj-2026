package com.pao.project.magazin;

import com.pao.project.magazin.exception.ProdusInexistentException;
import com.pao.project.magazin.exception.StocInsuficientException;
import com.pao.project.magazin.model.*;
import com.pao.project.magazin.service.*;
import com.pao.project.magazin.util.SchemaInitializer;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SchemaInitializer.resetSchema();
        ProdusService produse=ProdusService.getInstance();
        ClientService clienti=ClientService.getInstance();
        ComandaService comenzi=ComandaService.getInstance();
        RaportService rapoarte=RaportService.getInstance();

        Categorie electronice=new Categorie(1,"Electronice");
        Categorie alimente=new Categorie(2,"Alimente");
        Furnizor tech=new Furnizor(1,"Tech Distribution","0711000000");
        Furnizor food=new Furnizor(2,"Fresh Food","0722000000");
        produse.adaugaCategorie(electronice);
        produse.adaugaCategorie(alimente);
        produse.adaugaFurnizor(tech);
        produse.adaugaFurnizor(food);

        Produs laptop=new Produs(new CodProdus("P001"),"Laptop",3500,5,electronice,tech);
        Produs mouse=new Produs(new CodProdus("P002"),"Mouse",100,20,electronice,tech);
        Produs cafea=new Produs(new CodProdus("P003"),"Cafea",35,12,alimente,food);
        Produs temporar=new Produs(new CodProdus("P999"),"Produs temporar",10,1,alimente,food);
        produse.adaugaProdus(laptop);
        produse.adaugaProdus(mouse);
        produse.adaugaProdus(cafea);
        produse.adaugaProdus(temporar);
        cafea=new Produs(new CodProdus("P003"),"Cafea premium",40,12,alimente,food);
        produse.actualizeazaProdus(cafea);
        produse.stergeProdus(temporar.getCod());

        Client ana=new Client(1,"Ana Popescu","ana@example.com",100);
        Client ion=new Client(2,"Ion Ionescu","ion@example.com",50);
        clienti.adauga(ana);
        clienti.adauga(ion);

        comenzi.plaseaza(new Comanda(1,ana,LocalDateTime.now(),List.of(
                new LinieComanda(laptop,1),new LinieComanda(mouse,2))));
        comenzi.plaseaza(new Comanda(2,ion,LocalDateTime.now(),List.of(
                new LinieComanda(cafea,3),new LinieComanda(mouse,1))));

        try {
            produse.cautaProdus(new CodProdus("LIPSA"));
        } catch (ProdusInexistentException ex) {
            System.out.println("Exceptie tratata: " + ex.getMessage());
        }
        try {
            comenzi.plaseaza(new Comanda(3,ion,LocalDateTime.now(),List.of(new LinieComanda(laptop,100))));
        } catch (StocInsuficientException ex) {
            System.out.println("Exceptie tratata: " + ex.getMessage());
        }

        System.out.println("=== Produse sortate ===");
        produse.listeazaProduse().forEach(System.out::println);
        System.out.println("\n=== Cautare produs ===");
        System.out.println(produse.cautaProdus(new CodProdus("P001")));
        System.out.println("\n=== Produse din categoria Electronice ===");
        produse.produseDinCategorie(1).forEach(System.out::println);
        System.out.println("\n=== Produse cu stoc mic ===");
        produse.produseCuStocMic(10).forEach(System.out::println);
        System.out.println("\n=== Clienti ===");
        clienti.listeaza().forEach(System.out::println);
        System.out.println("\n=== Raport produse/categorii/furnizori ===");
        rapoarte.produseCuCategorieSiFurnizor().forEach(System.out::println);
        System.out.println("\n=== Raport comenzi clienti ===");
        rapoarte.comenziClienti().forEach(System.out::println);
        System.out.println("\n=== Top produse vandute ===");
        rapoarte.topProduse().forEach(System.out::println);

        Manager manager=new Manager(1,"Maria Manager","manager@magazin.ro",9000,"Vanzari");
        System.out.println("\n=== Polimorfism ===\n"+manager);
        if(args.length>0&&"--menu".equals(args[0]))new ConsoleMenu().run();
    }
}
