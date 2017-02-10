#!/bin/bash

source common.sh

for alter_file in $(ls -1 *alter.sql); do
	echo "Running file: ${alter_file}"
	psql -U ${USER} ${DATABASE} < ${alter_file}
	echo
done
