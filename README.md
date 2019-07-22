# bimspot IUCN red list exercise

## build 

mvn install

## run 

java -jar target/bimspot-1.0-SNAPSHOT-jar-with-dependencies.jar

## help 

java -jar target/bimspot-1.0-SNAPSHOT-jar-with-dependencies.jar --help

allows for a custom IUCN red list api token via the --token switch

allows for a custom timout in milliseconds via the --timeout switch

usage: java -jar target/bimspot-1.0-SNAPSHOT-jar-with-dependencies.jar --timeout 1000 --token mytoken
