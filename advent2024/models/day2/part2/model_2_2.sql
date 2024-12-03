{% set ok_increases = (1,2,3) %}

with
    to_table as (
    select regexp_split_to_table(input, '\n') as input, row_number() over() as report_id
        from {{ ref("input_2") }}
    ),
    to_array as (
        select report_id, regexp_split_to_array(input, ' ')::int[] as reports
        from to_table
    ),
    problem_dampener as (
        -- pop the element i from the reports
        -- to support the "Problem Dampener."
        select report_id, (reports[:i.generate_series] || reports[i.generate_series+2:]) as reports
        from to_array, generate_series(0, len(reports)) as i
    ),
    prev as (
        select report_id, reports, list_prepend(null, list_slice(reports, 0, len(reports) - 1)) as prev_reports
        from problem_dampener
    ),
    diff as (
        -- [2:] to skip the first element (null)
        select report_id, list_transform(list_zip(reports, prev_reports), x -> x[1] - x[2])[2:] as diff_reports
        from prev
    ),
    sequential as (
        select *
        from diff
        where
            list_bool_and(list_transform(diff_reports, x -> x > 0)) or
            list_bool_and(list_transform(diff_reports, x -> x < 0))
    )

select count(distinct report_id) as answer
from sequential
where list_bool_and(
    list_transform(diff_reports, x -> abs(x) in {{ ok_increases }} )
)
