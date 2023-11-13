package com.wego.assignment.controller.carparks.service;

import com.wego.assignment.common.view.LatLong;
import com.wego.assignment.controller.carparks.model.CarPark;
import com.wego.assignment.controller.carparks.model.CarParkAvailability;
import com.wego.assignment.controller.carparks.repo.CarParkAvailabilityRepo;
import com.wego.assignment.controller.carparks.view.CarParkDataCache;
import com.wego.assignment.controller.carparks.view.LotData;
import com.wego.assignment.controller.carparks.view.carparkavailability.CarParkInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CarParkCacheService {

    @Autowired
    CarParkAvailabilityRepo carParkAvailabilityRepo;

    protected  Map<String, CarParkDataCache> carparkDataMap = new HashMap<>();

    boolean lotDataMissingInCache(String liveCarParkLotType, List<LotData> lots) {

        Optional<LotData> lotDataOptional = lots.stream().filter(lot->lot.getLotType().equalsIgnoreCase(liveCarParkLotType)).findAny();

        if(! lotDataOptional.isPresent()) {
            return true;
        }

        return  false;
    }

    protected boolean carParkCacheChanged(CarParkDataCache carParkDataCache, CarParkInfo liveCarParkLotInfo) {

        if (carParkDataCache == null) {

            return true;
        }

        if(lotDataMissingInCache(liveCarParkLotInfo.getLotType(), carParkDataCache.getLotData())) {

            return  true;
        }

        if(lotDataInCacheStale(liveCarParkLotInfo.getTotalLots(), liveCarParkLotInfo.getLotsAvailable(), carParkDataCache.getLotData())) {

            return true;
        }

        return false;
    }

    protected boolean lotDataInCacheStale(Integer totalLots, Integer lotsAvailable, List<LotData> lots) {


        boolean lotDataStillFresh  = lots.stream().allMatch(lot-> lot.getTotalLots().equals(totalLots) && lot.getAvailableLots().equals(lotsAvailable));

        if(! lotDataStillFresh) {

            return true;
        }

        return  false;
    }


    public void updateCacheWithLiveLotData(CarParkDataCache carParkDataCache, CarParkAvailability carParkAvailability) {


        if(carParkDataCache == null) {

            CarParkDataCache carParkDataCache1 = new CarParkDataCache();
            carParkDataCache1.setCarparkNo(carParkAvailability.getCarParkNo());
            /*LatLong latLong = new LatLong();
            latLong.setLatitude(carPark.getLatitude());
            latLong.setLongitude(carPark.getLongitude());
            //carParkDataCache1.setLatLong(latLong);*/
            LotData lotData = new LotData();
            lotData.setLotType(carParkAvailability.getLotType());
            lotData.setTotalLots(carParkAvailability.getTotalLots());
            lotData.setAvailableLots(carParkAvailability.getAvailableLots());
            List<LotData> lots = new ArrayList<>();
            lots.add(lotData);
            carParkDataCache1.setLotData(lots);
            carparkDataMap.put(carParkAvailability.getCarParkNo(), carParkDataCache1);

        } else
        if(lotDataMissingInCache(carParkAvailability.getLotType(), carParkDataCache.getLotData())) {

            List<LotData> carParkLots =  carParkDataCache.getLotData();
            //fixme: use constructor
            LotData lotData = new LotData();
            lotData.setLotType(carParkAvailability.getLotType());
            lotData.setTotalLots(carParkAvailability.getTotalLots());
            lotData.setAvailableLots(carParkAvailability.getAvailableLots());
            carParkLots.add(lotData);

        } else {

            Optional<LotData> lotDataOptional = carParkDataCache.getLotData().stream().filter(lot->lot.getLotType().equalsIgnoreCase(carParkAvailability.getLotType())).findAny();
            if(lotDataOptional.isPresent()) {

                LotData lotData = lotDataOptional.get();
                lotData.setAvailableLots(carParkAvailability.getTotalLots());
                lotData.setTotalLots(carParkAvailability.getTotalLots());
            }
        }

    }

    protected Map<String, CarParkDataCache> getCarparkDataMap() {
        return carparkDataMap;
    }


    @Transactional
    public void updateCarParkAvailability(CarParkAvailability carParkAvailability, CarParkDataCache carParkDataCache) {

        carParkAvailabilityRepo.save(carParkAvailability);
        updateCacheWithLiveLotData(carParkDataCache, carParkAvailability);

    }
}
