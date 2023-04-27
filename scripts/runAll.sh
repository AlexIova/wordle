#!/bin/bash

gnome-terminal -- bash -c './runServer.sh; exec bash'
sleep 1
gnome-terminal -- bash -c './runClient.sh; exec bash'