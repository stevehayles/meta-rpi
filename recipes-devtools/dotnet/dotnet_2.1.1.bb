DESCRIPTION = ".NET Core Runtime, SDK & CLI tools"
HOMEPAGE = "https://www.microsoft.com/net/core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=42b611e7375c06a28601953626ab16cb"
PV = "2.1.1"
PR = "r0"

RPROVIDES_${PN} = "dotnetcore"
DEPENDS = "zlib curl"
RDEPENDS_${PN} += "libunwind icu curl libcurl openssl libgssapi-krb5 util-linux-libuuid lttng-ust"

PACKAGECONFIG_pn-curl = 'zlib ipv6 ssl'

SRC_URI =  "https://download.microsoft.com/download/D/0/4/D04C5489-278D-4C11-9BD3-6128472A7626/dotnet-sdk-2.1.301-linux-arm.tar.gz;downloadfilename=dotnet-${PV}.tar.gz"
SRC_URI[md5sum] = "af57c4da7976fdb4cbf8bd3b38051700"     
SRC_URI[sha256sum] = "5ab8b55dc930f4678ecb91d2d9117adf93830c9ef4dd2753ca64d61e8e3dc6d9"

S = "${WORKDIR}"

HOST_FXR = "2.1.1"
SHARED_FRAMEWORK = "2.1.1"
SDK = "2.1.1"

PACKAGES = "${PN}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
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
	
	# On x86_64 .NET uses /lib64/ld-linux-x86-64.so.2. However Yocto defaults everything to /lib
	mkdir -p /lib64 
	ln -sf /lib/ld-linux-x86-64.so.2 /lib64/ld-linux-x86-64.so.2
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
