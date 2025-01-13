package com.ohgiraffers.washplan.machine.controller;

import com.ohgiraffers.washplan.machine.model.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/machine")
public class MachineController {
    
    private final MachineService machineService;
    
    @Autowired
    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }
    
    @GetMapping("/status/{machineNo}")
    @ResponseBody
    public String getMachineStatus(@PathVariable int machineNo) {
        return machineService.getMachineStatus(machineNo);
    }
}
