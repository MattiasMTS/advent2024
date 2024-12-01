with
    to_rows as (select regexp_split_to_table(input, '\n') as input from {{ ref("input_1") }}),
    to_columns as (
        select
            split_part(input, '   ', 1)::int as column_1,
            split_part(input, '   ', 2)::int as column_2
        from to_rows
        where input != ''
        order by column_1, column_2
    )
select sum(column_2 - column_1) as answer
from to_columns

