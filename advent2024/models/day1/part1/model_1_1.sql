with
    to_table as (
        select regexp_split_to_table(input, '\n') as input
        from {{ ref("input_1") }}
    ),
    to_cols as (
        select
            split_part(input, '   ', 1)::int as list_1,
            split_part(input, '   ', 2)::int as list_2
        from to_table
    ),
    to_lists as (
        select
            list(list_1 order by list_1) as list_1,
            list(list_2 order by list_2) as list_2
        from to_cols
    ),
    to_unnest as (
        select unnest(list_1) as col1, unnest(list_2) as col2
        from to_lists
    )
select sum(abs(col2 - col1)) as answer
from to_unnest


