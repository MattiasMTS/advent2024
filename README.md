# ❄️ advent2024 ❄️

<!--toc:start-->

- [❄️ advent2024 ❄️](#️-advent2023-️)
- [🛠️ To get started 🛠️](#🛠️-to-get-started-🛠️)
- [🏃Useful commands 🏃](#🏃useful-commands-🏃)
- [🎄Repository Structure 🎄](#🎄repository-structure-🎄)
- [🏋️Benchmark 🏋️](#🏋️benchmark-🏋️)
<!--toc:end-->

https://adventofcode.com/2024

Squirrel is coming to town in AoC 2024 :squirrel:

# 🛠️ To get started 🛠️

Start by install `make` and `uv` on your OS.

# 🏃Useful commands 🏃

Useful commands to get you started:

1. generate the daily task:

```shell
make gen
make gen day=9
make gen year=2022 day=9
```

By default it will generate today's task but can be changed via
`year` and `day` keywords.

2. test the code:

```shell
make test
make test day=4 part=2
make test day=4 part=2
```

By default it will test today's task and part 1, but can be changed via
the `day` and `part` keywords.

3. submit your answer

```
make submit
make submit day=13 part=1
```

By default it will submit today's task and part 1, but can be changed via
the `day` and `part` keywords.

# 🎄Repository Structure 🎄

The repository is structured as follows:

- `models` is where your data models are stored. This contains the logic for transforming your input
  data to the answer needed. The output of the model should be "answer: <int>".
- `seeds` is where the input data for the submission is stored.
- `tests` is where your assert input data is stored. This is used for testing your model is correct
  with respect to the example data.

```tree
.
├── Makefile
├── README.md
├── cli
│   └── main.go
├── day01
│   ├── input.txt
│   ├── part1.go
│   ├── part1_test.go
│   ├── part2.go
│   └── part2_test.go
├── go.mod
├── go.sum
└── templates
    └── generate_aoc.go
```

- `./cli/` folder is in charge of interacting with https://adventofcode.com website
  for i) fetching the daily input and ii) submitting input.

  You can try it out by running:

  ```shell
  go run cli/main.go --help
  ```

- `./templates/` contains the go templates for generating each day.

- `./dayXX/` contains the daily problems. For each part, we have a solution file
  `partN.go` and a corresponding test file `partN_test.go`.

  The test file can look like this:

  ```go
  func Test_solvePart1(t *testing.T) {
  type args struct {
  	input func() (string, error)
  }
  tests := []struct {
  	args args
  	name string
  	want int
  }{
  	{
  		name: "solvePart1() with test input",
  		args: args{
  			input: func() (string, error) {
  				return "", nil // TODO: Add test input here.
  			},
  		},
  		want: 0, // TODO: Add expected output here.
  	},
  }
  ```

  where you have to manually add the input and answer on the TODOs.
  If you want to debug the `input.txt`, then just comment out the
  `want` field from the `test input` test case and add `want: 1`
  to the `input.txt` test case.

# 🏋️Benchmark 🏋️

```
+---------------------------------------------+
| Benchmark Results AoC 2024                  |
+-------+------+----------------+-------------+
| DAY   | PART | ANSWER         | TIME        |
+-------+------+----------------+-------------+
| day01 | 1    | 54644          | 240.278µs   |
|       |      |                |             |
|       | 2    | 53348          | 2.973872ms  |
|       |      |                |             |
| day02 | 1    | 2169           | 192.199µs   |
|       |      |                |             |
|       | 2    | 60948          | 273.45µs    |
|       |      |                |             |
| day03 | 1    | 527364         | 6.84216ms   |
|       |      |                |             |
|       | 2    | 79026871       | 5.721963ms  |
|       |      |                |             |
| day04 | 1    | 21158          | 427.507µs   |
|       |      |                |             |
|       | 2    | 6050769        | 408.992µs   |
|       |      |                |             |
| day06 | 1    | 1660968        | 11.721µs    |
|       |      |                |             |
|       | 2    | 26499773       | 30.572795ms |
|       |      |                |             |
| day07 | 1    | 253603890      | 1.261921ms  |
|       |      |                |             |
|       | 2    | 253630098      | 1.402404ms  |
|       |      |                |             |
| day08 | 1    | 19199          | 3.449268ms  |
|       |      |                |             |
|       | 2    | 13663968099527 | 7.43219ms   |
|       |      |                |             |
| day09 | 1    | 1887980197     | 4.063813ms  |
|       |      |                |             |
|       | 2    | 990            | 6.171931ms  |
|       |      |                |             |
+-------+------+----------------+-------------+
```

Benchmark table is generated by CI/CD or run `make benchmark`.
