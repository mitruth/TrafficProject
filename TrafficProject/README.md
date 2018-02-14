# RicaMaps
//DAN
din folderul de server client ai de modificat codul de server. Il faci sa parseze un fisier in care ai toate entryurile de trasee din fiecare punct catre fiecare punct la fiecare ora din zi si sa normalizezi datele alea. serverul cand este pornit ruleaza pana primeste ceva comanda de stop si asteapta de la un client un string de forma "x1 y1 x2 y2 time" (x1,y1 coord start x2,y2 coord end), cauta binar in datele parsate si salvate in memorie (ma intrebi daca nu te descurci cu structurile de date) si trimite clientului ca raspuns stringul de traseu de la start la end calculat pentru ora respectiva.

//SICA
in folderul de data processing ai polylines.txt. acolo ai fiecare strada definita ca si un set de coordonate. nu te intereseaza acele coordonate. trebuie sa scrii un cod care parseaza fisierul ala, ia de acolo doar numele(restul datelor le ignora) si genereaza pentru fiecare nume de strada 24 de procentaje (procentul din 50 km/h cu care se circula pe respectivul segment de drum in ora respectiva) primul e procentul pentru 00-01 urmatorul e 01-02 si asa mai departe. pune-le sa fie cat de cat realiste (nu chiar random). 
format fisier:
fabricii
100
100
100
100
100
100
50
20
50
70
70
70
80
80
60
40
30
30
40
50
60
70
90
100
alexandru vaida voevod 1
100
100
...
...
etc
(scrii codul in ce vrei tu si let me know daca ceva te bate)
