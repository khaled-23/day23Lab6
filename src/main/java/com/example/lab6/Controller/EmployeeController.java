package com.example.lab6.Controller;

import com.example.lab6.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/employees")
    public ResponseEntity getAllEmployees(){
        if(employees.isEmpty()){
            return ResponseEntity.status(200).body("no employees");
        }else return ResponseEntity.status(200).body(employees);
    }
    @PostMapping("/add")
    public ResponseEntity addEmployee(@RequestBody @Valid Employee employee, Errors errors){
        if(errors.hasErrors()){
            String message = Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }else {
            employees.add(employee);
            return ResponseEntity.status(200).body("employee added");
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity updateEmployee(@PathVariable String id, @RequestBody @Valid Employee employee, Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        for(int i = 0; i<employees.size();i++){
            if(employees.get(i).getID().equals(id)){
                employees.set(i,employee);
                return ResponseEntity.status(200).body("employee updated");
            }
        }
        return ResponseEntity.status(400).body("employee doesn't exists");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEmployee(@PathVariable String id){
        for(int i=0; i<employees.size();i++){
            if(employees.get(i).getID().equals(id)){
                employees.remove(i);
                return ResponseEntity.status(200).body("employee removed");
            }
        }
        return ResponseEntity.status(400).body("employee doesn't exists");
    }
    @GetMapping("/get-by-position/{position}")
    public ResponseEntity searchByPosition(@PathVariable String position){
        if(!position.equalsIgnoreCase("supervisor")&&!position.equalsIgnoreCase("coordinator")){
            return ResponseEntity.status(400).body("position should be supervisor or coordinator");
        }
        ArrayList<Employee> employeesByPosition = new ArrayList<>();
        for(Employee employee:employees){
            if(employee.getPosition().equalsIgnoreCase(position)){
                employeesByPosition.add(employee);
            }
        }
        if(employeesByPosition.isEmpty()){
            return ResponseEntity.status(400).body("no employees by " + position + " position");
        }else return ResponseEntity.status(200).body(employeesByPosition);
    }
    @GetMapping("/get-by-age/{minAge}/{maxAge}")
    public ResponseEntity searchByAge(@PathVariable int minAge,@PathVariable int maxAge){
        ArrayList<Employee> employeesByAge = new ArrayList<>();
        if(minAge>maxAge){
            return ResponseEntity.status(400).body("minimum age can not be greater than maximum age");
        }

        for(Employee employee:employees){
            if(employee.getAge()>=minAge && employee.getAge()<=maxAge){
                employeesByAge.add(employee);
            }
        }
        if(employeesByAge.isEmpty()){
            return ResponseEntity.status(400).body("there is no employee by age "+minAge+" - "+maxAge);
        }else return ResponseEntity.status(200).body(employeesByAge);
    }

    @PutMapping("/apply-annual-leave/{id}")
    public ResponseEntity applyForAnnualLeave(@PathVariable String id){
        for(int i = 0; i<employees.size();i++){
            if(employees.get(i).getID().equalsIgnoreCase(id)){
                if(!employees.get(i).getOnLeave() && employees.get(i).getAnnualLeave()>0){
                    employees.get(i).setOnLeave(true);
                    employees.get(i).setAnnualLeave(employees.get(i).getAnnualLeave()-1);
                    return ResponseEntity.status(200).body("applied for leave");
                }else if(employees.get(i).getOnLeave()){
                    return ResponseEntity.status(200).body("employee already on annual leave");
                }else return ResponseEntity.status(200).body("employee can not apply for annual leave");
            }
        }
        return ResponseEntity.status(400).body("employee doesn't exists");
    }

    @GetMapping("/employees-no-leave")
    public ResponseEntity getEmployeesWithNoAnnualLeave(){
        ArrayList<Employee> employeesWithOutAnnualLeave = new ArrayList<>();
            for(Employee employee:employees){
                if(employee.getAnnualLeave()==0){
                    employeesWithOutAnnualLeave.add(employee);
                }
            }

        if(employeesWithOutAnnualLeave.isEmpty()){
            return ResponseEntity.status(200).body("there is no employee without annual leave");
        }else return ResponseEntity.status(200).body(employeesWithOutAnnualLeave);
    }
@PutMapping("/promote/{sid}/{id}")
    public ResponseEntity promoteEmployee(@PathVariable String sid, @PathVariable String id){
        for(Employee employee:employees){
            if(employee.getID().equalsIgnoreCase(sid)){
                if(!employee.getPosition().equalsIgnoreCase("supervisor")){
                    return ResponseEntity.status(400).body("employee not a supervisor");
                }
            }
        }
        for(Employee employee:employees){
            if(employee.getID().equalsIgnoreCase(id)){
                if(employee.getPosition().equalsIgnoreCase("supervisor"))
                    return ResponseEntity.status(400).body("employee already supervisor");
            }
        }

        for(int i = 0; i<employees.size(); i++){
            if(employees.get(i).getID().equalsIgnoreCase(id)){
                if(employees.get(i).getAge()>=30&&!employees.get(i).getOnLeave()){
                    employees.get(i).setPosition("supervisor");
                    return ResponseEntity.status(200).body("employee position changed to supervisor");
                }else if(employees.get(i).getOnLeave()){
                    return ResponseEntity.status(400).body("employee on leave");
                }else return ResponseEntity.status(400).body("employee age below 30");
            }
        }
        return ResponseEntity.status(400).body("there is no employee with that id");
    }


}

