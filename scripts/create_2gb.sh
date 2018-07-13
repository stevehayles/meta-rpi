#!/bin/bash

echo -e "\n***** Setting OETMP directory to ~/rpi/build/tmp  *****"
export OETMP=~/rpi/build/tmp

echo -e "\n***** Setting MACHINE raspberrypi3  *****"
export MACHINE=raspberrypi3

echo -e "\n***** Enabling Image Compression *****"
export COMPRESS=1

sudo -E ./create_sdcard_images.sh 4

echo -e "\n***** Uploading image to Dropbox *****"

cd ~/rpi/upload/

curl -X POST https://content.dropboxapi.com/2/files/upload \
    --header "Authorization: Bearer GNEuV5Q2I7QAAAAAAABBRYS5Iq5c3zYNNYtjTHrea1MPtV4LGG_vWfSpnudre2_a" \
    --header "Dropbox-API-Arg: {\"path\": \"/51 North/Projects/Quadra/quadra-raspberrypi3-2gb_v1.img.xz\"}" \
    --header "Content-Type: application/octet-stream" \
    --data-binary @quadra-raspberrypi3-2gb.img.xz
