package dev.danvega.Controller;

import dev.danvega.DTO.ApoMedIDDTO;
import dev.danvega.DTO.MedicationActionDTO;
import dev.danvega.DTO.MedicationInfoDTO;
import dev.danvega.DTO.UserIDDTO;
import dev.danvega.Mapper.MedicationActionMapper;
import dev.danvega.Model.Apotecary;
import dev.danvega.Model.Medication;
import dev.danvega.Model.MedicationAction;
import dev.danvega.Model.MedicationInfo;
import dev.danvega.Services.MedicationActionService;
import dev.danvega.Services.MedicationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/medication-action")
public class MedicationActionController {

    @Autowired
    MedicationActionService medicationActionService = new MedicationActionService();

    @Autowired
    MedicationInfoService medicationInfoService = new MedicationInfoService();

    private final MedicationActionMapper medicationActionMapper = new MedicationActionMapper();


    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody UserIDDTO userIDDTO)
    {
        try{

            medicationActionService.delete(medicationActionService.findAllByMedicationInfo_id(userIDDTO.getId()).getId());
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Uspesno brisanje akcije!", HttpStatus.OK);

    }

    @PostMapping("/add-new")
    @Transactional
    public ResponseEntity<String> addNew(@RequestBody MedicationActionDTO medicationActionDTO)
    {
        MedicationAction existingMedicationAction = medicationActionService.findAllByMedicationInfo_id(medicationActionDTO.getMedicationInfo_id());
        if(existingMedicationAction == null){
            MedicationInfo medInfoTemp = medicationInfoService.findOne(medicationActionDTO.getMedicationInfo_id());
            MedicationAction newAction = medicationActionMapper.toEntity(medicationActionDTO, medInfoTemp);
            try{
                medicationActionService.create(newAction);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("Uspesno kreirana nova akcija", HttpStatus.OK);
        }

        else{
                MedicationInfo medInfoTemp = medicationInfoService.findOne(medicationActionDTO.getMedicationInfo_id());
                MedicationAction newAction = medicationActionMapper.toEntity(medicationActionDTO, medInfoTemp);

            try{
                medicationActionService.update(newAction, medicationActionDTO.getId());
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

                return new ResponseEntity<>("Uspesno azurirana akcija!", HttpStatus.OK);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<MedicationActionDTO>> get_all(){
        List<MedicationAction> medicationActions = medicationActionService.findAll();
        if(medicationActions == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(toMedicationInquiryDTOList(medicationActions), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/get")
    public ResponseEntity<MedicationAction> get(@RequestBody UserIDDTO userIDDTO){
        MedicationAction medAction = medicationActionService.findAllByMedicationInfo_id(userIDDTO.getId());
        return new ResponseEntity<>(medAction, HttpStatus.OK);
    }

    private List<MedicationActionDTO> toMedicationInquiryDTOList(List<MedicationAction> medicationActions){
        List<MedicationActionDTO> medicationActionDTOS = new ArrayList<>();
        for (MedicationAction medicationAction : medicationActions) {
            medicationActionDTOS.add(medicationActionMapper.toDto(medicationAction));
        }
        return medicationActionDTOS;
    }
}
