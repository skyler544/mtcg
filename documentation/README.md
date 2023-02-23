# Monster Trading Cards Game

## Building
The supplied Makefile provides a more convenient way of compiling and running
the project than the `mvn` commands.

  1. Running `make` will compile the project. 
  2. Running `make run` will start the server.
  3. Running `make uml` will generate the UML diagram. This requires the
     `plantuml` binary to be installed and available in your `$PATH`.
  4. Running `make view-uml` will open the diagram in your system's default
     image viewer. On platforms other than Linux you will likely need to
     manually open it in your system's image viewer: `[project root]/target/generated-docs/mtcg.png`
  5. Running `make curl-script` will start the curl test script.
  6. Running `make clean` will remove build artifacts.
  

# Clean up tables between runs
DROP TABLE IF EXISTS users, cards, trades, battles, rounds;
