all: compile uml

run:
	java -jar target/MTCG.jar

curl-script:
	documentation/MTCG-Test.sh

compile:
	mvn clean package compile assembly:single

uml:
	plantuml -theme carbon-gray target/generated-docs/mtcg.puml

view-uml:
	xdg-open target/generated-docs/mtcg.png

clean:
	mvn clean
