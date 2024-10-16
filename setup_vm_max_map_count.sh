#!/bin/bash
REQUIRED_VM_MAX_MAP_COUNT=262144
CURRENT_VM_MAX_MAP_COUNT=$(sysctl -n vm.max_map_count)

if [ "$CURRENT_VM_MAX_MAP_COUNT" -lt "$REQUIRED_VM_MAX_MAP_COUNT" ]; then
    echo "Setting vm.max_map_count to $REQUIRED_VM_MAX_MAP_COUNT"
    sudo sysctl -w vm.max_map_count=$REQUIRED_VM_MAX_MAP_COUNT
    echo "vm.max_map_count set successfully."
else
    echo "vm.max_map_count is already sufficient: $CURRENT_VM_MAX_MAP_COUNT"
fi
