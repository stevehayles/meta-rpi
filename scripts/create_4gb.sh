#!/bin/bash

echo -e "\n***** Setting OETMP directory to ~/rpi/build/tmp  *****"
export OETMP=~/rpi/build/tmp

echo -e "\n***** Setting MACHINE raspberrypi3  *****"
export MACHINE=raspberrypi3

echo -e "\n***** Enabling Image Compression *****"
export COMPRESS=1

echo -e "\n***** Enabling Dropbox Upload *****"
export DROPBOX=1

sudo -E ./create_sdcard_images.sh 4
