all: compile

run:
	mvn exec:java

curl-script:
	documentation/MTCG-Test.sh

loremaster:
	documentation/lore-master-test.sh

compile: clean package
	mvn compile assembly:single

package:
	mvn package

view-uml: uml
	xdg-open target/generated-docs/mtcg.png

uml:
	export PLANTUML_LIMIT_SIZE=8192 && plantuml -theme carbon-gray target/generated-docs/mtcg.puml

clean:
	mvn clean
