all: compile

run:
	java -jar target/MTCG.jar

curl-script:
	documentation/MTCG-Test.sh

compile:
	mvn clean package compile assembly:single

view-uml: uml
	xdg-open target/generated-docs/mtcg.png

uml:
	plantuml -theme carbon-gray target/generated-docs/mtcg.puml

clean:
	mvn clean
