DESCRIPTION = "64 bit .NET Core Runtime, SDK & CLI tools"
HOMEPAGE = "https://www.microsoft.com/net/core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9fc642ff452b28d62ab19b7eea50dfb9"
PV = "3.0-preview6"
PR = "r0"

RPROVIDES_${PN} = "dotnetcore"
DEPENDS = "zlib curl"
RDEPENDS_${PN} = "libunwind icu libcurl openssl libgssapi-krb5"

PACKAGECONFIG_pn-curl = 'zlib ipv6 ssl'

# See https://github.com/dotnet/core/tree/master/release-notes for json formatted download details
URL = "https://download.visualstudio.microsoft.com/download/pr/8997987c-1fcc-4b83-ab49-08117ac40f86/13f3cc0b0dfcf37398d11caff3926bb9/dotnet-sdk-3.0.100-preview6-012264-linux-arm64.tar.gz"

# run certUtil -hashfile dotnet-sdk-3.0.100-preview6-012264-linux-arm64.tar.gz MD5 (or SHA256) to get hashes
SRC_URI =  "${URL};downloadfilename=dotnet-${PV}.tar.gz"
SRC_URI[md5sum] = "2b0395f45eb9f6f8e2d4c25361b4ef65"
SRC_URI[sha256sum] = "16b691679fe5772cb822cd1b22e5bc7c1f3a149422b16c6d0845b7b0a87fdbfc"

S = "${WORKDIR}"

PACKAGES = "${PN}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

python do_install () {
    bb.build.exec_func("shell_do_install", d)
    oe.path.make_relative_symlink(d.expand("${D}${bindir}/dotnet"))
}

shell_do_install() {
	install -d ${D}${bindir}

	install -d ${D}/opt/dotnet
	install -d ${D}/opt/dotnet/host
  	install -d ${D}/opt/dotnet/sdk
	install -d ${D}/opt/dotnet/shared

	install -m 0755 ${S}/dotnet ${D}/opt/dotnet
	install -m 0644 ${S}/LICENSE.txt ${D}/opt/dotnet
	install -m 0644 ${S}/ThirdPartyNotices.txt ${D}/opt/dotnet

	cp -R --no-dereference --preserve=mode,links -v ${S}/host/. ${D}/opt/dotnet/host/
  	cp -R --no-dereference --preserve=mode,links -v ${S}/sdk/. ${D}/opt/dotnet/sdk/
	cp -R --no-dereference --preserve=mode,links -v ${S}/shared/. ${D}/opt/dotnet/shared/

	# Symlinks
	ln -s ${D}/opt/dotnet/dotnet ${D}${bindir}/dotnet

    # Configure install directory
	mkdir -p ${D}/etc/dotnet && printf '/opt/dotnet' > ${D}/etc/dotnet/install_location
}

FILES_${PN} = "\
	${bindir} \
	/opt/dotnet/dotnet \
	/opt/dotnet/*.txt \
	/opt/dotnet/host \
  	/opt/dotnet/sdk \
	/opt/dotnet/shared \
	/etc/dotnet/install_location \
"

INSANE_SKIP_${PN} = "already-stripped staticdev ldflags libdir file-rdeps"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
