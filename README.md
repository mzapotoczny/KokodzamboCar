Założenia projektu
---------------------
	- rejestracja danych samochodu, gps i wysłanie ich na serwer
	- wyświetlanie na serwerze w wygodnej formie tych danych, statystyki

Jak to działa?
--------------------
	- Użytkownik zakłada konto w którym podaje login, hasło, nazwę konta (samochodu), i ew. dane potrzebne przy obliczeniach (jak pojemność silnika, masa)
	- Po założeniu konta można się zalogować w aplikacji
	- Rejestrowanie podzielone na sesje
		- Sesja rozpoczyna się po kliknięciu start i zainicjowaniu wszystkich systemów (podłączenie się do obdII; gps)
		- Sesja kończy się po kliknięciu na stop ew. program kończy poprzednią sesję jeśli nie została zamknięta
	- Rejestrowana jest tylko data rozpoczęcia i zakończenia sesji (ew. diff od początku sesji?)
	- Różne pidy mogą być rejestrowane z różną częstotliwością
	- Dane są trzymane w pamięci, po zapełnieniu się cache - zrzucane na dysk
	- Jeśli jest połączenie z internetem dane są co jakiś czas wysyłane na serwer
