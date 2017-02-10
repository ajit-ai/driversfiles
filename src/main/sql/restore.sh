#!/bin/bash

DATABASE="driversfiles"
USER="driversfiles"

echo "drop database ${DATABASE};" | psql -U postgres
echo "drop user ${USER};" | psql -U postgres
echo "create user ${USER} with encrypted password '${DATABASE}'" | psql -U postgres
echo "create database ${DATABASE} with encoding 'utf-8' template template0;" | psql -U postgres
echo "grant all on database ${DATABASE} to ${USER};" | psql -U postgres

if [ ! -z "${1}" ]; then #dmp file to load
	pg_restore -U "${USER}" -d "${DATABASE}" "${1}"
else
	psql -U ${USER} ${DATABASE} < ddl.sql
	psql -U ${USER} ${DATABASE} < populate.sql
fi
