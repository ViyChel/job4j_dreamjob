CREATE TABLE public.posts
(
    id          integer NOT NULL DEFAULT nextval('posts_id_seq'::regclass),
    name        text COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    CONSTRAINT posts_pkey PRIMARY KEY (id)
);

CREATE TABLE public.photos
(
    id   integer NOT NULL DEFAULT nextval('photos_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT photos_pkey PRIMARY KEY (id)
);

CREATE TABLE public.cities
(
    id   integer NOT NULL DEFAULT nextval('cities_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT cities_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.candidates
(
    id       integer NOT NULL DEFAULT nextval('candidates_id_seq'::regclass),
    name     text COLLATE pg_catalog."default",
    photo_id integer,
    city_id  integer,
    CONSTRAINT candidates_pkey PRIMARY KEY (id),
    CONSTRAINT candidates_cities_id_fkey FOREIGN KEY (city_id)
        REFERENCES public.cities (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT candidates_photo_id_fkey FOREIGN KEY (photo_id)
        REFERENCES public.photos (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.users
(
    id       integer NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    name     character varying(255) COLLATE pg_catalog."default",
    email    character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
);