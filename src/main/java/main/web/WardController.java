package main.web;

import main.entity.Ward;
import main.entity.request.WardRequest;
import main.exception.WardAlreadyExistsException;
import main.exception.WardIsUsedException;
import main.exception.WardNotExistsException;
import main.service.WardService;
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
@RequestMapping("/wards")
public class WardController {

    @Autowired
    private WardService wardService;

    @GetMapping("")
    public String getList(@RequestParam(value = "filter", required = true) String filter,
                          @RequestParam(value = "text", required = false) String text,
                          Model model) {
        List<Ward> wards = null;
        String message = "";
        switch (filter) {
            case "all": {
                wards = wardService.listWards();
                message = "Все палаты";
                break;
            }
            case "notFull": {
                wards = wardService.listNotFullWards();
                message = "Не заполненные палаты";
                break;
            }
            case "withOneDiagnosis": {
                wards = wardService.listWardsWithOneDiagnosis();
                message = "Палаты с одним диагнозом";
                break;
            }
            case "withDifferentDiagnoses": {
                wards = wardService.listWardsWithDifferentDiagnoses();
                message = "Палаты с разными диагнозами";
                break;
            }
            case "search": {
                wards = new ArrayList<>();
                try {
                    long id = Long.parseLong(text);
                    try {
                        wards.add(wardService.findWard(id));
                        message = "Результаты поиска по id=" + id;
                    } catch (WardNotExistsException e) {
                        message = "Палаты с id=" + id + " не найдено";
                    }
                } catch (NumberFormatException e) {
                    try {
                        wards.add(wardService.findWard(text));
                        message = "Результаты поиска по имени " + text;
                    } catch (WardNotExistsException ex) {
                        message = "Палаты с именем " + text + " не найдено";
                    }
                }
                break;
            }
            default: {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong filter");
            }
        }
        model.addAttribute("wards", wards);
        model.addAttribute("message", message);
        return "wards/wards";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") long id, Model model) {
        try {
            Ward ward = wardService.findWard(id);
            model.addAttribute("ward", ward);
            return "wards/ward";
        } catch (WardNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getWithDiagnosis")
    public String getWithDiagnosis(@RequestParam(value = "diagnosisId", required = true) long diagnosisId, Model model) {
        List<Ward> wards = wardService.listWardsWithDiagnosis(diagnosisId);
        String message = "Палаты, в которых есть пациенты с диагнозом id=" + diagnosisId;
        model.addAttribute("wards", wards);
        model.addAttribute("message", message);
        return "wards/wards";
    }

    @GetMapping("/add")
    public String add(Model model) {
        return "wards/ward-add";
    }

    @PostMapping("/add")
    public String add(WardRequest wardRequest, Model model) {
        try {
            Pair<Ward, Boolean> pair = wardService.addWard(wardRequest);
            return "redirect:/wards/" + pair.getFirst().getId();
        } catch (WardAlreadyExistsException e) {
            model.addAttribute("message", e.getMessage());
            return "message";
        }
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") long id, Model model) {
        try {
            Ward ward = wardService.findWard(id);
            model.addAttribute("ward", ward);
        } catch (WardNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "wards/ward-update";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") long id, WardRequest wardRequest) {
        try {
            wardService.updateWard(id, wardRequest);
        } catch (WardNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/wards/" + id;
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id, Model model) {
        try {
            if (wardService.deleteWard(id)) {
                model.addAttribute("message", "Палата с id=" + id + " удалена.");
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Палаты с id=" + id + " не существует.");
            }
        } catch (WardIsUsedException e) {
            model.addAttribute("message", "Палата с id=" + id + " используется. Удаление невозможно.");
        }
        return "message";
    }

}
