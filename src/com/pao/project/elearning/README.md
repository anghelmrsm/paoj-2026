# Platforma E-Learning

Aplicatie consola pentru cursuri, studenti, instructori, inscrieri, quiz-uri si scoruri.

## Actiuni

1. Adaugare instructor.
2. Inregistrare student.
3. Adaugare curs.
4. Inscriere student la curs.
5. Creare quiz.
6. Notare quiz.
7. Listare cursuri disponibile.
8. Cautare cursuri dupa categorie.
9. Listare studenti inscrisi intr-un curs.
10. Afisare scoruri quiz pentru un student.

## Obiecte

`Person`, `Student`, `Instructor`, `CourseCode`, `Course`, `Enrollment`, `Quiz`, `ScoreRecord`.

## Etapa II

- Persistenta SQLite configurata prin `resources/db.properties`.
- Schema relationala in `schema.sql`, cu tabele pentru instructori, studenti, cursuri, inscrieri, quiz-uri si scoruri.
- Repository generic si implementari JDBC CRUD pentru toate cele sase entitati persistate.
- `EnrollmentService.enrollStudent(...)` foloseste o tranzactie explicita: salveaza inscrierea si actualizeaza contorul cursului cu `commit`/`rollback`.
- `ReportService` expune trei rapoarte SQL cu `JOIN`.
- `AuditService` este singleton thread-safe si scrie in mod append in `audit.csv`.

## Structura

```text
elearning/
  Main.java
  schema.sql
  resources/db.properties
  model/
  repository/
  service/
  exception/
  util/
```

## Rulare

Driverul SQLite este inclus in `lib/sqlite-jdbc-3.36.0.3.jar`.

```powershell
javac -cp "lib\sqlite-jdbc-3.36.0.3.jar" -d output src\com\pao\project\elearning\Main.java src\com\pao\project\elearning\model\*.java src\com\pao\project\elearning\service\*.java src\com\pao\project\elearning\exception\*.java src\com\pao\project\elearning\repository\*.java src\com\pao\project\elearning\util\*.java
java -cp "output;src;lib\sqlite-jdbc-3.36.0.3.jar" com.pao.project.elearning.Main
```
