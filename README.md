# Car Rent v1

## About

A simple Java GUI application to manage a vehicle rent agency.

## Requirements

This project requires at least JDK 8 to build and run.
You will need Maven to build the project : find it [here](https://maven.apache.org/download.cgi).

## Dependencies

This project relies on Maven to build, and Jackson to load/save data from and to files on disk.
Maven manages dependencies by itself, so you just need to type `mvn package`.

The resulting executable JAR file will be located in `targets`.

## Binaries

A prebuilt JAR file is provided, and located in `jar`.

## Sample database files

Sample database files are provided, and located in `examples`.

## Usage

One can open a database file directly on startup by providing said file as a command line argument, e.g.:

```java -jar CarRent-1.0-SNAPSHOT.jar database.xml```

The main interface is divided in tabs, one for each type of data to manage.
Vehicles are special: they are divided in each sort of vehicle in another tabbed view.

One each tab, the user can right-click anywhere and it will trigger a popup menu 
(only on the `Clients` and `Contracts` tabs for now). 

The options are :
- `Add` to open an input dialog to create a new entry
- `Copy` to copy the data from the selected cell (*only when clicking on a cell*)
- `Remove` to delete the selected entry (*only when clicking a row*)

The menu bar can be used to do the usual :
- ``File``
    - `Open` (shortcut `Ctrl+O`), to open an existing database file.
    - `Save` (shortcut `Ctrl+S`), to save the current database file.
    - `Save As` (shortcut `Ctrl+Alt+S`), to save the working database to a new file.
    - `Quit` (shortcut `Ctrl+Q`), to exit safely from the application.
- ``Edit``
    - `Find` (shortcut `Ctrl+F`), to find particular data
    - `Lock/Unlock` (shortcut `Ctrl+L`), to enable/disable edition in tables

## TODO list

- Find functionality
    - Backend: ready
    - Table models: ready
    - Dialogs: not ready
- Drag'n'Drop to load database file
- Automatic price computation for new contracts 