# Remove gnutils and enable ssl
PACKAGECONFIG[ssl] = "--with-ssl"
PACKAGECONFIG[gnutls] = "--without-gnutls"

EXTRA_OECONF = "--with-ssl \
                --without-gnutls \
                "

