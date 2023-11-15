package com.wego.assignment.controller.carparks.service;

import com.wego.assignment.common.view.LatLong;
import com.wego.assignment.controller.carparks.exception.CarParkAPIException;
import com.wego.assignment.controller.carparks.exception.CarParkDBException;
import com.wego.assignment.controller.carparks.exception.CarParkException;
import com.wego.assignment.controller.carparks.model.CarPark;
import com.wego.assignment.controller.carparks.model.CarParkAvailability;
import com.wego.assignment.controller.carparks.model.CarParkAvailabilityID;
import com.wego.assignment.controller.carparks.repo.CarParkAvailabilityRepo;
import com.wego.assignment.controller.carparks.repo.CarParkRepo;
import com.wego.assignment.controller.carparks.repo.CarParkRepository;
import com.wego.assignment.controller.carparks.view.*;
import com.wego.assignment.controller.carparks.view.carparkavailability.CarParkData;
import com.wego.assignment.controller.carparks.view.carparkavailability.CarParkInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class CarParkService  {

    @Autowired
    CarParkInfoCSVService carParkInfoCSVService;

    @Autowired
    CarParkAvailabilityRepo carParkAvailabilityRepo;

    @Autowired
    CarParkRepository carParkRepository;

    @Autowired
    CarparkServiceAPI carparkServiceAPI;

    @Autowired
    CarParkCacheService carParkCacheService;

    @Autowired
    CarParkRepo jdbcRepo;


    public LatLong convertSVY21ToLatLong(Double x, Double y) throws CarParkException {

        try {

            return carparkServiceAPI.getLatLongFromSvy21FromOneMap(x, y);

        } catch (CarParkAPIException e) {

            throw new CarParkException("Calling conversion API from one map failed " +  e.getMessage(),e);

        }catch (Exception e) {

            throw new CarParkException(e.getMessage(),e);
        }
    }


    /* carparkDataMap is a cache that consists of CarPark total and available slots for each lotType of a CarPark Number
       Why are we maintainint it? If the task to update live data runs every 30 seconds , we will have around 2000-4000 unnecessary updates every half minute
      If there were 20,000 car parks, that would mean almost 20-50K updates (some carpark nos have 1-3 slots). Hence we maintain a cache to only update car park that have lots changed
    * Cases: If cache doesn't contain CarParkNo key, it will be saved
    *        If carParkNo cache exists in cache but the lot type doesn't exist, slotTpe along with total and available lots will be added to CarParkAvailability  table and cache key updated
     *        If CarParNo cache exists in cache and the lot type exists and the total or available lots for that lot type has changed, it will be updated to CPA table and cache key updated
     *        If CarParNo cache exists in cache and the lot type exists and the total or available lots for that lot type has not changed, it will skip this live data */
    public void syncCarParkAvailability() throws CarParkException {

        try {

            CarParkAvailabilityResponse liveCarParkDataResponse = carparkServiceAPI.getCarParkAvailability();

            if (liveCarParkDataResponse.getItems() == null || liveCarParkDataResponse.getItems().isEmpty()) {
                throw new CarParkException("no items were found inside car park availability API");
            }

            List<CarParkData> liveCarParkLotsData = liveCarParkDataResponse.getItems().get(0).getCarParkData();

            if (liveCarParkLotsData == null) {
                throw new CarParkException("no live carpark data was found for any car park number in the API response");
            }


            Date createdDate = new Date();

            /*List<String> carParkNos = new ArrayList<>();
            List<String> carParkNosHits = new ArrayList<>();*/

            for (CarParkData liveCarParkData : liveCarParkLotsData) {

                String carparkNo = liveCarParkData.getCarparkNumber();

                List<CarParkInfo> liveCarParkLotInfos = liveCarParkData.getCarparkInfo();

                for (final CarParkInfo liveCarParkLotInfo : liveCarParkLotInfos) {

                    CarParkDataCache carParkDataCache = carParkCacheService.getCarparkDataMap().get(carparkNo);


                    if (carParkCacheService.carParkCacheChanged(carParkDataCache, liveCarParkLotInfo)) {

                        //fixme: prepare a util method
                        Optional<CarParkAvailability> carParkLiveDataOpt = carParkAvailabilityRepo.findById(new CarParkAvailabilityID(carparkNo, liveCarParkLotInfo.getLotType()));
                        CarParkAvailability carParkAvailability = new CarParkAvailability();
                        if(carParkLiveDataOpt.isPresent()) {

                             carParkAvailability = carParkLiveDataOpt.get();
                        }  else {
                            carParkAvailability.setCreatedDate(createdDate);
                        }

                        carParkAvailability.setCarParkNo(carparkNo);
                        carParkAvailability.setAvailableLots(liveCarParkLotInfo.getLotsAvailable());
                        carParkAvailability.setTotalLots(liveCarParkLotInfo.getTotalLots());
                        carParkAvailability.setLotType(liveCarParkLotInfo.getLotType());
                        carParkAvailability.setModifiedDate(createdDate);

                        try {
                            carParkCacheService.updateCarParkAvailability(carParkAvailability, carParkDataCache);
                            //carParkNosHits.add(carparkNo);

                        } catch (Exception e) {

                            //evicting entry of carpark no as it threw exception.
                            carParkCacheService.getCarparkDataMap().remove(carparkNo);

                            throw new CarParkException("something went wrong when updating CarparkAvailability with live data " + e.getMessage(), e);
                        }

                    }
                }
            }


        } catch (Exception e) {
            throw new CarParkException(e.getMessage(),e);
        }

    }


    public void saveCarParkInfoCSV()  throws CarParkException {

        try {
            carParkInfoCSVService.syncCarParkInfoFile();
        } catch (Exception e) {
            throw new CarParkException(e.getMessage(),e);

        }

    }

    public void updateCarParksLatLong() {

        List<CarPark> carParks = carParkRepository.findAll();
        //use parallel streams
        for(CarPark carPark : carParks) {

            if(carPark.getLongitude() == null || carPark.getLatitude() == null) {

                try {
                    LatLong latLong = convertSVY21ToLatLong(new Double(carPark.getX_coord()), new Double(carPark.getY_coord()));

                } catch (CarParkException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<CarPark> getAllCarParks() {

        return  carParkInfoCSVService.getAllCarParks();
    }

    public List<NearestCarPark> nearestPoint(Double latitude, Double longitude, int page, int per_page) throws CarParkException {


        try {
            if (latitude == null || longitude == null || (latitude < -90 || latitude > 90) || (longitude < -180 || longitude > 180)) {
                throw new CarParkException("provided lat long are not valid.");
            }

            page = page - 1;
            Pageable pageable = PageRequest.of(page, per_page);
            Page<NearestCarPark> dataPage = jdbcRepo.findDemoByPage(latitude, longitude, pageable);
            return dataPage.getContent();
        } catch (CarParkDBException e) {
            throw new CarParkException("Something went wrong when querying database to find nearest car parks");
        } catch (Exception e) {
            throw new CarParkException(e.getMessage(),e);
        }


    }

}
