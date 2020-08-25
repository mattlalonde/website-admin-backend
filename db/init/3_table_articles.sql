-- Create article table *****************
CREATE TABLE public.articles
(
    id uuid NOT NULL,
    title character varying(250) COLLATE pg_catalog."default" NOT NULL,
    precis character varying(500) COLLATE pg_catalog."default",
    body text COLLATE pg_catalog."default",
    created_timestamp timestamp with time zone NOT NULL,
    state character varying(20) COLLATE pg_catalog."default" NOT NULL,
    publication_date timestamp without time zone,
    CONSTRAINT "Articles_pkey" PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.articles
    OWNER to mattlalonde;

GRANT ALL ON TABLE public.articles TO mattlalonde;

GRANT INSERT, SELECT, UPDATE ON TABLE public.articles TO ml_website_admin_user;