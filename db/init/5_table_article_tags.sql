-- Create article tags table ********************************
CREATE TABLE public.article_tags
(
    article_id uuid NOT NULL,
    tag_id uuid NOT NULL,
    CONSTRAINT "ArticleTags_pkey" PRIMARY KEY (article_id, tag_id),
    CONSTRAINT "ArticleTags_article_id_fkey" FOREIGN KEY (article_id)
        REFERENCES public.articles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT "ArticleTags_tag_id_fkey" FOREIGN KEY (tag_id)
        REFERENCES public.tags (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public.article_tags
    OWNER to mattlalonde;

GRANT ALL ON TABLE public.article_tags TO mattlalonde;

GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.article_tags TO ml_website_admin_user;

CREATE INDEX article_id_ci
    ON public.article_tags USING btree
    (article_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX tag_id_ci
    ON public.article_tags USING btree
    (tag_id ASC NULLS LAST)
    TABLESPACE pg_default;