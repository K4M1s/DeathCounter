# DeathCounter

Simple minecraft spigot plugin that allows counting player deaths. Data about the death of players is stored in MySQL database and displayed in sidebar. 

## Requirements

- [Apache Maven](http://maven.apache.org/download.cgi)
- [Java SDK](https://www.oracle.com/pl/java/technologies/javase/javase-jdk8-downloads.html)

## Build

1. Clone the repository.
2. In a terminal cd into repository directory.
3. Run `mvn package`,
4. Move `./target/DeathCounter-xxx.jar` to your `plugins` folder.
5. Start and stop your server.
6. Edit configuration file in `plugins/DeathCounter/config.yml`,
7. Start your server.

## Release History
* 1.0.2
   * Fixed DB connection issues, added CommandTabCompleter, added permissions
   
* 1.0.1
   * Fix for DB SSL Error, Added bStats

* 1.0
    * First build of working plugin
    
## Authors

-   **[K4M1s](https://github.com/K4M1s)** - Creator, main developer

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
