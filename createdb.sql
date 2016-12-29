-------------------------------------------------------------------
-- CREATE TABLES --
-------------------------------------------------------------------

-- building.xy
create table buildings (
  bid varchar(20),
  bname varchar(50),
  nover int,
  shape sdo_geometry,
  primary key(bid)
);

-- students.xy
create table students (
  personid varchar(20),
  location sdo_geometry,
  primary key(personid)
);
  
-- annoucementsystems.xy
create table annsystems (
  asid varchar(20),
  locus sdo_geometry,
  radius int,
  circle sdo_geometry,
  primary key(asid)
);


---------------------------------------------------------------------------
-- UPDATE METADATA VIEW --
---------------------------------------------------------------------------

INSERT INTO user_sdo_geom_metadata
    (TABLE_NAME,
     COLUMN_NAME,
     DIMINFO,
     SRID)
  VALUES (
  'buildings',
  'shape',
  SDO_DIM_ARRAY(   
    SDO_DIM_ELEMENT('X', 0, 820, 0.005),
    SDO_DIM_ELEMENT('Y', 0, 580, 0.005)
     ),
  NULL   
);

INSERT INTO user_sdo_geom_metadata
    (TABLE_NAME,
     COLUMN_NAME,
     DIMINFO,
     SRID)
  VALUES (
  'students',
  'location',
  SDO_DIM_ARRAY( 
    SDO_DIM_ELEMENT('X', 0, 820, 0.005),
    SDO_DIM_ELEMENT('Y', 0, 580, 0.005)
     ),
  NULL   
);

INSERT INTO user_sdo_geom_metadata
    (TABLE_NAME,
     COLUMN_NAME,
     DIMINFO,
     SRID)
  VALUES (
  'annsystems',
  'circle',
  SDO_DIM_ARRAY(  
    SDO_DIM_ELEMENT('X', 0, 820, 0.005),
    SDO_DIM_ELEMENT('Y', 0, 580, 0.005)
     ),
  NULL  
);

-------------------------------------------------------------------
-- CREATE THE SPATIAL INDEX --
-------------------------------------------------------------------

CREATE INDEX bindex
   ON buildings(shape)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX sindex
   ON students(location)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX aindex
  ON annsystems(circle)
  INDEXTYPE IS MDSYS.SPATIAL_INDEX;


-------------------------------------------------------------------
-- DEBUG --
-------------------------------------------------------------------

-- building example
insert into buildings values (
  '0', 'example building', 3,
  sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,1),sdo_ordinate_array(1,1, 5,7, 2,3))
);
-- student example
insert into students values (
  '0', 
  sdo_geometry(2001,NULL,sdo_point_type(12,14,NULL),NULL,NULL)
);

-- as example
insert into annsystems values (
  '0',
  sdo_geometry(2001,NULL,sdo_point_type(0,0,NULL),NULL,NULL),
  5, 
  sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,4),sdo_ordinate_array(0,5, -5,0, 5,0))
);

-- look 
select * from buildings;
select * from students;
select * from annsystems;

-- drop
drop table buildings;
drop table students;
drop table annsystems;

-- select
select * from buildings where SDO_GEOM.RELATE(shape, 'anyinteract', sdo_geometry(2003,NULL,NULL,sdo_elem_info_array(1,1003,1),sdo_ordinate_array(1,1, 580,1,580,580, 1,580)), 0.005)='TRUE';
select SDO_GEOM.SDO_DISTANCE(circle, sdo_geometry(2001,NULL,sdo_point_type(500,500,NULL),NULL,NULL),0.005) from annsystems;

select * from annsystems a1 where SDO_GEOM.SDO_DISTANCE(a1.circle, sdo_geometry(2001,NULL,sdo_point_type(500,500,NULL),NULL,NULL),0.005) = (select min(SDO_GEOM.SDO_DISTANCE(a2.circle, sdo_geometry(2001,NULL,sdo_point_type(500,500,NULL),NULL,NULL),0.005)) from annsystems a2);
select s.* from students s, annsystems a1 where SDO_GEOM.RELATE(s.location, 'anyinteract', a1.circle, 0.005)='TRUE' and SDO_GEOM.SDO_DISTANCE(a1.circle, sdo_geometry(2001,NULL,sdo_point_type(500,500,NULL),NULL,NULL),0.005) = (select min(SDO_GEOM.SDO_DISTANCE(a2.circle, sdo_geometry(2001,NULL,sdo_point_type(500,500,NULL),NULL,NULL),0.005)) from annsystems a2); 
  
select a3.asid, a3.locus, a3.radius, s.*  from students s, annsystems a1, annsystems a3 where SDO_GEOM.SDO_DISTANCE(s.location,a3.circle,0.005) = (select min(SDO_GEOM.SDO_DISTANCE(s.location,a4.circle,0.005)) from annsystems a4 where a4.asid<>a1.asid) and
SDO_GEOM.RELATE(s.location, 'anyinteract', a1.circle, 0.005)='TRUE' and SDO_GEOM.SDO_DISTANCE(a1.circle, sdo_geometry(2001,NULL,sdo_point_type(500,500,NULL),NULL,NULL),0.005) = (select min(SDO_GEOM.SDO_DISTANCE(a2.circle, sdo_geometry(2001,NULL,sdo_point_type(500,500,NULL),NULL,NULL),0.005)) from annsystems a2)
and a3.asid<>a1.asid; 

select a3.asid, a3.locus, a3.radius, s.*  from students s, annsystems a1, annsystems a3 where SDO_GEOM.SDO_DISTANCE(s.location,a3.circle,0.005) = (select min(SDO_GEOM.SDO_DISTANCE(s.location,a4.circle,0.005)) from annsystems a4 where a4.asid<>a1.asid) and SDO_GEOM.RELATE(s.location, 'anyinteract', a1.circle, 0.005)='TRUE' and SDO_GEOM.SDO_DISTANCE(a1.circle, sdo_geometry(2001,NULL,sdo_point_type(500,500,NULL),NULL,NULL),0.005) = (select min(SDO_GEOM.SDO_DISTANCE(a2.circle, sdo_geometry(2001,NULL,sdo_point_type(500,500,NULL),NULL,NULL),0.005)) from annsystems a2) and a3.asid<>a1.asid; 
 
  