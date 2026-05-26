package com.pao.laboratory14.exercise2;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.repository.EvenimentRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        EvenimentRepository repository = new EvenimentRepository();
        repository.initSchema();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String command = scanner.next();
            if ("ADD".equals(command)) {
                Eveniment event = new Eveniment(scanner.next(), scanner.next(), scanner.nextInt(),
                        TipBilet.valueOf(scanner.next()));
                repository.save(event);
                System.out.println("Adaugat: [" + event.getId() + "] " + event.getNume());
            } else if ("LIST".equals(command)) {
                repository.findAll().forEach(System.out::println);
            } else if ("DELETE".equals(command)) {
                int id = scanner.nextInt();
                System.out.println(repository.deleteImpl(id) > 0 ? "Sters: " + id : "Nu exista: " + id);
            } else if ("COUNT".equals(command)) {
                System.out.println("Total: " + repository.count());
            }
        }
    }
}
