CREATE TABLE users (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    email NVARCHAR(256) NOT NULL,
    name NVARCHAR(256),
    role NVARCHAR(50) NOT NULL DEFAULT 'user',
    created_at DATETIME2 DEFAULT SYSUTCDATETIME()
);

CREATE TABLE vehicles (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    name NVARCHAR(200),
    vehicle_type NVARCHAR(100),
    capacity_kg FLOAT,
    speed_kmph FLOAT,
    cost_per_km FLOAT,
    cost_per_hour FLOAT,
    created_at DATETIME2 DEFAULT SYSUTCDATETIME()
);

CREATE TABLE depots (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    name NVARCHAR(200),
    latitude FLOAT,
    longitude FLOAT,
    address NVARCHAR(500)
);

CREATE TABLE shipments (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    external_ref NVARCHAR(200),
    created_by UNIQUEIDENTIFIER NULL,
    status NVARCHAR(50) DEFAULT 'created',
    created_at DATETIME2 DEFAULT SYSUTCDATETIME()
);

CREATE TABLE stops (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    shipment_id UNIQUEIDENTIFIER NOT NULL REFERENCES shipments(id),
    sequence_order INT NULL,
    type NVARCHAR(50) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    earliest_arrival DATETIME2 NULL,
    latest_arrival DATETIME2 NULL,
    service_minutes INT DEFAULT 0,
    weight_kg FLOAT DEFAULT 0,
    address NVARCHAR(500)
);

CREATE TABLE optimization_runs (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    request_payload NVARCHAR(MAX),
    result_payload NVARCHAR(MAX),
    status NVARCHAR(50) DEFAULT 'pending',
    started_at DATETIME2 NULL,
    completed_at DATETIME2 NULL,
    duration_ms INT NULL,
    created_at DATETIME2 DEFAULT SYSUTCDATETIME()
);

CREATE TABLE routes (
    id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
    optimization_run_id UNIQUEIDENTIFIER REFERENCES optimization_runs(id),
    vehicle_id UNIQUEIDENTIFIER REFERENCES vehicles(id),
    sequence NVARCHAR(MAX),
    distance_km FLOAT,
    duration_minutes INT,
    cost FLOAT
);

CREATE INDEX idx_stops_shipment ON stops(shipment_id);
CREATE INDEX idx_runs_started ON optimization_runs(started_at);
CREATE INDEX idx_routes_run ON routes(optimization_run_id);

