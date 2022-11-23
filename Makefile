all: package uml

package:
	mvn package

uml:
	plantuml -theme carbon-gray target/uml/*.puml

view-uml:
	xdg-open target/uml/*.png

run:
	java -jar target/*.jar

clean:
	mvn clean
