# Proiect Energy System Etapa 2

## Despre

Proiectare Orientata pe Obiecte, Seriile CA, CD
2020-2021

Student: Tanase Razvan-Andrei 321CAb

### Entitati

Voi face o scurta prezentare a pachetelor si claselor continute de acestea:
    * common -> Constants:  se regasesc toate constantele de tip string folosite
                            in cod
                Utils:  clasa ce contine metode care imi folosesc la parsarea
                        update-urilor lunare

    * consumer ->   Consumer: aceasta clasa contine detaliile de baza ale unui
                              consumator primite in urma parsarii fisierelor de
                              input cat si informatii noi folositoare pentru
                              starea jocului
                    Blacklist:  o clasa pe care un consumator o are drept camp;
                                ofera informatii despre situatia financiara a
                                consumatorului mai exact despre platile restante

    * databases ->  ConsumersDB: contine o lista a consumatorilor care inca isi
                                 pot plati ratele
                    DistributorsDB: contine o lista a distribuitorilor care inca
                                    isi pot plati ratele
                    ProducersDB: contine o lista cu toti producatorii si metode
                                 care le modifica starea
                    Accountant: un "secretar" care tine evidenta distribuitorilor
                                la care trebuie sa plateasca fiecare consumator
                                si a platitorilor de taxe care au falimentat
    * input ->  Input: contine toate informatiile preluate din parsarea
                        fisierelor de test
                InputLoader: parseaza fisierele de test

    * distributors ->   Subscriber: interfata asemanatoare clasei Observer; este
                                    folosita pentru observer design pattern
                        Distributor: aceasta clasa contine detaliile de baza
                                      ale unui distribuitor primite in urma
                                      parsarii fisierelor de input cat si 
                                      informatii noi folositoare pentru starea
                                      jocului; Observatorul jocului
                        Contracts: entitatile care leaga un consumator de un
                                   distribuitor
    * entities ->   EnergyType: Enum-ul cu toate tipurile de energii din joc;
                                aflat in schelet

    * output -> OutputLoader: contine o lista cu toate obiectele ale caror
                              detalii vor aparea in fisierele de output
                Consumers, Distributors, EnergyProducers: contin doar detaliile
                                                relevante pentru entitatea
                                                respectiva la finalul jocului
    * producer ->   MonthlyStats: contine informatii despre statistica lunara a
                                unui producator si de aceea este si camp pentru
                                aceasta clasa; trebuie afisat la ouput
                    Producer: aceasta clasa contine detaliile de baza ale unui
                              producator primite in urma parsarii fisierelor de
                              input cat si informatii noi folositoare pentru
                              starea jocului; Observabilul jocului
                    Provider: clasa mostenita de Producer, este o implementare
                              proprie a clasei Observable si contine lista de
                              distribuitorilor abonati la un producator.
    * strategies -> EnergyChoiceStrategyType: clasa care face parte din schelet
                                              contine toate tipurile de strategii
                    Green/Price/QuantityStrategy: strategiile concrete implementate
                                                prin sortarea listei de producatori
                    MarketStrategy: interfata pe care o extind strategiile concrete;
                                    contine declararea metode "execute"
                    StrategyFactory: clasa Singleton care genereaza, folosind 
                                     factory design pattern, toate strategiile
                                     pe care le asigneaza imediat dupa parsare la 
                                     initializarea distribuitorilor.

### Flow
In linii mari, flow-ul codului este acelasi fata de etapa anterioara, singurele 
exceptii facandu-le actualizarea producatorilor si aplicarea strategiei de catre
distribuitori.

