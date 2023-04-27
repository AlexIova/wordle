#! /bin/bash

mkdir ../jars

# Server jar
cd ../WordleServer
make
rm manifest.txt         # Remove previous manifest.txt
echo 'Main-Class: WordleServerMain' >> manifest.txt
echo 'Class-Path: ../gson-2.10.1.jar' >> manifest.txt
jar cvfm WordleServer.jar manifest.txt *.class ../gson-2.10.1.jar
mkdir ../jars/server
mv WordleServer.jar ../jars/server/
cp server.properties ../jars/server/
make clean

# Client jar
cd ../WordleClient
make
rm manifest.txt         # Remove previous manifest.txt
echo 'Main-Class: WordleClientMain' >> manifest.txt
jar cvfm WordleClient.jar manifest.txt *.class
mkdir ../jars/client
mv WordleClient.jar ../jars/client/
cp client.properties ../jars/client/
make clean