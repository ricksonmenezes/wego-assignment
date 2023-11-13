package com.wego.assignment.controller.carparks.service;

import com.wego.assignment.common.view.LatLong;
import com.wego.assignment.controller.carparks.exception.CarParkAPIException;
import com.wego.assignment.controller.carparks.exception.CarParkException;
import com.wego.assignment.controller.carparks.exception.CarParkInfoCSVSyncingException;
import com.wego.assignment.controller.carparks.helper.LatLonCoordinate;
import com.wego.assignment.controller.carparks.helper.SVY21;
import com.wego.assignment.controller.carparks.model.CarPark;
import com.wego.assignment.controller.carparks.repo.CarParkRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarParkInfoCSVService {

    @Autowired
    CarParkRepository repository;

    @Autowired
    CarparkServiceAPI carparkServiceAPI;

    @Autowired
    TaskExecutor taskExecutor;


    public void syncCarParkInfoFile() throws CarParkInfoCSVSyncingException {

        InputStream is = null;
        try {
            //download file

            //readfile

            //add some limit to the file being read into
            /*File file = ResourceUtils.getFile("classpath:csv/HDBCarparkInformation.csv");
            is = new FileInputStream(file);*/
            Resource resource = new ClassPathResource("csv/HDBCarparkInformation.csv");
            List<CarPark> carParksFromCSV = csvToCarParkInfo(resource.getInputStream());

            for( CarPark carParkFromCSV :  carParksFromCSV) {

                taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {

                        CarPark carParkToBeSaved  = null;
                        Optional<CarPark> carParkFromDbOpt = repository.findById(carParkFromCSV.getCar_park_no());
                        if(! carParkFromDbOpt.isPresent()) {

                            carParkToBeSaved = carParkFromCSV;
                        } else {
                            carParkToBeSaved = carParkFromDbOpt.get();
                            carParkToBeSaved.setAddress(carParkFromCSV.getAddress());
                            //fixme: if coord changes we need to pull laltlong again
                            carParkToBeSaved.setX_coord(carParkFromCSV.getX_coord());
                            carParkToBeSaved.setY_coord(carParkFromCSV.getY_coord());
                            carParkToBeSaved.setCar_park_basement(carParkFromCSV.getCar_park_basement());
                            carParkToBeSaved.setCar_park_decks(carParkFromCSV.getCar_park_decks());
                            carParkToBeSaved.setCar_park_type(carParkFromCSV.getCar_park_type());
                            carParkToBeSaved.setFree_parking(carParkFromCSV.getFree_parking());
                            carParkToBeSaved.setNight_parking(carParkFromCSV.getNight_parking());
                            carParkToBeSaved.setGantry_height(carParkFromCSV.getGantry_height());
                            carParkToBeSaved.setShort_term_parking(carParkFromCSV.getShort_term_parking());
                            carParkToBeSaved.setType_of_parking_system(carParkFromCSV.getType_of_parking_system());

                            //if CarPark Info is being saved for first time or if CarPark has shifted i.e its X,Y cord is different from one in DB, then call converto4326 API to update lat long
                            if(latlongHasChanged(carParkToBeSaved, carParkFromCSV)) {

                                LatLong latLong = null;
                                try {
                                    //latLong = carparkServiceAPI.getLatLongFromSvy21FromOneMap(new Double(carParkFromCSV.getX_coord()), new Double(carParkFromCSV.getY_coord()));

                                    Double easting = new Double(carParkFromCSV.getX_coord());
                                    Double northing = new Double(carParkFromCSV.getY_coord());

                                    LatLonCoordinate latLonCoordinate = SVY21.computeLatLon(northing, easting);
                                    carParkToBeSaved.setLatitude(latLonCoordinate.getLatitude());
                                    carParkToBeSaved.setLongitude(latLonCoordinate.getLongitude());

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }


                            }

                        }

                        //fixme: version field does not update on save()
                        repository.save(carParkToBeSaved);

                    }
                });

            }


        } catch (FileNotFoundException e){

            throw new CarParkInfoCSVSyncingException(e.getMessage(),e);

        }catch (Exception e) {
            e.printStackTrace();
            throw new CarParkInfoCSVSyncingException(e.getMessage(),e);
        }

    }

    private boolean latlongHasChanged(CarPark carParkFromDB, CarPark carParkFromCSV) {

        if(carParkFromDB.getLatitude() == null || carParkFromDB.getLongitude() == null ||
                !carParkFromCSV.getX_coord().equalsIgnoreCase(carParkFromDB.getX_coord()) || !carParkFromDB.getY_coord().equalsIgnoreCase(carParkFromCSV.getY_coord())) {

            return  true;
        }
        return  false;
    }

    public List<CarPark> getAllCar() {
        return null;
    }

    public static String TYPE = "text/csv";
    static String[] HEADERs = { "car_park_no","address","x_coord","y_coord","car_park_type","type_of_parking_system","short_term_parking","free_parking","night_parking","car_park_decks","gantry_height","car_park_basement"};

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<CarPark> csvToCarParkInfo(InputStream is) throws CarParkInfoCSVSyncingException {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<CarPark> carParks = new ArrayList<CarPark>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                CarPark carPark = new CarPark(
                        csvRecord.get("car_park_no"), csvRecord.get("address"), csvRecord.get("x_coord"), csvRecord.get("y_coord"), csvRecord.get("car_park_type"),
                        csvRecord.get("type_of_parking_system"), csvRecord.get("short_term_parking"), csvRecord.get("free_parking"),
                        csvRecord.get("night_parking"), csvRecord.get("car_park_decks"), csvRecord.get("gantry_height"), csvRecord.get("car_park_basement"));


                carParks.add(carPark);
            }

            return carParks;
        } catch (IOException e) {
            throw new CarParkInfoCSVSyncingException("fail to parse CSV file for car parks info: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CarParkInfoCSVSyncingException("fail to parse CSV file for car parks info: " + e.getMessage(), e);
        }
    }

    public List<CarPark> getAllCarParks() {

        return  repository.findAll();
    }
}
