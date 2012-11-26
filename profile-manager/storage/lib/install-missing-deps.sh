#!/bin/bash
# TODO move to parent directory
mvn install:install-file -DgroupId=com.openlink.virtuoso -DartifactId=sesame -Dversion=2.0 -Dpackaging=jar -Dfile=./virt_sesame2.jar
mvn install:install-file -DgroupId=com.openlink.virtuoso -DartifactId=virtuoso-jdbc-3 -Dversion=3.0 -Dpackaging=jar -Dfile=./virtjdbc3.jar