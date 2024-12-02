year := $(shell date +'%Y')
day := $(shell date +'%-d')
part := 1
s := ""

.PHONY: help
help:
	@echo "Usage: make [target]"
	@echo ""
	@echo "Targets:"
	@echo "  help       Show this help message"
	@echo "  connect    Connect to duckdb file"
	@echo "  test       Run tests for the current day"
	@echo "  gen        Generate input and template for the current day"
	@echo "  submit     Submit the current day to Advent of Code"

.PHONY: connect
connect:
	@echo "Connecting to duckdb"
	@duckdb ./advent2024/dbt.duckdb

.PHONY: debug
debug:
	@echo "Debugging year=$(year) day=$(day) part=$(part)"
	@cd advent2024 && \
		uv run dbt run -s +model_$(day)_${part}
	@duckdb ./advent2024/dbt.duckdb "select * from main_day$(day).model_$(day)_$(part);"

.PHONY: run
run:
	@cd advent2024 && \
		uv run dbt run -s $(s)

.PHONY: seed
seed:
	@cd advent2024 && \
		uv run dbt seed -s $(s)

.PHONY: test
test:
	@echo "Testing year=$(year) day=$(day) part=$(part)"
	@cd advent2024 && \
		uv run dbt test -s +model_$(day)_${part}

.PHONY: gen
gen:
	@echo "Generating year=$(year) day=$(day)"
	@./cli/gen_day.sh $(day)
	@uv run aoc download-input --day $(day)

.PHONY: submit
submit:
	@echo "Submitting year=$(year) day=$(day) part=$(part)"
	@uv run aoc submit-solution --day $(day) --part $(part)
