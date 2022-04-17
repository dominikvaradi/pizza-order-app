package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.dto.pizza.PizzaSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.InternalServerErrorException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;

import java.util.List;

public interface PizzaService {
    public List<Pizza> getAllPizzas();

    public Pizza createNewPizza(PizzaSaveRequestDTO pizza) throws BadRequestException, InternalServerErrorException;

    public Pizza getPizzaById(long id) throws NotFoundException;

    public void deletePizzaById(long id) throws NotFoundException;

    public void editPizzaById(long id, PizzaSaveRequestDTO pizza) throws BadRequestException, InternalServerErrorException, NotFoundException;
}