Simularea respecta pasii indicatii pe ocw, astfel incat:
    * Runda 0:
        * se incarca datele initiale, in urma parsarii, despre participantii la
        simulare si se instantiaza bazele de date;
        * distribuitorii isi cauta producatori pentru a le furniza energie (isi
        aplica strategia) si isi calculeaza costul de productie;
        * distribuitorii isi vor calcula primele contracte tinand cond ca inca
        nu au niciun consumator abonat. De asemenea profitul este si el calculat
        tot atunci;
        * deoarece contractele sunt deja calculate, vine randul consumatorilor
        care trebuie sa semneze cu cel mai accesibil distribuitor, acesta fiind
        gasit cu ajutorul unei sortari in lista de distribuitori pe baza valorii
        contractului. (Aceasta metoda va ramane valabila pe tot parcursul jocului);
        * dupa semnarea contractelor, fiecare entitate trebuie sa-si plateasca
        taxele; in momentul in care un consumator plateste rata lunara, banii
        ajung direct la distribuitorul abonat iar lunile de plata ramase vor fi
        reduse cu 1. (Daca se afla in imposibilitatea de a plati, el va fi gratiat
        o luna, bugetul lui nu va fi afectat, distribuitorul nu isi va primi
        banii, dar tot i se va scadea o luna de pe contract);
    
    * Runda normala
        * se obtin informatiile care vin lunar despre noii consumatori sau despre
        modificarea datelor distribuitorilor;
        * la inceputul fiecarei runde se calculeaza pretul noilor contracte pe
        baza consumatorilor abonati si a costurilor schimbate; pret pe care il
        vor plati consumatorii care nu au contract incheiat deja;
        * se elimina contractele care au ajuns la final;
        * consumatorii fara contracte isi vor incheia unul cu cel mai aceesibil
        distribuitor;
        * fiecare consumator si distribuitor va plati taxele aferente sfarsitului
        de luna, iar cei in imposibilitate de a face acest lucru, vor fi gratiati
        (in cazul consumatorilor) o luna, sau declarati faliti;
        * eliminarea celor care au dat faliment si adaugarea lor intr-o lista
        separata deoarece sunt considerati ca fiind in afara jocului.

    Noutati fata de etapa 1:
        * se actualizeaza la finalul lunii detaliile despre producatori; dupa
        aparitia acestor modificari, distribuitorii sunt imediat notificati, iar
        acestia isi vor seta costul de productie pe 0 pentru a putea fi
        identificati mai usor.
        * distribuitorii care au costul de productie 0 se vor dezabona de la 
        fiecare producator in lista carora figureaza si isi va reaplica strategia
        de cautare a unui furnizor de energie. 
        * se actualizeaza statistica lunara a fiecarui producator pe baza listei
        de abonati.

### Design patterns

Cele mai importante design pattern-uri folosite in cod sunt si cele recomandate:

Observer Pattern:
* Ofera mecanismul de notificare dintre producatori si distribuitori;
* Observabilul este producatorul si Observatorul este distribuitorul;
* Pentru ca interfața Observer și clasa Observable nu-mi ofereau, din punctul meu
de vedere, suficient control asupra flow-ului programului, mi-am implementat eu
o interfata si o clasa separata care pastreaza, in lini mari, aceleasi
functionalitati;  
* Clasa Provider care inlocuieste Observable, este extinsa de clasa Producer si
pastreaza o lista de distribuitori care sunt abonati la un producator intr-un
moment de timp si contine metode de abonare, dezabonare cat si de notificare;
* Interfața Subscriber inlocuieste Observer, este extinsa de clasa Distributor
si descrie metoda de update.

Strategy Pattern:
* Permite distribuitorilor sa-si aleaga un anumit tip de producatori in functie
de strategia cu care este instantiat;
* Am creat o interfata, MarketStrategy, ce defineste metoda de "execute", 
necesara acestui design pattern. Ea este extinsa de trei strategii concrete si
singurele din simularea jocului;
* Strategiile concrete sunt un camp in interiorul clasei Distributor si sunt
alese la runtime.

Factory Pattern:
* Necesar in prima parte a proiectului, l-am reintrodus sub o alta forma, mai
putin fortata, in etapa 2.
* Se afla in legatura cu Strategy Pattern deoarece imi intoarce un obiect care
reprezinta o strategie concreta, specificata de un String obtinut in urma parsarii;
* Clasa care implementeaza aceasta metoda este de tip Singleton deoarece nu-si
modifica starea.

Singleton Pattern:
* Necesar tot in prima parte a proiectului, mergea foarte bine cu design-ul de 
Factory.
 
### Dificultati intalnite, limitari, probleme

Problema cea mai des intalnita si care m-a fortat sa schimb putin logica initala
a programului a fost exceptia de tip ConcurrentModificationException. Un caz
concret este atunci cand un producator se actualiza, trebuia sa notifice
distribuitorii, iar acestia trebuiau sa foloseasca metoda "update". La inceput,
am gandit ca in metoda "update", distribuitorul sa se dezaboneze de la toti 
producatorii care figurau pe lista lui, dar asta insemna sa le dau remove ceea
ce era imposibil din cauza faptului ca eu incercam sa sterg dintr-o lista prin
care se plimba un iterator. Deoarece acest mecanism de notificare se baza pe 
inlantuirea apelurilor mai multor metode, nu puteam rezolva problema. De aceea,
am decis sa fac aceasta dezabonare in functie de "execute" a strategiei.