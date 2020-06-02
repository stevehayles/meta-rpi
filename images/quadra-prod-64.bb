SUMMARY = "The production image for the Quadra instrument processor"
HOMEPAGE = "http://www.fiftyone-north.com"
LICENSE = "MIT"

IMAGE_LINGUAS = "en-gb"

inherit image

DEPENDS += "bcm2835-bootfiles"

CORE_OS = " \
    kernel-modules \
    openssh openssh-keygen openssh-sftp-server \
    packagegroup-core-boot \
    term-prompt \
    tzdata \
"

MQTT = " \
    libmosquitto1 \
    libmosquittopp1 \
    mosquitto \
    mosquitto-dev \
    mosquitto-clients \
    python-paho-mqtt \
"

QUADRA = " \
    dotnet-64 \
    brickd \
    quadra \
    pi-bluetooth \
    nodejs \
"

WIFI_SUPPORT = " \
    crda \
    iw \
    linux-firmware-raspbian \
    wpa-supplicant \
"

DEV_SDK_INSTALL_MINIMAL = " \
    git \
    make \
    gcc \
    g++ \
    sqlite3 \
"

DEV_EXTRAS = " \
    ntp \
    ntp-tickadj \
    serialecho  \
    spiloop \
"

EXTRA_TOOLS_INSTALL = " \
    bzip2 \
    devmem2 \
    dosfstools \
    ethtool \
    fbset \
    findutils \
    firewall \
    i2c-tools \
    iperf3 \
    iproute2 \
    iptables \
    less \
    nano \
    netcat \
    procps \
    sysfsutils \
    tcpdump \
    unzip \
    util-linux \
    wget \
    zip \
"

EXTRA_TOOLS_INSTALL_MINIMAL = " \
    util-linux \
"

RPI_STUFF = " \
    raspi2fb \
    userland \
"

IMAGE_INSTALL += " \
    ${CORE_OS} \
    ${QUADRA} \
    ${WIFI_SUPPORT} \
    ${EXTRA_TOOLS_INSTALL} \
    ${RPI_STUFF} \
"

set_local_timezone() {
    ln -sf /usr/share/zoneinfo/UTC ${IMAGE_ROOTFS}/etc/localtime
}

disable_bootlogd() {
    echo BOOTLOGD_ENABLE=no > ${IMAGE_ROOTFS}/etc/default/bootlogd
}

disable_unused_services() {
    rm -f ${IMAGE_ROOTFS}/etc/rc5.d/S15mountnfs.sh
}

ROOTFS_POSTPROCESS_COMMAND += " \
    set_local_timezone ; \
    disable_bootlogd ; \
    disable_unused_services ; \
 "

export IMAGE_BASENAME = "quadra-prod-64"
