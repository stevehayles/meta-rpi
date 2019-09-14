DESCRIPTION = "Quadra application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9fc642ff452b28d62ab19b7eea50dfb9"
PV = "1.0.0"
PR = "r0"

RPROVIDES_${PN} = "quadra"
DEPENDS = "zlib"
RDEPENDS_${PN} = "dotnetcore-64"

PACKAGECONFIG_pn-curl = 'zlib ipv6 ssl'

BB_STRICT_CHECKSUM = "0"
SRC_URI = "\
    file://LICENSE.txt \ 
    file://quadra.tar.gz \    
    file://quadra.service \   
"

S = "${WORKDIR}"

PACKAGES = "${PN}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

python do_install () {
    bb.build.exec_func("shell_do_install", d)
}

shell_do_install() {
    # Create the application directory and install the binary application files
	install -d ${D}/opt/quadra
	cp -R --no-dereference --preserve=mode,links -v ${S}/publish/. ${D}/opt/quadra

    # Install the service unit files and create the symlinks to activate them
    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/quadra.service ${D}/${systemd_unitdir}/system 

    install -d ${D}/${sysconfdir}/systemd/system/basic.target.wants
    ln -s -r "${D}/${systemd_unitdir}/system/quadra.service" "${D}/${sysconfdir}/systemd/system/basic.target.wants/quadra.service"
}

FILES_${PN} = "\
	/opt/quadra \
    ${systemd_unitdir}/system/quadra.service \
    ${sysconfdir}/systemd/system/basic.target.wants/quadra.service \
"

INSANE_SKIP_${PN}_append = "already-stripped"
