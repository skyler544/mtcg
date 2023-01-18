all: compile uml

run:
	java -jar target/MTCG.jar

curl-script:
	documentation/MTCG-Test.sh

compile:
	mvn clean compile assembly:single

uml: uml-directory
	plantuml -theme carbon-gray target/uml/*.puml

view-uml:
	xdg-open target/uml/*.png

uml-directory:
	mkdir target/uml

clean:
	mvn clean
