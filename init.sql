CREATE EXTENSION "pgcrypto";

CREATE TABLE public.role
(
    id uuid NOT NULL,
    role_name integer,
    CONSTRAINT role_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

CREATE TABLE public."user"
(
    id uuid NOT NULL,
    created_date timestamptz,
    updated_at timestamptz,
    password character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT user_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;


ALTER TABLE public.role
    OWNER to postgres;

INSERT INTO public.role(id, role_name)
VALUES
    (gen_random_uuid(), 0);

ALTER TABLE public."user"
    OWNER to postgres;


CREATE TABLE public.tx_history
(
    id uuid NOT NULL,
    create_date timestamptz,
    tx_type character varying(255) COLLATE pg_catalog."default",
    result integer,
    user_id uuid,
    CONSTRAINT transaction_pkey PRIMARY KEY (id),
    CONSTRAINT FKq3vs6jlluoas5q30tarofapx4 FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.tx_history
    OWNER to postgres;

CREATE TABLE public.user_roles
(
    users_id uuid NOT NULL,
    roles_id uuid NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (users_id, roles_id),
    CONSTRAINT FKayp8a554n3beny1eifp5ct9nt FOREIGN KEY (users_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT FKsoyrbfa9510yyn3n9as9pfcsx FOREIGN KEY (roles_id)
        REFERENCES public.role (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.user_roles
    OWNER to postgres;


