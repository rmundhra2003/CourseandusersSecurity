package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class CourseController {

    @Autowired
    UserService userService;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/list")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("user", userService.getCurrentUser());
        return "list";
    }

    @GetMapping("/add")
    public String courseForm(Model model) {
        model.addAttribute("instructors", instructorRepository.findAll());
        model.addAttribute("course", new Course());
        return "courseform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("instructors", instructorRepository.findAll());
            model.addAttribute("user", userService.getCurrentUser());
            return "courseform";
        }
        courseRepository.save(course);
//        //Add this course to the user's courses if student
//        if(userService.getCurrentUser().getStatus().equalsIgnoreCase("student")) {
//            userService.getCurrentUser().getCourses().add(course);
//            System.out.printf("\n\nuser name is %s\n\n", userService.getCurrentUser().getUsername());
//        }
        return "redirect:/";
    }

    @GetMapping("/addInstructor")
    public String instructorForm(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "instructorform";
    }

    @PostMapping("/processInstructor")
    public String processInstructor(@Valid Instructor instructor, BindingResult result) {
        if (result.hasErrors()) {
            return "instructorform";
        }
        instructorRepository.save(instructor);
        //Save in the user's list of courses
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model) {
        model.addAttribute("course", courseRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model) {
        model.addAttribute("instructors", instructorRepository.findAll());
        model.addAttribute("course", courseRepository.findById(id));
        return "courseform";
    }

    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id) {
        if(userService.getCurrentUser().getStatus().equalsIgnoreCase("instructor")) {
            courseRepository.deleteById(id);
        } else {
        //If student delete from students list of courses
            //If this course has been added to the user then delete
            if(userService.getCurrentUser().getCourses().contains(courseRepository.findById(id)))
                userService.getCurrentUser().getCourses().remove(courseRepository.findById(id));
        }
        return "redirect:/";
    }
    @RequestMapping("/add/{id}")
    public String addCourse(@PathVariable("id") long id) {
        User user = userService.getCurrentUser();
        System.out.println("Course id is "+id);
        //If student add to  students list of courses
        if(user.getStatus().equalsIgnoreCase("student")) {
            Iterable <Course> usercourses = user.getCourses();
            User user1 = userRepository.findByIdAndCoursesId(user.getId(), id);
            if(user1 != null)
                System.out.println("Found course in sql query\n");

            for(Course c:usercourses){
                if(c.getId() == id) {
                    System.out.println("Course has already been added\n");
                    return "redirect:/";
                }
            }
               Iterable<Course>courses = courseRepository.findAll();
                for(Course c: courses) {
                    if(c.getId() == id) {
                        user.setCourse(c);
                        System.out.printf("Course %s has been added to user %s", c.getTitle(),
                                user.getUsername());
                    }
                }
                userRepository.save(userService.getCurrentUser());
                //userService.saveUser(user);
        }
        return "redirect:/";
    }

}

