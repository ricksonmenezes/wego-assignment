package com.wego.assignment.controller.carparks.service;

import com.wego.assignment.controller.carparks.exception.CarParkInfoCSVSyncingException;
import com.wego.assignment.controller.carparks.model.CarPark;
import com.wego.assignment.controller.carparks.repo.CarParkInfoRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarParkInfoCSVService {

    @Autowired
    CarParkInfoRepository repository;


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
            repository.saveAll(carParks);

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
