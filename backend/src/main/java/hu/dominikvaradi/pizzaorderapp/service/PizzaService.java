package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.dto.pizza.PizzaCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;
import hu.dominikvaradi.pizzaorderapp.data.dto.pizza.PizzaEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.InternalServerErrorException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface PizzaService {
    public List<Pizza> getAllPizzas();

    public Pizza createNewPizza(PizzaCreateRequestDTO pizza, MultipartFile image) throws BadRequestException, InternalServerErrorException;

    public Pizza getPizzaById(long pizzaId) throws NotFoundException;

    public void deletePizzaById(long pizzaId) throws NotFoundException;

    public Pizza editPizzaById(long pizzaId, PizzaEditRequestDTO pizza, MultipartFile image) throws BadRequestException, InternalServerErrorException, NotFoundException;

    public Resource getPizzaImageById(long pizzaId) throws NotFoundException, MalformedURLException;
}
