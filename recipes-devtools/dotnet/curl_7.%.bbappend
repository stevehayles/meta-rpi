# Remove gnutils and enable ssl
PACKAGECONFIG_remove = "gnutls"
PACKAGECONFIG_append = " zlib ipv6 ssl"

DEPENDS_append = " openssl"
