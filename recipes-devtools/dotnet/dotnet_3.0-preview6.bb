DESCRIPTION = ".NET Core Runtime, SDK & CLI tools"
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
URL = "https://download.visualstudio.microsoft.com/download/pr/50bc5936-b374-490b-9312-f3ca23c0bcfa/d7680c7a396b115d95ac835334777d02/dotnet-sdk-3.0.100-preview6-012264-linux-arm.tar.gz"

# run certUtil -hashfile dotnet-sdk-3.0.100-preview6-012264-linux-arm.tar.gz MD5 (or SHA256) to get hashes
SRC_URI =  "${URL};downloadfilename=dotnet-${PV}.tar.gz"
SRC_URI[md5sum] = "70c52cef93ae9295524dd3a868657e77"
SRC_URI[sha256sum] = "e3223862302a302a082a94fbe64750551e1c39e0b073a63a16951c04cebe242f"

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
}

FILES_${PN} = "\
	${bindir} \
	/opt/dotnet/dotnet \
	/opt/dotnet/*.txt \
	/opt/dotnet/host \
  	/opt/dotnet/sdk \
	/opt/dotnet/shared \
"

INSANE_SKIP_${PN} = "already-stripped staticdev ldflags libdir file-rdeps"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
