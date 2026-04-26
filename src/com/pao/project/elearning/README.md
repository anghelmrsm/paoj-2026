# Platforma E-Learning

Proiectul demonstreaza o aplicatie OOP simpla pentru un sistem de e-learning.

## Functionalitati implementate

- Adaugare instructor
- Inregistrare student
- Adaugare curs
- Inscriere student la curs
- Creare quiz
- Notare quiz
- Listare cursuri disponibile
- Cautare cursuri pe categorie
- Listare studenti inscrisi intr-un curs
- Afisare scoruri quiz pentru un student

## Structura proiectului

- `model/` - modelele domeniului
- `service/` - serviciile pentru operatiuni
- `exception/` - exceptii custom
- `Main.java` - demonstratie a functionalitatilor

## Cum rulezi

```bash
javac src/com/pao/project/elearning/Main.java src/com/pao/project/elearning/model/*.java src/com/pao/project/elearning/service/*.java src/com/pao/project/elearning/exception/*.java
java -cp src com.pao.project.elearning.Main
```
