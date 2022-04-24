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

import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("")
    public String getAll(Model model) {
        List<Person> people = personService.listPeople();
        model.addAttribute("people", people);
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
        model.addAttribute("people", people);
        return "people/people";
    }

    @GetMapping("/add")
    public String add(Model model) {
        return "people/person-add";
    }

    @PostMapping("/add")
    public String add(@RequestBody PersonRequest personRequest) {
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
    public String update(@PathVariable("id") long id, @RequestBody PersonRequest personRequest) {
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
            return "people/person-delete-response";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пациента с id=" + id + " не существует.");
        }
    }

}
