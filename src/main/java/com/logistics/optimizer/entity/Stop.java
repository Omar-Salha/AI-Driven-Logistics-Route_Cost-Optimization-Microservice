package com.logistics.optimizer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "stops")
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @Column(name = "sequence_order")
    private Integer sequenceOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private StopType type;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "earliest_arrival")
    private Instant earliestArrival;

    @Column(name = "latest_arrival")
    private Instant latestArrival;

    @Column(name = "service_minutes")
    private Integer serviceMinutes;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "address")
    private String address;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public StopType getType() {
        return type;
    }

    public void setType(StopType type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Instant getEarliestArrival() {
        return earliestArrival;
    }

    public void setEarliestArrival(Instant earliestArrival) {
        this.earliestArrival = earliestArrival;
    }

    public Instant getLatestArrival() {
        return latestArrival;
    }

    public void setLatestArrival(Instant latestArrival) {
        this.latestArrival = latestArrival;
    }

    public Integer getServiceMinutes() {
        return serviceMinutes;
    }

    public void setServiceMinutes(Integer serviceMinutes) {
        this.serviceMinutes = serviceMinutes;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

