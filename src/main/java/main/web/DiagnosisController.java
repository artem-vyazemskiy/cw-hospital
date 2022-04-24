package main.web;

import main.entity.Diagnosis;
import main.entity.request.DiagnosisRequest;
import main.exception.DiagnosisIsUsedException;
import main.exception.DiagnosisNotExistsException;
import main.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/diagnoses")
public class DiagnosisController {

    @Autowired
    private DiagnosisService diagnosisService;

    @GetMapping("")
    public String getList(@RequestParam(value = "text", required = false) String text, Model model) {
        List<Diagnosis> diagnoses = new ArrayList<>();
        if (text != null) {
            try {
                long id = Long.parseLong(text);
                diagnoses.add(diagnosisService.findDiagnosis(id));
            } catch (NumberFormatException | DiagnosisNotExistsException ignored) {}
            try {
                diagnoses.add(diagnosisService.findDiagnosis(text));
            } catch (DiagnosisNotExistsException ignored) {}
        } else {
            diagnoses = diagnosisService.listDiagnosis();
        }
        model.addAttribute("diagnoses", diagnoses);
        return "diagnoses/diagnoses";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") long id, Model model) {
        try {
            Diagnosis diagnosis = diagnosisService.findDiagnosis(id);
            model.addAttribute("diagnosis", diagnosis);
            return "diagnoses/diagnosis";
        } catch (DiagnosisNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/add")
    public String add(Model model) {
        return "diagnoses/diagnosis-add";
    }

    @PostMapping("/add")
    public String add(DiagnosisRequest diagnosisRequest, Model model) {
        Pair<Diagnosis, Boolean> pair = diagnosisService.addDiagnosis(diagnosisRequest);
        return "redirect:/diagnoses/" + pair.getFirst().getId();
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id, Model model) {
        try {
            if (diagnosisService.deleteDiagnosis(id)) {
                model.addAttribute("message", "Диагноз с id=" + id + " удален.");
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Диагноза с id=" + id + " не существует.");
            }
        } catch (DiagnosisIsUsedException e) {
            model.addAttribute("message", "Диагноз с id=" + id + " используется. Удаление невозможно.");
        }
        return "diagnoses/diagnosis-delete-response";
    }

}
