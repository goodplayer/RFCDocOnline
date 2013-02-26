create table rfc(
  id int primary key,
  title varchar(200),
  author varchar(200),
  origInfo varchar(1000)
);

create index idx_rfc_id on rfc(id);

create table rfcpage(
  id int,
  pageNum int,
  content varchar(5000),
  primary key(id, pageNum)
);

create index idx_rfcpage_id on rfcpage(id);
create index idx_rfcpage_pageNum on rfcpage(pageNum);

-- 全文搜索部分
CREATE ALIAS IF NOT EXISTS FT_INIT FOR "org.h2.fulltext.FullText.init";
CALL FT_INIT();
CALL FT_CREATE_INDEX('PUBLIC', 'RFC', NULL);
CALL FT_CREATE_INDEX('PUBLIC', 'RFCPAGE', NULL);
