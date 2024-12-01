from __future__ import annotations

import argparse
import os.path
import re
import sys
import time
import urllib.error
import urllib.parse
import urllib.request
from datetime import datetime
from pathlib import Path

import duckdb
from dbt.cli.main import dbtRunner, dbtRunnerResult

ROOT = Path(__file__).parent.parent
DATABASE_CONN = os.getenv("DATABASE_CONN", "advent2024/dbt.duckdb")

TOO_QUICK = re.compile("You gave an answer too recently.*to wait.")
WRONG = re.compile(r"That's not the right answer.*?\.")
RIGHT = "That's the right answer!"
ALREADY_DONE = re.compile(r"You don't seem to be solving.*\?")


def cli() -> int:
    parser = argparse.ArgumentParser()
    commands = {
        "download-input": download_input,
        "submit-solution": submit_solution,
    }
    now = datetime.now()
    parser.add_argument("command", choices=commands)
    parser.add_argument("--year", type=int, default=now.year)
    parser.add_argument("--day", type=int, default=now.day)
    parser.add_argument("--part", type=int, default=1)
    args = parser.parse_args()

    return commands[args.command](args)


def _get_cookie_headers() -> dict[str, str]:
    with open(os.path.join(ROOT, ".env")) as f:
        contents = f.read().strip()
    return {"Cookie": f"session={contents}"}


def get_input(year: int, day: int) -> str:
    url = f"https://adventofcode.com/{year}/day/{day}/input"
    req = urllib.request.Request(url, headers=_get_cookie_headers())
    return urllib.request.urlopen(req).read().decode()


def download_input(args: argparse.Namespace) -> int:
    RETRIES = 5
    for _ in range(RETRIES):
        try:
            s = get_input(args.year, args.day)
        except urllib.error.URLError as e:
            print(f"zzz: not ready yet: {e}")
            time.sleep(1)
        else:
            break
    else:
        raise SystemExit(f"timed out after attempting {RETRIES} times")

    output_path = (
        ROOT / f"advent2024/seeds/fixtures/day{args.day}/input_{args.day}.csv"
    )
    if not output_path.parent.exists():
        output_path.parent.mkdir(parents=True)

    with output_path.open("w") as f:
        f.write(f"input\n{s}")

    lines = s.splitlines()
    if len(lines) > 10:
        for line in lines[:10]:
            print(line)
        print("...")
    else:
        print(lines[0][:80])
        print("...")

    return 0


def _post_answer(year: int, day: int, part: int, answer: int) -> str:
    params = urllib.parse.urlencode({"level": part, "answer": answer})
    req = urllib.request.Request(
        f"https://adventofcode.com/{year}/day/{day}/answer",
        method="POST",
        data=params.encode(),
        headers=_get_cookie_headers(),
    )
    return urllib.request.urlopen(req).read().decode()


def _run_dbt_commands(cmds):
    try:
        os.chdir(ROOT / "advent2024")
        for cmd in cmds:
            print(f"Running dbt {' '.join(cmd)}")
            rv: dbtRunnerResult = dbtRunner().invoke(cmd)
            if rv.exception or not rv.success:
                print(
                    f"Command failed: {' '.join(cmd)} \n"
                    + f"Success: {rv.success} \n"
                    + f"Exception: {rv.exception} \n"
                    + f"ReturnValue: {rv}"
                )
                raise rv.exception or Exception(
                    f"Command failed (success={rv.success})"
                )
    finally:
        os.chdir(ROOT)


def get_answer_from_db(day, part) -> int:
    query = f"select answer from main_day{day}.model_{day}_{part}"
    conn = duckdb.connect(DATABASE_CONN)
    result = conn.execute(query).fetchall()
    conn.close()
    assert len(result) == 1
    out = result[0][0]
    assert isinstance(out, int)
    return out


def submit_solution(args: argparse.Namespace) -> int:
    cmds = [
        ["seed", "-s", f"input_{args.day}"],
        ["run", "-s", f"+model_{args.day}_{args.part}"],
    ]
    _run_dbt_commands(cmds)

    answer = get_answer_from_db(args.day, args.part)
    print(f"Answer: {answer}")
    contents = _post_answer(args.year, args.day, args.part, answer)

    for error_regex in (WRONG, TOO_QUICK, ALREADY_DONE):
        error_match = error_regex.search(contents)
        if error_match:
            print(f"\033[41m{error_match[0]}\033[m")
            return 1

    if RIGHT in contents:
        print(f"\033[42m{RIGHT}\033[m")
        return 0
    else:
        # unexpected output?
        print(contents)
        return 1


if __name__ == "__main__":
    sys.exit(cli())
