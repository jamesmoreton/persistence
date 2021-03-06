#!/bin/bash

function commandCheck() {
  if ! command -v mongo &> /dev/null; then
    echo "❌ Mongo must be installed"
    exit 1
  fi
}

function startMongo() {
  echo "➡️ Starting Mongo Daemon on port 26543..."

  commandCheck
  mongod --config /usr/local/etc/mongod.conf --fork --port 26543

  echo "✅ Start up successful"
}

function shutdownMongo() {
  echo "➡️ Shutting down Mongo Daemon..."

  commandCheck
  mongo --port 26543 << EOF
    use admin
    db.shutdownServer()
EOF

  echo "✅ Shut down successful"
}

case $1 in
start)
  startMongo
  ;;
shutdown)
  shutdownMongo
  ;;
*)
  echo "Usage: $0 [ start | shutdown ]"
  exit 1
  ;;
esac