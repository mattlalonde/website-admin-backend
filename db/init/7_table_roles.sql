-- Create roles table ********************************************
CREATE TABLE public.roles
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    name character varying(60) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.roles
    OWNER to mattlalonde;

GRANT ALL ON TABLE public.roles TO mattlalonde;

GRANT INSERT, SELECT, UPDATE ON TABLE public.roles TO ml_website_admin_user;