CREATE TABLE territories (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    unit_name VARCHAR(120) NOT NULL,
    linked_population_percent NUMERIC(5, 2) NOT NULL,
    data_competence VARCHAR(7) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE territory_indicators (
    id UUID PRIMARY KEY,
    territory_id UUID NOT NULL REFERENCES territories(id) ON DELETE CASCADE,
    focus VARCHAR(40) NOT NULL,
    score NUMERIC(5, 2) NOT NULL,
    target NUMERIC(5, 2) NOT NULL,
    CONSTRAINT uq_territory_indicator UNIQUE (territory_id, focus)
);

CREATE TABLE search_actions (
    id UUID PRIMARY KEY,
    territory_id UUID NOT NULL REFERENCES territories(id),
    focus VARCHAR(40) NOT NULL,
    objective VARCHAR(500) NOT NULL,
    responsible_team VARCHAR(160) NOT NULL,
    planned_start DATE NOT NULL,
    planned_end DATE NOT NULL,
    target_count INTEGER NOT NULL,
    performed_count INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    notes VARCHAR(1000),
    result_notes VARCHAR(1000),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_search_actions_territory ON search_actions(territory_id);
CREATE INDEX idx_search_actions_status ON search_actions(status);
