-- Create users table **********************
CREATE TABLE public.users
(
    id uuid NOT NULL,
    first_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(250) COLLATE pg_catalog."default" NOT NULL,
    password character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT unique_user_email_constraint UNIQUE (email)
)

TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to mattlalonde;

GRANT ALL ON TABLE public.users TO mattlalonde;

GRANT INSERT, SELECT, UPDATE ON TABLE public.users TO ml_website_admin_user;