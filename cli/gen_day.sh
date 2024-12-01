#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 <dayN>"
    exit 1
fi

DAY=$1
SRC_MODEL_DIR="advent2024/models/day0"
DEST_MODEL_DIR="advent2024/models/day$DAY"
DEST_SEED_DIR="advent2024/seeds/fixtures/day$DAY"
DBT_FILE="advent2024/dbt_project.yml"

mkdir -p "$DEST_MODEL_DIR/part1" "$DEST_MODEL_DIR/part2"

# models
cp "$SRC_MODEL_DIR/part1/model_0_1.sql" "$DEST_MODEL_DIR/part1/model_${DAY}_1.sql"
cp "$SRC_MODEL_DIR/part1/test.yml" "$DEST_MODEL_DIR/part1/test.yml"
cp "$SRC_MODEL_DIR/part2/model_0_2.sql" "$DEST_MODEL_DIR/part2/model_${DAY}_2.sql"
cp "$SRC_MODEL_DIR/part2/test.yml" "$DEST_MODEL_DIR/part2/test.yml"

# seeds
mkdir -p "$DEST_SEED_DIR"
echo "input" >"$DEST_SEED_DIR/test_input_${DAY}_1.csv"
echo "input" >"$DEST_SEED_DIR/test_input_${DAY}_2.csv"

# update references
find "$DEST_MODEL_DIR" -type f -exec sed -i '' -e "s/input_0/input_${DAY}/g" \
    -e "s/test_0_1/test_${DAY}_1/g" \
    -e "s/test_0_2/test_${DAY}_2/g" \
    -e "s/test_input_0_1/test_input_${DAY}_1/g" \
    -e "s/test_input_0_2/test_input_${DAY}_2/g" \
    -e "s/model_0_1/model_${DAY}_1/g" \
    -e "s/model_0_2/model_${DAY}_2/g" {} +

# add schema
if ! grep -q "    day$DAY:" "$DBT_FILE"; then
    sed -i '' "/^  advent2024:/a\\
    day$DAY:\\
      +schema: \"day$DAY\"\\
      +tags: [\"day$DAY\"]\\
" "$DBT_FILE"
fi
