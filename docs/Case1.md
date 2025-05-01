## 1. Wymagania funkcjonalne i niefunkcjonalne

### Funkcjonalne:

- Policjant może wystawiać mandat (po podaniu numeru służbowego).
- Kierowca może przeglądać mandaty i sumę punktów karnych (po podaniu numeru PESEL).
- Możliwość anulowania mandatu.
- Uwzględnianie recydywy (niektóre wykroczenia mają podwójną wagę).
- Powiązanie mandatu z kierowcą i policjantem.
- Dane kierowcy i policjanta pobierane z dokumentów (prawo jazdy, legitymacja).

### Niefunkcjonalne:

- System powinien być dostępny 24/7.
- Wersja dla policjanta to aplikacja desktopowa (JavaFX).
- Wersja dla kierowcy to aplikacja webowa responsywna (RWD).
- Bezpieczeństwo danych (uwierzytelnienie po numerze służbowym lub PESEL).
- Skalowalność bazy danych i łatwa rozbudowa.

---

## 2. Tabela przypadków użycia

### Przypadek użycia 1

| Numer | Nazwa               | Aktorzy  | Warunki wejściowe                             | Warunki wyjściowe        | Przebieg główny                                                                 |
|-------|---------------------|----------|------------------------------------------------|---------------------------|----------------------------------------------------------------------------------|
| 1     | Wystawienie mandatu | Policjant| Posiadanie dokumentu kierowcy, numer służbowy | Aktualizacja bazy mandatów| Wejście do aplikacji → wpisanie numeru służbowego → ID kierowcy → formularz → zatwierdzenie |

### Przypadek użycia 2

| Numer | Nazwa                         | Aktorzy  | Warunki wejściowe | Warunki wyjściowe | Przebieg główny                                                     |
|-------|-------------------------------|----------|-------------------|-------------------|----------------------------------------------------------------------|
| 2     | Podgląd wykroczeń i punktów   | Kierowca | Posiadanie PESEL  | -                 | Wejście na stronę → wpisanie PESEL → zakładka wykroczeń i punktów   |

---

## 3. Punkty karne za przekroczenie prędkości (2025)

- Przekroczenie prędkości do 10 km/h – **1 punkt karny**
- Przekroczenie prędkości o 20 km/h – **3 punkty karne**
- Przekroczenie prędkości o 30 km/h – **7 punktów karnych**
- Przekroczenie prędkości o 40 km/h – **9 punktów karnych**
- Przekroczenie prędkości o 50 km/h – **11 punktów karnych**
- Przekroczenie prędkości o 60 km/h – **13 punktów karnych**
- Przekroczenie prędkości o 70 km/h i więcej – **15 punktów karnych**

---

## 4. Kary finansowe za przekroczenie prędkości (2025)

- Do 10 km/h – **50 zł**
- 11–15 km/h – **100 zł**
- 16–20 km/h – **200 zł**
- 21–25 km/h – **300 zł**
- 26–30 km/h – **400 zł**
- 31–40 km/h – **800 zł / 1600 zł** (recydywa)
- 41–50 km/h – **1000 zł / 2000 zł** (recydywa)
- 51–60 km/h – **1500 zł / 3000 zł** (recydywa)
- 61–70 km/h – **2000 zł / 4000 zł** (recydywa)
- 71 km/h i więcej – **2500 zł / 5000 zł** (recydywa)

---

## 5. Projekt bazy danych (przykładowe tabele)

- `policjanci`
- `kierowcy`
- `mandaty`
- `wykroczenia`
- `punkty_karne`
- `kary_finansowe`

Przykładowy wpis:
- **Niekorzystanie z pasów bezpieczeństwa podczas jazdy** – mandat **100 zł**

---

## Technologie

- **Aplikacja desktopowa** (dla policjanta): JavaFX
- **Aplikacja webowa (RWD)** (dla kierowcy): HTML + CSS + JS
- **Backend i baza danych**: dowolne technologie zgodne z wymogami (np. Spring Boot + MySQL)