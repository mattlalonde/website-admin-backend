-- Create tag table *************************
CREATE TABLE public.tags
(
    id uuid NOT NULL,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(250) COLLATE pg_catalog."default",
    CONSTRAINT "Tags_pkey" PRIMARY KEY (id),
    CONSTRAINT unique_tag_name_constraint UNIQUE (name)
)

TABLESPACE pg_default;

ALTER TABLE public.tags
    OWNER to mattlalonde;

GRANT ALL ON TABLE public.tags TO mattlalonde;

GRANT INSERT, SELECT, UPDATE ON TABLE public.tags TO ml_website_admin_user;