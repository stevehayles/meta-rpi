DESCRIPTION = "64 bit .NET Core Runtime, SDK & CLI tools"
HOMEPAGE = "https://www.microsoft.com/net/core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9fc642ff452b28d62ab19b7eea50dfb9"
PV = "3.0.1-latest"
PR = "r0"

RPROVIDES_${PN} = "dotnetcore-64"
DEPENDS = "zlib curl"
RDEPENDS_${PN} = "libunwind icu libcurl openssl libgssapi-krb5"

PACKAGECONFIG_pn-curl = 'zlib ipv6 ssl'

# See https://github.com/dotnet/core/tree/master/release-notes for json formatted download details
URL = "https://dotnetcli.blob.core.windows.net/dotnet/Sdk/release/3.0.1xx/dotnet-sdk-latest-linux-arm64.tar.gz"
       
# run certUtil -hashfile dotnet-sdk-3.0.100-preview6-012264-linux-arm64.tar.gz MD5 (or SHA256) to get hashes
# this is a rolling 'latest' release so turn off the checksum validation for now
BB_STRICT_CHECKSUM = "0"
SRC_URI =  "${URL};downloadfilename=dotnet-${PV}-linux-arm64.tar.gz"

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

	install -d ${D}/usr/share/dotnet
	install -d ${D}/usr/share/dotnet/host
  	install -d ${D}/usr/share/dotnet/sdk
	install -d ${D}/usr/share/dotnet/shared

	install -m 0755 ${S}/dotnet ${D}/usr/share/dotnet
	install -m 0644 ${S}/LICENSE.txt ${D}/usr/share/dotnet
	install -m 0644 ${S}/ThirdPartyNotices.txt ${D}/usr/share/dotnet

	cp -R --no-dereference --preserve=mode,links -v ${S}/host/. ${D}/usr/share/dotnet/host/
  	cp -R --no-dereference --preserve=mode,links -v ${S}/sdk/. ${D}/usr/share/dotnet/sdk/
	cp -R --no-dereference --preserve=mode,links -v ${S}/shared/. ${D}/usr/share/dotnet/shared/

	# Symlinks
	ln -s ${D}/usr/share/dotnet/dotnet ${D}${bindir}/dotnet

    # Configure install directory
	mkdir -p ${D}/etc/dotnet && printf '/usr/share/dotnet' > ${D}/etc/dotnet/install_location
}

FILES_${PN} = "\
	${bindir} \
	/usr/share/dotnet/dotnet \
	/usr/share/dotnet/*.txt \
	/usr/share/dotnet/host \
  	/usr/share/dotnet/sdk \
	/usr/share/dotnet/shared \
	/etc/dotnet/install_location \
"

INSANE_SKIP_${PN} = "already-stripped staticdev ldflags libdir file-rdeps"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
