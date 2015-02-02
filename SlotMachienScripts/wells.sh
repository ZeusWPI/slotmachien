#!/usr/bin/env bash
# Passes door commands to slotmachien-pi.

# Runs on wells, authorized users can just invoke the commands:
# - `door open`
# - `door close`
# - `door status`
# Which will call this script (/usr/local/bin/door). Private key should be
# inserted to allow ssh to the slotmachien raspberry pi.

temp_file=$(mktemp)
cat > $temp_file <<EOF
-----BEGIN RSA PRIVATE KEY-----
-----END RSA PRIVATE KEY-----
EOF

ssh -i $temp_file slotmachien@raspberrypi "./door $(id -un) $@"
rm $temp_file
