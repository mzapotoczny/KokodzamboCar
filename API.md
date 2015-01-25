Api
---
Autoryzacja: OAuth (1/2?)

| URL | Metoda | Format | Dodatkowe info |
|---|---|---|---|
| /sessions   | GET    | [ {id:INT,name:STRING,created_at:DATE},... ]  | Zwraca wszystkie sesje aktualnie zalogowanego użytkownika  |
| /sessions   | POST   | {name:STRING}  | Tworzy nową sesje (pole created_at i id nadaje serwer)  |
| /sessions/X | DELETE | - | Usuwa sesję o id = X |
| /sessions/X | POST   | [ { pid_id:INT, value:STRING } ]  | Dodaje nowe pomiary do sesji. Ilość pomiarów w jednym zapytaniu będzie ograniczona do jakieś sensowej liczby. Value w stringu dlatego, ze mamy różne jednostki i tak chyba będzie najprościej. |

UWAGA!! Zmiany: Zamieniono ostatni url z /session/X -> /sessions/X; Usunięto time_diff z tego samego zapytania

Do tego jakieś standardowe tworzenie użytkownika.

Pid
---
pid_id jest numerem komendy. Aktualne numery można wyczytać z tego kodu
(później mogę zrobić tabelkę jeszcze z wartośćiami jakie zwracają te pidy)

``` java
    private static void loadIds(){
        // Engine
        mCommandsIds.put(OBDEngineLoadCommand.class, 1);
        mCommandsIds.put(OBDEngineRuntimeCommand.class, 2);
        mCommandsIds.put(OBDMAFCommand.class, 3);
        mCommandsIds.put(OBDRPMCommand.class, 4);
        mCommandsIds.put(OBDThrottlePositionCommand.class, 5);
        // Proto
        mCommandsIds.put(OBDAutoProtocolCommand.class, 20);
        mCommandsIds.put(OBDEchoOffCommand.class, 21);
        mCommandsIds.put(OBDLineFeedOffCommand.class, 22);
        mCommandsIds.put(OBDResetCommand.class, 23);
        mCommandsIds.put(OBDSupportedPIDsCommand.class, 24);
        // Temperature
        mCommandsIds.put(OBDAirIntakeTemperatureCommand.class, 40);
        mCommandsIds.put(OBDAmbientAirTemperature.class, 41);
        mCommandsIds.put(OBDEngineCoolantTemperature.class, 42);

        mIdsLoaded = true;
    }
```
