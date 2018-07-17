DESCRIPTION = "Tinkerforge Brickd USB daemon"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://license.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "pkgconfig-native libusb1 udev pm-utils"
RDEPENDS_${PN} = "lsbinitscripts libusb1 udev pm-utils"

SRC_URI = "git://github.com/Tinkerforge/brickd.git;rev=8f1fe4f373e60c01dd93ee00de4c7f7ab7a7e921 \
           git://github.com/Tinkerforge/daemonlib.git;rev=d1ffbed6bb7ab420cec9b173767fb6fd097fca80;destsuffix=git/src/daemonlib"

S = "${WORKDIR}/git"

inherit update-rc.d

INITSCRIPT_NAME = "brickd"
INITSCRIPT_PARAMS = "defaults"

EXTRA_OEMAKE = "\
    all -C ${S}/src/brickd/ \
"

POWER_HOOKS="${@os.popen('pkg-config pm-utils --variable pm_powerhooks').read()}"
SLEEP_HOOKS="${@os.popen('pkg-config pm-utils --variable pm_sleephooks').read()}"

do_compile () {
    oe_runmake
}

do_install () {
    install -d -m 0755 ${D}${bindir}  
    install -d -m 0755 ${D}${sysconfdir}
    install -d -m 0755 ${D}${sysconfdir}/init.d
    install -d -m 0755 ${D}${datadir}
    install -d -m 0755 ${D}${datadir}/man/man5
    install -d -m 0755 ${D}${datadir}/man/man8
    install -d ${D}${POWER_HOOKS} 
    install -d ${D}${SLEEP_HOOKS}
    
    install -m 0755 ${S}/src/brickd/brickd ${D}${bindir}
    
    install -m 0755 ${S}/src/build_data/linux/installer/usr/lib/pm-utils/power.d/42brickd ${D}${POWER_HOOKS}
    install -m 0755 ${S}/src/build_data/linux/installer/usr/lib/pm-utils/sleep.d/42brickd ${D}${SLEEP_HOOKS}  
    
    install -m 0755 ${S}/src/build_data/linux/installer/usr/share/brickd/brickd-sysv-init ${D}${sysconfdir}/init.d/brickd
    install -m 0644 ${S}/src/build_data/linux/installer/etc/brickd-default.conf ${D}${sysconfdir}/brickd.conf
    install -m 0644 ${S}/src/build_data/linux/installer/etc/logrotate.d/brickd ${D}${sysconfdir}/logrotate.d
    
    install -m 0644 ${S}/src/build_data/linux/installer/usr/share/man/man5/brickd.conf.5 ${D}${datadir}/man/man5
    gzip -n -f ${D}${datadir}/man/man5/brickd.conf.5
    
    install -m 0644 ${S}/src/build_data/linux/installer/usr/share/man/man8/brickd.8 ${D}${datadir}/man/man8
    gzip -n -f ${D}${datadir}/man/man8/brickd.8
}

FILES_${PN} += "\
    ${bindir} \
    ${sysconfdir} \
    ${datadir} \
    ${POWER_HOOKS} \
    ${SLEEP_HOOKS} \
"
