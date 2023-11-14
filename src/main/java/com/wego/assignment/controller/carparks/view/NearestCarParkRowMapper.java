package com.wego.assignment.controller.carparks.view;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NearestCarParkRowMapper implements RowMapper<NearestCarPark> {


    @Override
    public NearestCarPark mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NearestCarPark(
                rs.getString("carParkNo"),
                rs.getString("address"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getInt("totalLots"),
                rs.getInt("availableLots")

        );
    }
}
