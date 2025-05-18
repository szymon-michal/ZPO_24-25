### Propozycja pełnej struktury REST API


| Zasób główny              | Endpoint                               | Metody      | Opis / typowe kody odpowiedzi                                                                                                                                    |
| ------------------------- | -------------------------------------- | ----------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Policjanci**            | `/officers`                            | `POST`      | **Rejestracja** lub import policjanta (nr służbowy, imię, nazwisko). <br>201 Created / 409 Conflict                                                              |
|                           | `/officers/{badgeNumber}`              | `GET`       | Dane jednego policjanta (do audytu). 200 / 404                                                                                                                   |
| **Kierowcy**              | `/drivers`                             | `POST`      | Dodanie/aktualizacja kierowcy (PESEL, dane z prawa jazdy). 201 / 409                                                                                             |
|                           | `/drivers/{pesel}`                     | `GET`       | Pełne dane kierowcy wraz z sumą punktów. 200 / 404                                                                                                               |
|                           | `/drivers/{pesel}/points`              | `GET`       | *Lightweight* – tylko aktualna liczba punktów karnych. 200 / 404                                                                                                 |
|                           | `/drivers/{pesel}/tickets`             | `GET`       | Lista mandatów kierowcy (paged, filtr: `status`, `from`, `to`). 200                                                                                              |
| **Wykroczenia (katalog)** | `/violations`                          | `GET`       | Lista dostępnych wykroczeń (z punktami, widełkami cen, flaga recydywy). 200                                                                                      |
|                           |  `/violations/new`                                      | `POST`      | Dodanie nowego rodzaju wykroczenia (tylko admin). 201                                                                                                            |
|                           | `/violations/{id}`                   | `GET`       | Szczegóły jednego wykroczenia. 200 / 404                                                                                                                         |
|                           |         `/violations/{id}`                            | `PUT/PATCH` | Aktualizacja param. wykroczenia. 200                                                                                                                             |
| **Mandaty**               | `/tickets`                             | `POST`      | **Wystawienie mandatu**<br>Payload: `{datetime, location, officerBadge, driverPesel, violations:[{code, isRepeat}], totalAmount}`. <br>Odp.: 201 Created + link. |
|                           | `/tickets/{id}`                        | `GET`       | Szczegóły mandatu (pełne dane kierowcy, policjanta, lista wykroczeń, suma punktów, status). 200 / 404                                                            |
|                           |             `/tickets/{id}`            | `PATCH`     | Aktualizacja statusu (np. `accepted`, `cancelled`). 200 / 400                                                                                                    |
|                           | `/tickets/{id}/cancel`                 | `POST`      | Wygodne anulowanie (alias do powyższego). 200 / 409 if already cancelled                                                                                         |
| **Statystyki / raporty**  | `/stats/officers/{badgeNumber}/issued` | `GET`       | Mandaty wystawione przez danego policjanta (opc. suma kwot/punktów).                                                                                             |
|                           | `/stats/drivers/top-points`            | `GET`       | Kierowcy z największą liczbą punktów (limit param).                                                                                                              |

---

#### Szczegóły dla kluczowych operacji

1. **POST /tickets**

   * Serwer oblicza punkty i kwotę:
     `amount = Σ (repeat? 2×price : price)`; `points = Σ points`.
   * Zapis statusu początkowego `PENDING_ACCEPTANCE`.

2. **Akceptacja mandatu**

   * `PATCH /tickets/{id}` z body `{status:"ACCEPTED"}` albo prostszy:
     `POST /tickets/{id}/accept`.

3. **Anulowanie**

   * Tylko do momentu akceptacji (lub uprawniony supervisor).
   * W przypadku anulacji punkty nie naliczają się kierowcy.

4. **Sumowanie punktów**

   * Na żądanie (`GET /drivers/{pesel}/points`) – serwer liczy *on-demand* lub przechowuje kolumnę `points_total` aktualizowaną triggerem po każdej zmianie statusu mandatu.
                                                           
