all: compile

run:
	mvn exec:java

curl-script:
	documentation/MTCG-Test.sh

compile:
	mvn clean compile assembly:single

view-uml: uml
	xdg-open target/generated-docs/mtcg.png

uml:
	plantuml -theme carbon-gray target/generated-docs/mtcg.puml

clean:
	mvn clean
