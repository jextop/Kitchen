#!/bin/bash

# package
./package.sh

# launch
java -Dfile.encoding=utf-8 -jar ./target/track-0.0.1-SNAPSHOT.jar
