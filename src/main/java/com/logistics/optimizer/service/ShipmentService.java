package com.logistics.optimizer.service;

import com.logistics.optimizer.dto.ShipmentRequest;
import com.logistics.optimizer.dto.ShipmentResponse;
import com.logistics.optimizer.entity.Shipment;
import com.logistics.optimizer.entity.ShipmentStatus;
import com.logistics.optimizer.entity.Stop;
import com.logistics.optimizer.entity.StopType;
import com.logistics.optimizer.mapper.ShipmentMapper;
import com.logistics.optimizer.repository.ShipmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Transactional
    public ShipmentResponse createShipment(ShipmentRequest request) {
        Shipment shipment = new Shipment();
        shipment.setExternalRef(request.externalRef());
        shipment.setStatus(ShipmentStatus.CREATED);

        int index = 1;
        for (var stopRequest : request.stops()) {
            Stop stop = new Stop();
            stop.setShipment(shipment);
            stop.setSequenceOrder(index++);
            stop.setType(StopType.valueOf(stopRequest.type().toUpperCase()));
            stop.setLatitude(stopRequest.lat());
            stop.setLongitude(stopRequest.lng());
            stop.setWeightKg(stopRequest.weightKg());
            stop.setServiceMinutes(stopRequest.serviceMinutes());
            stop.setEarliestArrival(stopRequest.earliestArrival());
            stop.setLatestArrival(stopRequest.latestArrival());
            stop.setAddress(stopRequest.address());
            shipment.getStops().add(stop);
        }

        shipmentRepository.save(shipment);
        return ShipmentMapper.toDto(shipment);
    }

    @Transactional
    public ShipmentResponse getShipment(UUID id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shipment " + id + " not found"));
        shipment.getStops().size(); // initialize lazy collection
        return ShipmentMapper.toDto(shipment);
    }
}

