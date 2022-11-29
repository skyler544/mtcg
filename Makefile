all: package uml

server:
	java -cp target/MTCG.jar org.mtcg.Server

user:
	java -cp target/MTCG.jar org.mtcg.User

package:
	mvn package

uml:
	plantuml -theme carbon-gray target/uml/*.puml

view-uml:
	xdg-open target/uml/*.png

run:
	java -jar target/MTCG.jar

help:
	echo "Call make with one of the following targets:\n" && cat Makefile

clean:
	mvn clean
