create schema chipchippoker_dev collate utf8mb4_bin;

create table friend(
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    member_a_id bigint not null,
    member_b_id bigint not null,
    updated_at datetime(6),
    primary key(id)
) engine = InnoDB

create table friend_request(
    created_at datetime(6) not null,
    from_member_id bigint,
    id bigint not null auto_increment,
    to_member_id bigint,
    updated_at datetime(6),
    status varchar(255) not null,
    primary key(id)
) engine = InnoDB

create table game_room(
    is_private bit not null,
    password integer,
    total_participant_cnt integer not null,
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    updated_at datetime(6),
    room_manager_nickname varchar(255),
    state varchar(255) not null,
    title varchar(255) not null,
    type varchar(255) not null,
    primary key(id)
) engine = InnoDB

create table member(
    kakao_friend_list_agreement bit not null,
    kakao_link_state bit not null,
    created_at datetime(6) not null,
    game_room_id bigint,
    id bigint not null auto_increment,
    kakao_social_id bigint,
    spectate_room_id bigint,
    updated_at datetime(6),
    icon varchar(255) not null,
    member_id varchar(255),
    nickname varchar(255) not null,
    password varchar(255),
    refresh_token varchar(255),
    primary key(id)
) engine = InnoDB

create table member_game_room_black_list(
    created_at datetime(6) not null,
    game_room_id bigint,
    id bigint not null auto_increment,
    member_id bigint,
    updated_at datetime(6),
    primary key(id)
) engine = InnoDB

create table point(
    competitive_draw integer,
    competitive_lose integer,
    competitive_win integer,
    current_winning_streak integer,
    friendly_draw integer,
    friendly_lose integer,
    friendly_win integer,
    max_win integer,
    point_score integer,
    created_at datetime(6) not null,
    id bigint not null auto_increment,
    member_id bigint,
    updated_at datetime(6),
    primary key(id)
) engine = InnoDB

create table spectate_room(
    created_at datetime(6) not null,
    game_room_id bigint,
    id bigint not null auto_increment,
    updated_at datetime(6),
    primary key(id)
) engine = InnoDB