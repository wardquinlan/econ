delete from time_series_data where id = 100;
delete from time_series where id = 100;

insert into time_series (id, name, title, source_org) values (100, 'TEST1', 'TEST1', 'TEST');
insert into time_series_data (id, datestamp, value) values (100, '2022-10-07', 100);
insert into time_series_data (id, datestamp, value) values (100, '2022-10-14', 100);
insert into time_series_data (id, datestamp, value) values (100, '2022-10-21', 100);
insert into time_series_data (id, datestamp, value) values (100, '2022-10-28', 100);
insert into time_series_data (id, datestamp, value) values (100, '2022-11-04', 100);
insert into time_series_data (id, datestamp, value) values (100, '2022-11-11', 100);
insert into time_series_data (id, datestamp, value) values (100, '2022-11-18', 100);
insert into time_series_data (id, datestamp, value) values (100, '2022-11-25', 100);
insert into time_series_data (id, datestamp, value) values (100, '2022-12-02', 100);

delete from time_series_data where id = 101;
delete from time_series where id = 101;

insert into time_series (id, name, title, source_org) values (101, 'TEST2', 'TEST2', 'TEST');
insert into time_series_data (id, datestamp, value) values (101, '2022-09-02', 100);
insert into time_series_data (id, datestamp, value) values (101, '2022-09-09', 100);
insert into time_series_data (id, datestamp, value) values (101, '2022-09-16', 100);
insert into time_series_data (id, datestamp, value) values (101, '2022-09-23', 100);
insert into time_series_data (id, datestamp, value) values (101, '2022-09-30', 100);

delete from time_series_data where id = 102;
delete from time_series where id = 102;

insert into time_series (id, name, title, source_org) values (102, 'TEST3', 'TEST3', 'TEST');
insert into time_series_data (id, datestamp, value) values (102, '2022-12-02', 100);
insert into time_series_data (id, datestamp, value) values (102, '2022-12-09', 100);
insert into time_series_data (id, datestamp, value) values (102, '2022-12-16', 100);
insert into time_series_data (id, datestamp, value) values (102, '2022-12-23', 100);
insert into time_series_data (id, datestamp, value) values (102, '2022-12-30', 100);
