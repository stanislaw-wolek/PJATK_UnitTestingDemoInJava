# 2627Z_wis_PRO1aio – testy jednostkowe w Javie

Projekt do prezentacji z testów jednostkowych (JUnit 5, AssertJ, Mockito, Spring Boot). Aplikacja to uproszczony fragment systemu linii lotniczej: **odprawa online** pasażera i **opłata za bagaż rejestrowany**.

Na gałęzi `master` jest kod aplikacji bez testów. Testy będziemy pisać razem na zajęciach. Rozwiązania pojawią się potem na gałęzi `solutions`.

Przed zajęciami proszę wykonać kroki z sekcji [Przygotowanie](#przygotowanie) i sprawdzić, czy test startowy przechodzi.

---

## Spis treści

- [Wymagania](#wymagania)
- [Przygotowanie](#przygotowanie)
  - [Klonowanie w IntelliJ IDEA](#klonowanie-w-intellij-idea)
  - [Klonowanie z terminala](#klonowanie-z-terminala)
  - [Sprawdzenie, czy wszystko działa](#sprawdzenie-czy-wszystko-działa)
- [Uruchomienie aplikacji](#uruchomienie-aplikacji)
- [Architektura projektu](#architektura-projektu)
- [Najczęstsze problemy](#najczęstsze-problemy)

---

## Wymagania

| Narzędzie | Wersja | Uwagi |
|---|---|---|
| JDK | **21** | np. JDK pobrane z poziomu IntelliJ |
| IntelliJ IDEA | dowolna aktualna | wersja Community wystarczy |
| Git | dowolna aktualna | potrzebny do sklonowania repozytorium |

Mavena **nie trzeba instalować**. Projekt zawiera Maven Wrapper (`mvnw` / `mvnw.cmd`), który sam pobierze odpowiednią wersję.

Sprawdzenie wersji Javy w terminalu:

```bash
java -version
```

Wynik powinien zawierać `21`. Jeśli masz inną wersję albo polecenie nie działa, możesz pobrać JDK bezpośrednio w IntelliJ (opis w kroku 5 poniżej).

---

## Przygotowanie

### Klonowanie w IntelliJ IDEA

1. Skopiuj adres repozytorium: zielony przycisk **Code** na GitHubie -> zakładka **HTTPS** -> ikona kopiowania. Adres ma postać:

    ```
    https://github.com/stanislaw-wolek/PJATK_UnitTestingDemoInJava.git
    ```

2. W IntelliJ:
    - na ekranie powitalnym kliknij **Clone Repository** (w starszych wersjach **Get from VCS**),
    - albo przy otwartym projekcie wybierz **File -> New -> Project from Version Control…**.
3. Wklej adres w pole **URL**, wybierz katalog docelowy w polu **Directory** i kliknij **Clone**.
4. Gdy IntelliJ zapyta, czy ufasz projektowi, wybierz **Trust Project**. IntelliJ wykryje plik `pom.xml` i sam zaimportuje projekt Maven. Pierwsze pobieranie zależności może potrwać dłużej.
5. Ustaw JDK 21: **File -> Project Structure… -> Project -> SDK**.
    - Jeśli na liście jest JDK 21, wybierz je.
    - Jeśli nie ma, wybierz **Download JDK…**, wersję **21** i dowolnego dostawcę (np. Eclipse Temurin).

Po imporcie katalog `src/main/java` powinien być oznaczony na niebiesko, a `src/test/java` na zielono.

### Klonowanie z terminala

```bash
git clone https://github.com/stanislaw-wolek/PJATK_UnitTestingDemoInJava.git
cd PJATK_UnitTestingDemoInJava
```

Następnie otwórz katalog `PJATK_UnitTestingDemoInJava` w IntelliJ (**File -> Open…**) i wykonaj kroki 4–5 z poprzedniej sekcji.

### Sprawdzenie, czy wszystko działa

**W IntelliJ:** otwórz `src/test/java/pjwstk/edu/pl/airport/SmokeTest.java` i kliknij zieloną strzałkę obok nazwy klasy. Test powinien przejść (zielony znacznik).

**W terminalu**, w katalogu projektu:

```bash
# Windows (PowerShell / cmd)
.\mvnw.cmd test

# macOS / Linux
./mvnw test
```

Oczekiwany wynik na końcu:

```
[INFO] BUILD SUCCESS
```

Jeśli coś nie działa, zajrzyj do sekcji [Najczęstsze problemy](#najczęstsze-problemy). Jeśli to nie pomoże, napisz do mnie przed zajęciami, spróbuję pomóc.

---

## Uruchomienie aplikacji

Do zajęć **nie trzeba** uruchamiać aplikacji, ale można to zrobić w IDE lub przez:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

Aplikacja nasłuchuje domyślnie na `http://localhost:8080`. Przykładowe rezerwacje i loty są zdefiniowane w klasach `InMemoryBookingRepository` i `InMemoryFlightRepository`.

Przykładowe żądanie odprawy (numer rezerwacji weź z `InMemoryBookingRepository`):
```
POST http://localhost:8080/api/check-ins
content type: JSON
body: {"bookingReference": "<numer rezerwacji>"}
```

---

## Architektura projektu

### Technologie

- Java 21
- Spring Boot 4.2.0 (Spring Web)
- JUnit 5, AssertJ, Mockito, MockMvc
- Maven (przez Maven Wrapper)

Projekt nie używa bazy danych. Dane są trzymane w pamięci, a po restarcie aplikacji wracają do stanu początkowego.

### Struktura katalogów

`src/main/java/pjwstk/edu/pl/airport/`:

- `AirportApplication.java` – klasa startowa Spring Boot
- `domain/` – model domeny (POJO)
    - `Flight.java`
    - `Booking.java`
    - `BoardingPass.java`
    - `exceptions/` – wyjątki domenowe
- `service/` – logika biznesowa
    - `BaggageFeeCalculator.java`
    - `CheckInService.java`
    - `BoardingPassSender.java`
    - `LoggingBoardingPassSender.java`
- `repository/` – dostęp do danych (w pamięci)
    - `BookingRepository.java`
    - `FlightRepository.java`
    - `InMemoryBookingRepository.java`
    - `InMemoryFlightRepository.java`
- `web/` – warstwa HTTP (REST)
    - `CheckInController.java`
    - `CheckInRequest.java`
    - `ErrorHandler.java`

`src/test/java/pjwstk/edu/pl/airport/`:

- `SmokeTest.java` – test sprawdzający środowisko
- `AirportApplicationTests.java` – test uruchamiający całą aplikację


### Jak to się łączy

Kontroler przyjmuje żądanie i przekazuje je do serwisu. Serwis pobiera dane z repozytoriów, sprawdza reguły odprawy i wysyła kartę pokładową. Jeśli reguła nie jest spełniona, serwis rzuca wyjątek domenowy, a `ErrorHandler` zamienia go na odpowiedź HTTP z odpowiednim statusem.

`BaggageFeeCalculator` jest niezależny od reszty: to czysta logika bez zależności.

Wszystkie zależności są przekazywane **przez konstruktor**. Spring tworzy obiekty i łączy je ze sobą, a w testach możemy zrobić to samo ręcznie, podając atrapy (mocki) zamiast prawdziwych implementacji.

### Pakiet `domain`

Zwykłe klasy Javy, POJO.

| Klasa | Opis |
|---|---|
| `Flight` | Lot: numer, czas odlotu (`LocalDateTime`), liczba miejsc i liczba odprawionych pasażerów. Metoda `assignNextSeat()` przydziela kolejny numer miejsca. |
| `Booking` | Rezerwacja: numer rezerwacji, imię i nazwisko pasażera, numer lotu oraz informacja, czy pasażer jest już odprawiony (`isCheckedIn()`, `markCheckedIn()`). |
| `BoardingPass` | Karta pokładowa (`record`): numer rezerwacji, numer lotu, numer miejsca. |

### Pakiet `domain.exceptions`

Wyjątki domenowe dziedziczące po `RuntimeException`.

| Wyjątek | Kiedy jest rzucany |
|---|---|
| `BookingNotFoundException` | nie istnieje rezerwacja o podanym numerze |
| `AlreadyCheckedInException` | pasażer próbuje odprawić się drugi raz |
| `CheckInClosedException` | do odlotu zostało mniej niż 40 minut |
| `BaggageTooHeavyException` | bagaż przekracza limit wagi |

### Pakiet `service`

| Klasa | Opis |
|---|---|
| `BaggageFeeCalculator` | Liczy opłatę za bagaż rejestrowany na podstawie wagi (reguły poniżej). |
| `CheckInService` | Odprawa pasażera: metoda `checkIn(String bookingReference)` zwraca kartę pokładową (reguły poniżej). |
| `BoardingPassSender` | Interfejs wysyłki karty pokładowej. |
| `LoggingBoardingPassSender` | Implementacja, która zamiast wysyłać kartę, wypisuje ją do logu. |

**Opłata za bagaż** (`BaggageFeeCalculator.calculate(int weightKg)`):

| Waga bagażu | Wynik |
|---|---|
| 0 kg lub mniej | `IllegalArgumentException` (niepoprawna waga) |
| poniżej 24 kg | 0 PLN |
| od 24 do 32 kg | 100 PLN |
| powyżej 32 kg | `BaggageTooHeavyException` (bagaż nie zostanie przyjęty) |

**Odprawa** (`CheckInService.checkIn`):

1. Jeśli rezerwacja nie istnieje -> `BookingNotFoundException`.
2. Jeśli pasażer jest już odprawiony -> `AlreadyCheckedInException`.
3. Jeśli do odlotu zostało mniej niż 40 minut -> `CheckInClosedException`. Dokładnie 40 minut przed odlotem odprawa jest jeszcze otwarta.
4. W przeciwnym razie pasażer dostaje kolejny wolny numer miejsca, rezerwacja zostaje oznaczona jako odprawiona, a karta pokładowa zostaje wysłana i zwrócona.

Aktualny czas serwis pobiera ze wstrzykniętego obiektu `Clock`.

### Pakiet `repository`

| Klasa | Opis |
|---|---|
| `BookingRepository` | Interfejs: `findByReference(String)` zwraca `Optional<Booking>`, `save(Booking)`. |
| `FlightRepository` | Interfejs: `findByNumber(String)` zwraca `Optional<Flight>`, `save(Flight)`. |
| `InMemoryBookingRepository` | Implementacja w pamięci z przykładowymi rezerwacjami. |
| `InMemoryFlightRepository` | Implementacja w pamięci z przykładowymi lotami. |

### Pakiet `web`

| Klasa | Opis |
|---|---|
| `CheckInController` | Endpoint `POST /api/check-ins`. |
| `CheckInRequest` | Treść żądania (`record`): `{"bookingReference": "..."}`. |
| `ErrorHandler` | Zamienia wyjątki domenowe na odpowiedzi HTTP w formacie `ProblemDetail`. |

**Odpowiedzi endpointu `POST /api/check-ins`:**

| Sytuacja | Status | Treść |
|---|---|---|
| Odprawa udana | `201 Created` | karta pokładowa: `bookingReference`, `flightNumber`, `seatNumber` |
| Rezerwacja nie istnieje | `404 Not Found` | `ProblemDetail` z komunikatem w polu `detail` |
| Pasażer już odprawiony | `409 Conflict` | `ProblemDetail` |
| Odprawa zamknięta | `409 Conflict` | `ProblemDetail` |

### Testy na gałęzi `master`

| Klasa | Rodzaj | Co sprawdza |
|---|---|---|
| `SmokeTest` | test jednostkowy (JUnit) | czy środowisko i JUnit działają |
| `AirportApplicationTests` | test integracyjny (`@SpringBootTest`) | czy cała aplikacja Spring poprawnie się uruchamia |

Pozostałe testy napiszemy na zajęciach.

---

## Najczęstsze problemy

**`release version 21 not supported` albo `invalid target release: 21`**
Projekt jest uruchamiany na starszej Javie. W IntelliJ ustaw JDK 21 w **File -> Project Structure… -> Project -> SDK**. Jeśli problem występuje w terminalu, zmienna `JAVA_HOME` musi wskazywać na JDK 21.

**IntelliJ nie rozpoznaje projektu (brak kolorowania, czerwone importy, brak zielonych strzałek przy testach)**
Projekt Maven nie został zaimportowany. Kliknij prawym przyciskiem na `pom.xml` -> **Add as Maven Project** albo otwórz panel **Maven** (prawa krawędź okna) i kliknij **Reload All Maven Projects**.

**`./mvnw: Permission denied` (macOS / Linux)**
Nadaj plikowi prawo wykonywania:

```bash
chmod +x mvnw
```

**`No tests found` po kliknięciu zielonej strzałki**
Zwykle oznacza to, że projekt nie został zaimportowany jako Maven (patrz wyżej). Spróbuj też uruchomić testy z terminala poleceniem `.\mvnw.cmd test` / `./mvnw test`.
