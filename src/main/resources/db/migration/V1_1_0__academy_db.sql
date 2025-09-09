create table category
(
    id            bigint auto_increment primary key,
    title varchar(250) not null,
    priority int  not null,
    created_at    datetime(6)  not null,
    updated_at    datetime(6)  not null
);

create table course
(
    id            bigint auto_increment primary key,
    title varchar(250) not null,
    description varchar(300) not null,
    url_image varchar(300) not null,
    url_resource varchar(300)  null,
    chapters int  null,
    duration varchar(30) null,
    status   enum('AVAILABLE','SOON', 'EXPIRED') not null,
    created_at    datetime(6)  not null,
    updated_at    datetime(6)  not null
);

create table course_category
(
    id            bigint auto_increment primary key,
    course_id bigint not null,
    category_id bigint not null,
    priority int  not null,
    created_at    datetime(6)  not null,
    updated_at    datetime(6)  not null,
    constraint fk_course foreign key (course_id) references course (id),
    constraint fk_caregory foreign key (category_id) references category (id)
);

create table chapter
(
    id            bigint auto_increment primary key,
    course_id bigint not null,
    title varchar(250) not null,
    description varchar(300) not null,
    url_media varchar(300) not null,
    title_resource varchar(300)  null,
    url_resource varchar(300)  null,
    duration varchar(30) not null,
    main_chapter boolean not null,
    created_at    datetime(6)  not null,
    updated_at    datetime(6)  not null,
    constraint fk_course_chapter foreign key (course_id) references course (id)
);


create table customer_course
(
    id            bigint auto_increment primary key,
    customer_id varchar(20) not null,
    course_id bigint not null,
    url_resource varchar(300)  null,
    status   enum('PROGRESS', 'FINISH') not null,
    created_at    datetime(6)  not null,
    updated_at    datetime(6)  not null,
    constraint fk_customer_course foreign key (course_id) references course (id)
);

create table customer_course_chapter
(
    id            bigint auto_increment primary key,
    customer_course_id bigint not null,
    chapter_id bigint not null,
    status   enum('PROGRESS', 'FINISH') not null,
    created_at    datetime(6)  not null,
    updated_at    datetime(6)  not null,
    constraint fk_customer_course_chapter foreign key (customer_course_id) references customer_course (id),
    constraint fk_customer_course_chapter_chapter foreign key (chapter_id) references chapter (id)
);


