create table if not exists rfc_doc
(
    rfc_id       varchar(100) primary key,
    content      BLOB,
    updates      BLOB,
    obsoletes    BLOB,
    time_created bigint,
    time_updated bigint
);
