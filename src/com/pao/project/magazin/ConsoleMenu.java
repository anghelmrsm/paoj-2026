package com.pao.project.magazin;

import com.pao.project.magazin.model.CodProdus;
import com.pao.project.magazin.service.*;
import java.util.Scanner;

public final class ConsoleMenu {
    private final Scanner scanner=new Scanner(System.in);
    private final ProdusService produse=ProdusService.getInstance();
    private final ClientService clienti=ClientService.getInstance();
    private final RaportService rapoarte=RaportService.getInstance();

    public void run(){
        int optiune;
        do{
            System.out.println("""

                    === Gestiune Magazin ===
                    1. Listeaza produse
                    2. Cauta produs dupa cod
                    3. Afiseaza produse cu stoc mic
                    4. Listeaza clienti
                    5. Raport produse, categorii si furnizori
                    6. Raport comenzi clienti
                    7. Top produse vandute
                    0. Iesire
                    """);
            optiune=Integer.parseInt(read("Optiune: "));
            try{execute(optiune);}catch(RuntimeException e){System.out.println("Eroare: "+e.getMessage());}
        }while(optiune!=0);
    }

    private void execute(int optiune){
        switch(optiune){
            case 1->produse.listeazaProduse().forEach(System.out::println);
            case 2->System.out.println(produse.cautaProdus(new CodProdus(read("Cod: "))));
            case 3->produse.produseCuStocMic(Integer.parseInt(read("Prag: "))).forEach(System.out::println);
            case 4->clienti.listeaza().forEach(System.out::println);
            case 5->rapoarte.produseCuCategorieSiFurnizor().forEach(System.out::println);
            case 6->rapoarte.comenziClienti().forEach(System.out::println);
            case 7->rapoarte.topProduse().forEach(System.out::println);
            case 0->System.out.println("Aplicatia s-a inchis.");
            default->System.out.println("Optiune invalida.");
        }
    }
    private String read(String mesaj){System.out.print(mesaj);return scanner.nextLine().trim();}
}
