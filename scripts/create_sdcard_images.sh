#!/bin/bash

DSTDIR=~/rpi/upload
IMG=quadra-dev-64
IMG_LONG="${IMG}-${MACHINE}"

if [ ! -d ${DSTDIR} ]; then
    mkdir ${DSTDIR}

    if [ $? -ne 0 ]; then
        echo "Failed to create $DSTDIR"
        exit 1
    fi
fi

if [ -z "${IMG}" ]; then
    IMG=console
fi

if [ -z "${MACHINE}" ]; then
    if [ -f ../../build/conf/local.conf ]; then
        export MACHINE=$(grep '^MACHINE =' ../../build/conf/local.conf | cut -d'=' -f2 | tr -d '"' | tr -d ' ')
        echo "Using MACHINE from local.conf: $MACHINE"
    fi
fi

IMG_LONG="${IMG}-${MACHINE}"

if [ ! -d /media/card ]; then
        echo "Temporary mount point [/media/card] not found"
        exit 1
fi

if [ "x${1}" = "x" ]; then
    CARDSIZE=2
else
	if [ "${1}" -eq 2 ]; then
		CARDSIZE=2
	elif [ "${1}" -eq 4 ]; then
		CARDSIZE=4
	elif [ "${1}" -eq 8 ]; then
		CARDSIZE=8
	else
		echo "Unsupported card size: ${1}"
		exit 1
	fi
fi

if [ -z "$OETMP" ]; then
    # echo try to find it
    if [ -f ../../build/conf/local.conf ]; then
        OETMP=$(grep '^TMPDIR' ../../build/conf/local.conf | awk '{ print $3 }' | sed 's/"//g')
    fi

    if [ -z "$OETMP" ]; then
        if [ -d "../../build/tmp" ]; then
            OETMP="../../build/tmp"
        fi
    fi
fi

if [ -z "${OETMP}" ]; then
    echo "OETMP environment variable not set"
    exit 1
fi

if [ -z "${MACHINE}" ]; then
    echo "MACHINE environment variable not set"
    exit 1
fi

HOSTNAME=${MACHINE}

SRCDIR=${OETMP}/deploy/images/${MACHINE}

if [ ! -f "${SRCDIR}/${IMG_LONG}.tar.xz" ]; then
    echo "File not found: ${SRCDIR}/${IMG_LONG}.tar.xz"
    exit 1
fi

SDIMG=${IMG}-${MACHINE}-${CARDSIZE}gb.img

if [ -f "${DSTDIR}/${SDIMG}" ]; then
        rm ${DSTDIR}/${SDIMG}
fi

if [ -f "${DSTDIR}/${SDIMG}.xz" ]; then
        rm -f ${DSTDIR}/${SDIMG}.xz*
fi

echo -e "\n***** Creating the loop device *****"
LOOPDEV=`losetup -f`

echo -e "\n***** Creating an empty SD image file *****"
dd if=/dev/zero of=${DSTDIR}/${SDIMG} bs=1G count=${CARDSIZE}

echo -e "\n***** Partitioning the SD image file *****"
sudo fdisk ${DSTDIR}/${SDIMG} <<END
o
n
p
1

+64M
n
p
2


t
1
c
a
1
w
END

echo -e "\n***** Attaching to the loop device *****"
sudo losetup -P ${LOOPDEV} ${DSTDIR}/${SDIMG}

echo -e "\n***** Copying the boot partition *****"
DEV=${LOOPDEV}p1
./copy_boot.sh ${DEV}

if [ $? -ne 0 ]; then
    sudo losetup -D
    exit
fi

echo -e "\n***** Copying the rootfs *****"
DEV=${LOOPDEV}p2
./copy_rootfs.sh ${DEV} ${IMG} ${HOSTNAME}

if [ $? -ne 0 ]; then
    sudo losetup -D
    exit
fi

echo -e "\n***** Detatching loop device *****"
sudo losetup -D

if [[ -z "${COMPRESS}" ]]; then
  echo -e "\n***** COMPRESS environment variable not set *****"
else
  echo -e "\n***** Compressing the SD card image *****"
  sudo xz -k -9 ${DSTDIR}/${SDIMG}

  echo -e "\n***** Creating an md5sum *****"
  cd ${DSTDIR}
  md5sum ${SDIMG}.xz > ${SDIMG}.xz.md5
fi

cd ${OLDPWD}

if [[ -z "${DROPBOX}" ]]; then
  echo -e "\n***** DROPBOX environment variable not set *****\n"
else
  echo -e "\n***** Uploading image to Dropbox *****\n"
  
  if [[ -z "${COMPRESS}" ]]; then
    LOCAL="${HOME}/rpi/upload/${IMG_LONG}-${CARDSIZE}gb.img"
    REMOTE="/51 North/Projects/Blackfin/${IMG_LONG}-${CARDSIZE}gb_$(date +"%FT%H%M").img"
  else
    LOCAL="${HOME}/rpi/upload/${IMG_LONG}-${CARDSIZE}gb.img.xz"
    REMOTE="/51 North/Projects/Blackfin/${IMG_LONG}-${CARDSIZE}gb_$(date +"%FT%H%M").img.xz"
  fi

  echo -e "File name: ${LOCAL}\nRemote File name: ${REMOTE}\n"

  ./dropbox_uploader.sh -f .dropbox_uploader upload "${LOCAL}" "${REMOTE}"
fi

echo -e "\n***** Done *****\n"
