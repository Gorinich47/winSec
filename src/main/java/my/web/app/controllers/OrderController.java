package my.web.app.controllers;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import my.web.app.models.Addres;
import my.web.app.models.Client;
import my.web.app.models.Order;
import my.web.app.models.User;
import my.web.app.repo.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/ord")
public class OrderController {

    @Autowired
    AddresRepository addresRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    // всегда будем начинать с добавления заявки

    /**
     * получение фомры авторизации, кастомная форма в login.html
     * @param model
     * @return
     */
    @GetMapping("/login")
    public String showFormLogin(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    /**
     * авторизация
     * @param user
     * @return
     */
    @PostMapping("/login")
    public String showFormLogin(User user) {
        return "redirect:/ord/list";
    }

    /**
     * выход
     * @param request
     * @param response
     * @param model
     * @return
     */
    @GetMapping("/logout")
    public String showFormLogout(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("content", "add");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "index";
    }

    /**
     * регистрация нового пользователя
     * @param model
     * @return
     */
    @GetMapping("/reg_form")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "reg_form";
    }

    /**
     * сохранение нового пользователя
     * @param user
     * @return
     */
    @PostMapping("/reg_form")
    public String processRegister(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();// кодирование пароля
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/ord/login";
    }

    /**
     * стартовая страница, заполнение заявки
     * @param model
     * @return
     */
    @GetMapping
    public String index(Model model) {
        model.addAttribute("content", "add");
        return "index";
    }

    /**
     * список всех заявок, отсортированных по полному имени клиента и по дате
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String orders(Model model) {
        var orders = orderRepository.findAllByOrderByClientFullNameAscDateAsc(); //findAllByOrderByDateAsc(); //findAll();
        System.out.println("listCars = " + orders);
        model.addAttribute("content", "list");
        model.addAttribute("listOrders", orders);
        return "index";
    }

    /**
     * список всех клиентов, сортированных по имени и фамилии
     * @param model
     * @return
     */
    @GetMapping("/clients")
    public String clients(Model model) {
            var clients = clientRepository.findAllByOrderByFirstNameAscLastNameAsc();
            model.addAttribute("content", "clients");
            model.addAttribute("listClients", clients);
            return "index";
    }

    /**
     * добавление заявки - получение формы
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
            model.addAttribute("content", "add");
            return "index";
    }

    /**
     * добавление заявки по введенным данным
     * @param firstName - фамиилия клиента
     * @param lastName - имя клиента
     * @param phoneNumber - телефон клиента
     * @param city - город, населенный пункт
     * @param street - улица
     * @param house - дом
     * @param flat - квартира, если есть
     * @param countWindows - количество окон для замера
     * @param date - дата замера
     * @param comments - комментарии к заявке
     * @return
     */
    @PostMapping("/add")
    public String saveOrder(@RequestParam String firstName,
                          @RequestParam String lastName,
                          @RequestParam String phoneNumber,
                          @RequestParam String city,
                          @RequestParam String street,
                          @RequestParam String house,
                          @RequestParam String flat,
                          @RequestParam Long countWindows,
                          @RequestParam String date,
                          @RequestParam String comments){

        // попробовали найти клиента по имени и фамилии
        Client client = clientRepository.findByFirstNameAndLastName(firstName, lastName);
        if (client == null) {  /* если не нашли, то создаем нового */
            client = clientRepository.save(new Client(firstName, lastName, phoneNumber));
            //client = new Client(firstName, lastName, phoneNumber);
        }
        // попробовали найти адрес по городу, улице, дому и квартире
        Addres addres = addresRepository.findByCityAndStreetAndHouseAndFlat(city, street, house, flat);
        if (addres == null) {  /* если не нашли, то создаем новый */
            if (city != null && street != null && house != null && flat != null) {
                addres = addresRepository.save(new Addres(city, street, house, flat));
                //addres = new Addres(city, street, house, flat);
            }
        }

        // создаем заявку и указываем клиента и адрес
        Order order = new Order();
        order.setClient(client);
        order.setAddress(addres);
        order.setDate(LocalDateTime.parse(date)); // ошибка тут
        order.setCountWindows(countWindows);
        order.setCommets(comments);

        //Order newOrder =
        orderRepository.save(order);

        return "redirect:/ord";
    }

    /**
     * удаление заявки по id
     * @param id
     * @return
     */
    @PostMapping("/del/{id}")
    public String delOrder(@PathVariable("id") Long id) {
        if (id != null) {
            orderRepository.deleteById(id); // удаляем заказ по id, клиента и адрес оставим.
        }
        return "redirect:/ord/list";
    }

}
