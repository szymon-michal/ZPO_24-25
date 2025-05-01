# Projekt 4: Mandaty

Zaimplementuj aplikację symulującą działanie systemu wystawiania mandatów i ich przeglądania.

## Scenariusz

Policjant zatrzymuje kierowcę w określonym czasie i miejscu. Wystawia mandat za pewne wykroczenia (może być więcej niż jedno).  
Wykroczenie ma określoną liczbę punktów karnych oraz widełki cenowe, z tym że niektóre wykroczenia recydywistyczne są wyceniane podwójnie.

Wystawieniu mandatu towarzyszy akceptacja kierowcy. Taki rekord zawiera również dane policjanta i kierowcy. Mandat można anulować.  
Dane kierowcy pochodzą z jego prawa jazdy, a dane policjanta z jego legitymacji służbowej.

## Wymagania funkcjonalne

- **Policjant**:
  - Wystawianie mandatu (dostęp po podaniu numeru służbowego)

- **Kierowca**:
  - Podgląd wykroczeń
  - Podgląd sumy punktów karnych (dostęp po podaniu PESEL)

## Wymagania techniczne

- Przygotuj odpowiednią strukturę obiektową
- Zaproponuj i zaimplementuj odpowiednie relacje *many-to-one* (lub *many-to-many*)  
  (przykład: [Hibernate one-to-many relationship example](http://www.mkyong.com/hibernate/hibernate-one-to-many-relationship-example/))

## Technologie

- **Klient dla policjanta**: aplikacja desktopowa JavaFX  
- **Klient dla kierowcy**: strona RWD do wyświetlenia na telefonie
