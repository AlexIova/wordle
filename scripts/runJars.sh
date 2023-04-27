#!/bin/bash

cd ../jars/server
gnome-terminal -- bash -c 'java -jar ./WordleServer.jar; exec bash'
sleep 1
cd ../client
gnome-terminal -- bash -c 'java -jar ./WordleClient.jar; exec bash'