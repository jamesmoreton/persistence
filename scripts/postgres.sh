#!/bin/bash

function commandCheck() {
  if ! hash pg_ctlcluster 2>/dev/null; then
    echo "❌ Postgres must be installed"
    exit 1
  fi
}

function createCluster() {
  echo "➡️ Creating Postgres cluster..."

  commandCheck

  pg_createcluster 12 persistence
  startCluster

  # Set necessary defaults
  psql -d postgres << EOF
   CREATE USER test WITH SUPERUSER PASSWORD 'test' VALID UNTIL 'infinity';
   ALTER SYSTEM SET PORT TO 15432;
EOF

  # Restart cluster
  pg_ctlcluster 12 persistence stop
  pg_ctlcluster 12 persistence start

  echo "✅ New cluster created"
}

function createDatabase() {
  echo "➡️ Setting up sample database..."

  commandCheck

  if ! pg_ctlcluster 12 persistence status >/dev/null; then
    echo "❌ Postgres cluster is not running..."
    exit 1
  fi

  psql -d postgres << EOF
   CREATE DATABASE persistence;
   \c persistence
   CREATE TABLE employee (
     id            SERIAL      NOT NULL,
     employee_uid  UUID        NOT NULL,
     first_name    TEXT        NOT NULL,
     last_name     TEXT        NOT NULL,
     date_of_birth DATE        NOT NULL,
     role          VARCHAR(20) NOT NULL,

     CONSTRAINT pk_employee PRIMARY KEY (id),
     CONSTRAINT uk_employee_employee_uid UNIQUE (employee_uid)
   );
EOF

  echo "✅ Sample database created"
}

function dropCluster() {
  echo "➡️ Dropping Postgres cluster..."

  commandCheck
  stopCluster
  pg_dropcluster 12 persistence

  echo "✅ Cluster dropped"
}

function status() {
  commandCheck
  if pg_ctlcluster 12 persistence status >/dev/null; then
    echo "✅ Postgres cluster is running..."
  else
    echo "🛑 Postgres cluster is not running..."
  fi
}

function startCluster() {
  echo "➡️ Starting Postgres cluster..."

  commandCheck
  pg_ctlcluster 12 persistence start

  echo "✅ Cluster started"
}

function stopCluster() {
  echo "➡️ Stopping Postgres cluster..."

  commandCheck
  pg_ctlcluster 12 persistence stop

  echo "✅ Cluster stopped"
}

case $1 in
create)
  createCluster
  createDatabase
  ;;
reset)
  dropCluster
  createCluster
  createDatabase
  ;;
status)
  status
  ;;
start)
  startCluster
  ;;
stop)
  stopCluster
  ;;
*)
  echo "Usage: $0 [ create | reset | status | start | stop ]"
  exit 1
  ;;
esac