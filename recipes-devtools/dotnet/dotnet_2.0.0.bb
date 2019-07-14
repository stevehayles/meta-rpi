DESCRIPTION = ".NET Core Runtime, SDK & CLI tools"
HOMEPAGE = "https://www.microsoft.com/net/core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=42b611e7375c06a28601953626ab16cb"

RDEPENDS_${PN} += "krb5 icu openssl curl libunwind"

# coreclr
# DEPENDS = "clang-native lldb libunwind gettext icu openssl util-linux cmake-native lttng-ust ca-certificates-native"
# RDEPENDS_${PN} = "libicuuc libicui18n lttng-ust"

# corefx
# DEPENDS = "clang-native coreclr libunwind gettext icu openssl util-linux cmake-native krb5 curl ca-certificates-native"
# RDEPENDS_${PN} = "coreclr libcurl libuv"

# corehost
# DEPENDS = "clang-native corefx coreclr cmake-native openssl ca-certificates-native"
# RDEPENDS_${PN} = "libssl libicuuc libicui18n libcurl"

PR = "r0"

# latest https://dotnetcli.blob.core.windows.net/dotnet/Runtime/release/2.0.0/dotnet-runtime-latest-linux-arm.tar.gz
# md5sum 70ed44a618d8237c3efe9ba81b16468b
# sha256 50e8955644ebbecce51ea2cd6f28a5db3b2b93b2f7201befae30da2fa17ac299

# 2.0.0  https://dotnetcli.blob.core.windows.net/dotnet/Runtime/2.0.0/dotnet-runtime-2.0.0-linux-arm.tar.gz
# md5sum ce013b11e6e87b8c3eaae3fb489194b0
# sha256 ad09b4618cb0a5e13914027f11794fcbf5529bea33078397324c7eb78f9778e8

SRC_URI =  "https://dotnetcli.blob.core.windows.net/dotnet/Runtime/2.0.0/dotnet-runtime-2.0.0-linux-arm.tar.gz;downloadfilename=dotnet-${PV}.tar.gz"
SRC_URI[md5sum] = "ce013b11e6e87b8c3eaae3fb489194b0"     
SRC_URI[sha256sum] = "ad09b4618cb0a5e13914027f11794fcbf5529bea33078397324c7eb78f9778e8"

S = "${WORKDIR}"

HOST_FXR = "2.0.0"
SHARED_FRAMEWORK = "2.0.0"
SDK = "2.0.0"

PACKAGES = "\
	${PN} \
"

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
	install -d ${D}/opt/dotnet/shared

	install -m 0755 ${S}/dotnet ${D}/opt/dotnet
	install -m 0644 ${S}/LICENSE.txt ${D}/opt/dotnet
	install -m 0644 ${S}/ThirdPartyNotices.txt ${D}/opt/dotnet

	cp -a ${S}/host/. ${D}/opt/dotnet/host/
	cp -a ${S}/shared/. ${D}/opt/dotnet/shared/

	# Symlinks
	ln -s ${D}/opt/dotnet/dotnet ${D}${bindir}/dotnet
}

FILES_${PN} = "\
	${bindir} \
	/opt/dotnet/dotnet \
	/opt/dotnet/*.txt \
	/opt/dotnet/host \
	/opt/dotnet/shared \
"

INSANE_SKIP_${PN} = "already-stripped staticdev ldflags libdir file-rdeps"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"