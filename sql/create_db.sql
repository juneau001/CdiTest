/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Juneau
 * Created: Mar 3, 2017
 */

create table MOVIE (
id          numeric primary key,
name        varchar(50) not null,
actors      varchar(200));

create table MOVIE_GENERATOR (
name        varchar(50),
value       numeric
);

insert into MOVIE_GENERATOR values(
'MOVIE_GEN',
1);

