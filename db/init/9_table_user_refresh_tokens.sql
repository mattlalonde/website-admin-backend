-- Create user refresh token table *********************************
CREATE TABLE public.user_refresh_tokens
(
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    expires_at timestamp with time zone NOT NULL,
    CONSTRAINT user_refresh_tokens_pkey PRIMARY KEY (id),
    CONSTRAINT user_refresh_token_fk FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public.user_refresh_tokens
    OWNER to mattlalonde;

GRANT ALL ON TABLE public.user_refresh_tokens TO mattlalonde;

GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.user_refresh_tokens TO ml_website_admin_user;