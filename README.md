# Game-Manager

## Project scoring condition:

- No compilation errors present
- Implement the given requirements
- Clean code & Readability - try to follow DRY principle (DRY = Do not repeat yourself)
- The code follows JAVA coding styles and conventions.
- All tests passed

## I. Define a system of your choice.

1. Define 10 business requirements for the chosen business domain
2. Prepare a document based on the 10 business requirements containing a description of 5 main features your project should contain for the MVP (minimum viable product) phase.
3. Create a repository for your project and commit the document for review.

### 1. 10 Business requirements:

**Domeniul de business:** System de management pentru jocuri video

1. **Gestionarea utilizatorilor si rolurilor**: Sistemul trebuie sa permita crearea si autentificarea diferitelor tipuri de utilizatori cu roluri si permisiuni distincte:

   - admin
   - client
   - provider => developer + publisher

2. **Managementul jocurilor**: Dezvoltatorii trebuie sa poata anunta si edita jocuri noi. Publisherii trebuie sa poata edita si publica jocurile cu care au contract.

3. **System de contracte**: Publisherii si dezvoltatorii pot crea, actualiza si gestiona contracte pentru dezvoltarea si publicarea jocurilor. Fiecare contract se va face intre un publisher si un joc.

4. **Marketplace pentru jocuri**: Clientii trebuie sa poata vizualiza jocurile disponibile, sa le cumpere si sa le adauge in biblioteca personala.

5. **Biblioteca personala a utilizatorilor**: Clientii pot vizualiza si accesa jocurile achizitionate in biblioteca lor.

6. **Reduceri pentru jocuri**: Sistemul trebuie sa permita aplicarea de reduceri pentru jocuri, astfel incat clientii sa poata beneficia de preturi speciale pentru o perioada limitata.

7. **Statusul jocurilor**: Jocurile trebuie sa aiba un status clar (ANUNTAT, PUBLICAT, DELISTAT) si sa fie filtrabile in functie de acesta.

8. **Securitate si autorizare**: Sistemul trebuie sa implementeze mecanisme JWT pentru autentificare si sa asigure ca fiecare utilizator poate accesa doar functionalitatile corespunzatoare rolului sau.

9. **Validarea datelor**: Toate datele introduce in system (jocuri, utilizatori, contracte) trebuie validate pentru a asigura integritatea si consistenta bazei de date.

10. **Documentarea API-ului**: Toate endpoint-urile REST trebuie documentate folosind Swagger/OpenAPI pentru a facilita integrarea si utilizarea sistemului de catre dezvoltatori si clienti.

### 2. 5 Functionalitati principale pentru faza MVP (Minimum Viable Product)

#### Feature 1: Autentificare si Gestionare Utilizatori

**Descriere:** Sistemul ofera un mechanism complete de inregistrare si autentificare pentru diferite tipuri de utilizatori (clienti, furnizori, administratori). Utilizatorii se pot inregistra cu informatii personale si credentiale, apoi se pot autentifica pentru a primi un token JWT care le permite accesul la functionalitatile specifice rolului lor.

**Motivatie business:** Aceasta functionalitate este fundamentala pentru orice sistem modern, permitand identificarea unica a utilizatorilor si controlul accesului bazat pe roluri. Fara un sistem robust de autentificare, nu se pot implementa celelalte functionalitati in siguranta.

#### Feature 2: Managementul jocurilor de catre dezvoltatori

**Descriere:** Dezvoltatorii pot anunta jocuri noi, pot actualiza titluri si pot schimba status-ul jocurilor. Contractele asociate jocurilor sunt vizibile si pot fi gestionate in functie de status.

**Motivatie business:** Permite dezvoltatorilor sa gestioneze si sa publice continut, ceea ce este esential pentru ecosistemul marketplace-ului.

#### Feature 3: Contracte intre Developer si Publisher

**Descriere:** Publisherii pot crea si actualiza contracte pentru jocuri. Contractele contin detalii despre drepturi si venituri.

**Motivatie business:** Aceasta formalizeaza relatiile comerciale intre dezvoltatori si publisheri si asigura ca jocurile sunt publicate corect si conform regulilor platformei.

#### Feature 4: Marketplace jocuri

**Descriere:** Clientii pot naviga prin catalogul de jocuri disponibile titlu si pot vizualiza detalii despre fiecare joc. Jocurile publicate sunt afisate in marketplace.

**Motivatie business:** Aceasta este functionalitatea principala pentru clienti si genereaza fluxul de venituri.


#### Feature 5: Biblioteca personala

**Descriere:** Jocurile achizitionate sunt adaugate in biblioteca personala, unde pot fi vizualizate si gestionate.

**Motivatie business:** Accesul clientilor la jocurile cumparate reprezinta o functionalitate vitala.

## II. The project should consist of a Spring Boot Application containing:

1. REST endpoints for all the features defined for the MVP. You should define at least 5 endpoints.
2. Beans for defining services (implementing business logic). One service per feature.
3. Beans for defining repositories. One repository per entity.
4. Write unit tests for all REST endpoints and services and make sure all passed.
5. The data within the application should be persisted in a database. Define at least 6 entities that will be persisted in the database database, and at least 4 relations between them.
6. You should validate the POJO classes. You can use the existing validation constraints or create your own annotations if you need a custom constraint.
7. Document the functionalities in the application such that anyone can use it after reading the document. Every API will be documented by Swagger. You can also export the visual documentation and add it to your main document presentation.
8. The functionality of the application will be demonstrated using Postman or GUI (Thymeleaf, Angular, etc).
