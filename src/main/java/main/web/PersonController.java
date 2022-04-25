package main.web;

import main.entity.Person;
import main.entity.request.PersonRequest;
import main.exception.DiagnosisNotExistsException;
import main.exception.PersonNotExistsException;
import main.exception.WardNotExistsException;
import main.service.PersonService;
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
@RequestMapping("/people")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("")
    public String getList(@RequestParam(value = "text", required = false) String text,
                          Model model) {
        List<Person> people = null;
        String message = "";
        if (text != null) {
            people = new ArrayList<>();
            try {
                long id = Long.parseLong(text);
                try {
                    people.add(personService.findPerson(id));
                    message = "Результаты поиска по id=" + id;
                } catch (PersonNotExistsException e) {
                    message = "Пациента с id=" + id + " не найдено";
                }
            } catch (NumberFormatException e) {
                people.addAll(personService.findByLastName(text));
                message = "Результаты поиска по фамилии " + text;
            }
        } else {
            people = personService.listPeople();
            message = "Все пациенты";
        }
        model.addAttribute("people", people);
        model.addAttribute("message", message);
        return "people/people";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") long id, Model model) {
        try {
            Person person = personService.findPerson(id);
            model.addAttribute("person", person);
        } catch (PersonNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "people/person";
    }

    @GetMapping("/getWithDiagnosis")
    public String getWithDiagnosis(@RequestParam(value = "diagnosisId", required = true) long diagnosisId, Model model) {
        List<Person> people = personService.listPeopleWithDiagnosis(diagnosisId);
        String message = "Пациенты с диагнозом id=" + diagnosisId;
        model.addAttribute("people", people);
        model.addAttribute("message", message);
        return "people/people";
    }

    @GetMapping("/add")
    public String add(Model model) {
        return "people/person-add";
    }

    @PostMapping("/add")
    public String add(PersonRequest personRequest) {
        Pair<Person, Boolean> pair;
        try {
            pair = personService.addPerson(personRequest);
        } catch (WardNotExistsException | DiagnosisNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/people/" + pair.getFirst().getId();
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") long id, Model model) {
        try {
            Person person = personService.findPerson(id);
            model.addAttribute("person", person);
        } catch (PersonNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "people/person-update";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") long id, PersonRequest personRequest) {
        try {
            personService.updatePerson(id, personRequest);
        } catch (PersonNotExistsException | WardNotExistsException | DiagnosisNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return "redirect:/people/" + id;
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id, Model model) {
        if (personService.deletePerson(id)) {
            model.addAttribute("message", "Пациент с id=" + id + " удален.");
            return "message";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пациента с id=" + id + " не существует.");
        }
    }

}
