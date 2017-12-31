# Remove gnutils and enable ssl
PACKAGECONFIG_remove = "gnutls"
PACKAGECONFIG_append = " ssl"

RDEPENDS_${PN}_append = " openssl"