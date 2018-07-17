DESCRIPTION = "Tinkerforge Brickd USB daemon"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://license.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"
DEPENDS = "pkgconfig-native libusb1 udev pm-utils"

# FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI = "git://github.com/Tinkerforge/brickd.git;rev=8f1fe4f373e60c01dd93ee00de4c7f7ab7a7e921 \
           git://github.com/Tinkerforge/daemonlib.git;rev=d1ffbed6bb7ab420cec9b173767fb6fd097fca80;destsuffix=git/src/daemonlib"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "\
    all -C ${S}/src/brickd/ \
    bindir=${D}${bindir} \
    sysconfdir=${D}${sysconfdir} \
    localstatedir=${D}${localstatedir} \
    datadir=${D}${datadir} \
    WITH_PM_UTILS=no \
"

POWER_HOOKS="${@os.popen('pkg-config pm-utils --variable pm_powerhooks').read()}"
SLEEP_HOOKS="${@os.popen('pkg-config pm-utils --variable pm_sleephooks').read()}"

do_compile () {
    oe_runmake
}

do_install () {
    oe_runmake install DESTDIR=${D}
    
    install -d ${D}${POWER_HOOKS}
    install -d ${D}${SLEEP_HOOKS}
    
    install -m 755 ${S}/src/build_data/linux/installer/usr/lib/pm-utils/power.d/42brickd ${D}${POWER_HOOKS}
    install -m 755 ${S}/src/build_data/linux/installer/usr/lib/pm-utils/sleep.d/42brickd ${D}${SLEEP_HOOKS}  
}

do_install_append () {
    rm -rf ${D}/var/run
}

FILES_${PN} += "\
    ${bindir} \
    ${sysconfdir} \
    ${datadir} \
    ${localstatedir} \
    ${POWER_HOOKS} \
    ${SLEEP_HOOKS} \
"
