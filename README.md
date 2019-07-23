# bimspot IUCN red list exercise

## build 

mvn install

## default run 

java -jar target/bimspot-1.0-SNAPSHOT-jar-with-dependencies.jar

## help and options

java -jar target/bimspot-1.0-SNAPSHOT-jar-with-dependencies.jar --help

--token allows for a custom IUCN red list api token (default is the token used in the examples on http://apiv3.iucnredlist.org/api/v3/docs)

--timeout allows for a custom timout in milliseconds for single requests (default is 2000)

usage: java -jar target/bimspot-1.0-SNAPSHOT-jar-with-dependencies.jar --timeout 1000 --token <mytoken>
