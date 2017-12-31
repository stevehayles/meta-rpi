# Remove gnutils and enable ssl
PACKAGECONFIG_remove = "gnutls"
PACKAGECONFIG_append = " ssl"

RDEPENDS_append = " openssl"