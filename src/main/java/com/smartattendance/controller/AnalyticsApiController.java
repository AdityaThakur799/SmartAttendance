package com.smartattendance.controller;

import com.smartattendance.repository.AttendanceRecordRepository;
import com.smartattendance.repository.StudentRepository;
import com.smartattendance.model.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController        // ✅ FIXED
@RequestMapping("/api/analytics")
public class AnalyticsApiController {

    @Autowired
    private AttendanceRecordRepository attendanceRepo;

    @Autowired
    private StudentRepository studentRepo;

    // -------------------- SUMMARY API --------------------
    @GetMapping("/summary")
    public Map<String, Long> getSummary() {

        long present = attendanceRepo.countByStatus("PRESENT");
        long absent = attendanceRepo.countByStatus("ABSENT");
        long late = attendanceRepo.countByStatus("LATE");

        Map<String, Long> map = new HashMap<>();
        map.put("present", present);
        map.put("absent", absent);
        map.put("late", late);

        return map;
    }


    // -------------------- DAILY API --------------------
    @GetMapping("/daily")
    public List<Map<String, Object>> getDaily() {

        List<Object[]> raw = attendanceRepo.countDailyAttendance();
        List<Map<String, Object>> list = new ArrayList<>();

        for(Object[] row : raw){
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0].toString());
            map.put("count", ((Number) row[1]).intValue());
            list.add(map);
        }

        return list;
    }


    // -------------------- STUDENT ANALYTICS API --------------------
    @GetMapping("/student/{id}")
    public Map<String, Object> getStudentAnalytics(@PathVariable int id) {

        Student s = studentRepo.findById(id).orElse(null);

        if(s == null){
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Student not found");
            return err;
        }

        List<Object[]> monthly = attendanceRepo.countMonthlySummaryForStudent(id);
        List<Map<String, Object>> monthlyList = new ArrayList<>();

        int totalPresent = 0, totalAbsent = 0, totalLate = 0;

        for(Object[] row : monthly){
            String month = row[0].toString();
            int p = ((Number)row[1]).intValue();
            int a = ((Number)row[2]).intValue();
            int l = ((Number)row[3]).intValue();

            totalPresent += p;
            totalAbsent += a;
            totalLate += l;

            Map<String, Object> m = new HashMap<>();
            m.put("month", month);
            m.put("present", p);
            m.put("absent", a);
            m.put("late", l);

            monthlyList.add(m);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("name", s.getName());
        response.put("rollNo", s.getRollNo());
        response.put("monthlySummary", monthlyList);

        Map<String, Integer> totals = new HashMap<>();
        totals.put("present", totalPresent);
        totals.put("absent", totalAbsent);
        totals.put("late", totalLate);

        response.put("totals", totals);

        return response;
    }
}
