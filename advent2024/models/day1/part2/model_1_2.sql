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
        select unnest(list_1) as col1, list_2
        from to_lists
    ),
    list_transform as (
        select
            col1,
            list_transform(list_2, x -> case when x = col1 then 1 end) as list_2
        from to_unnest
    ),
    list_filter as (
        select
            col1,
            list_filter(list_2, x -> x is not null) as list_2
        from list_transform
    )

select sum(col1 * len(list_2)) as answer
from list_filter
