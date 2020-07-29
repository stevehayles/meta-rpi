SUMMARY = "A basic development image for the Quadra instrument processor"
HOMEPAGE = "http://www.fiftyone-north.com"
LICENSE = "MIT"

IMAGE_FEATURES += "package-management"
IMAGE_LINGUAS = "en-gb"

inherit image

DEPENDS += "bootfiles"

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
    python3-paho-mqtt \
"

QUADRA = " \
    dotnet-sdk-64 \
    brickd \
    quadra \
    pi-bluetooth \
    nodejs \
    docker-ce \
"

WIFI_SUPPORT = " \
    crda \
    iw \
    linux-firmware-rpidistro-bcm43430 \
    linux-firmware-rpidistro-bcm43455 \
    wpa-supplicant \
"

DEV_SDK_INSTALL_MINIMAL = " \
    git \
    make \
    gcc \
    g++ \
    sqlite3 \
"

DEV_SDK_INSTALL = " \
    binutils \
    binutils-symlinks \
    coreutils \
    cpp \
    cpp-symlinks \
    diffutils \
    file \
    g++ \
    g++-symlinks \
    gcc \
    gcc-symlinks \
    gdb \
    gdbserver \
    gettext \
    git \
    ldd \
    libstdc++ \
    libstdc++-dev \
    libtool \
    make \
    pkgconfig \
    python3-modules \
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

RPI_STUFF = " \
    raspi2fb \
    userland \
"

IMAGE_INSTALL += " \
    ${CORE_OS} \
    ${MQTT} \
    ${QUADRA} \
    ${DEV_SDK_INSTALL_MINIMAL} \
    ${DEV_EXTRAS} \
    ${EXTRA_TOOLS_INSTALL} \
    ${RPI_STUFF} \
    ${WIFI_SUPPORT} \
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
    disable_unused_services ; \
 "

export IMAGE_BASENAME = "quadra-dev-64"
