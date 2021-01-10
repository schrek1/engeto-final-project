package com.engeto.bus_reservation.service;

import com.engeto.bus_reservation.model.TimetableEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TimetableHolder {

    private List<TimetableEntry> timetable = new ArrayList<>();


    public void setTimetable(List<TimetableEntry> entries) {
        if (entries != null) {
            this.timetable.clear();
            this.timetable.addAll(entries);
        }
    }

    public List<TimetableEntry> getTimetable() {
        return timetable;
    }

    public Set<String> findTargetsFromCity(String cityName) {
        return timetable.stream()
                .filter(entry -> entry.from.equals(cityName))
                .map(TimetableEntry::getTo)
                .collect(Collectors.toSet());
    }

    public Set<String> findTimesOfDeparturesForLine(String departureCityName, String arrivalCityName) {
        return timetable.stream()
                .filter(entry -> entry.from.equals(departureCityName))
                .filter(entry -> entry.to.equals(arrivalCityName))
                .map(TimetableEntry::getTime)
                .collect(Collectors.toSet());
    }
}
