package com.wego.assignment.controller.carparks.service;

import com.wego.assignment.controller.carparks.exception.CarParkInfoCSVSyncingException;
import com.wego.assignment.controller.carparks.model.CarPark;
import com.wego.assignment.controller.carparks.repo.CarParkRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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


    public void syncCarParkInfoFile() throws CarParkInfoCSVSyncingException {

        InputStream is = null;
        try {
            //download file

            //readfile

            //add some limit to the file being read into
            /*File file = ResourceUtils.getFile("classpath:csv/HDBCarparkInformation.csv");
            is = new FileInputStream(file);*/
            Resource resource = new ClassPathResource("csv/HDBCarparkInformation.csv");
            List<CarPark> carParks = csvToCarParkInfo(resource.getInputStream());
            for( CarPark carPark :  carParks) {

                CarPark carParkToBeSaved  = null;
                Optional<CarPark> carParkFromDbOpt = repository.findById(carPark.getCar_park_no());
                if(! carParkFromDbOpt.isPresent()) {

                    carParkToBeSaved = carPark;
                } else {
                    carParkToBeSaved = carParkFromDbOpt.get();
                    carParkToBeSaved.setAddress(carPark.getAddress());
                    //fixme: if coord changes we need to pull laltlong again
                    carParkToBeSaved.setX_coord(carPark.getX_coord());
                    carParkToBeSaved.setY_coord(carPark.getY_coord());
                    carParkToBeSaved.setCar_park_basement(carPark.getCar_park_basement());
                    carParkToBeSaved.setCar_park_decks(carPark.getCar_park_decks());
                    carParkToBeSaved.setCar_park_type(carPark.getCar_park_type());
                    carParkToBeSaved.setFree_parking(carPark.getFree_parking());
                    carParkToBeSaved.setNight_parking(carPark.getNight_parking());
                    carParkToBeSaved.setGantry_height(carPark.getGantry_height());
                    carParkToBeSaved.setShort_term_parking(carPark.getShort_term_parking());
                    carParkToBeSaved.setType_of_parking_system(carPark.getType_of_parking_system());
                }
                repository.save(carParkToBeSaved);
            }


            System.out.print("hello");

        } catch (FileNotFoundException e){

            throw new CarParkInfoCSVSyncingException(e.getMessage(),e);

        }catch (Exception e) {
            throw new CarParkInfoCSVSyncingException(e.getMessage(),e);
        }

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
                        csvRecord.get("car_park_no"), "add:" + csvRecord.get("address"), csvRecord.get("x_coord"), csvRecord.get("y_coord"), csvRecord.get("car_park_type"),
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
