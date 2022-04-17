package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.pizza.PizzaSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.repository.PizzaRepository;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.InternalServerErrorException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class PizzaServiceImpl implements PizzaService {
    public static final Path pizzaImagesRoot = Paths.get("pizza-images");

    private final PizzaRepository pizzaRepository;

    public PizzaServiceImpl(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public List<Pizza> getAllPizzas() {
        return pizzaRepository.findAll();
    }

    @Override
    public Pizza createNewPizza(PizzaSaveRequestDTO pizza) throws BadRequestException, InternalServerErrorException {
        try {
            if (!Objects.requireNonNull(pizza.getImage().getContentType()).startsWith("image/")) {
                throw new BadRequestException("File " + pizza.getImage().getName() + " is not an image!");
            }
        } catch (NullPointerException e) {
            throw new BadRequestException("Image is required!");
        }

        if (pizzaRepository.existsByName(pizza.getName())) {
            throw new BadRequestException("Pizza with name " + pizza.getName() + " already exists!");
        }

        try {
            Pizza newPizza = new Pizza();
            savePizza(pizza, newPizza);

            return newPizza;
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not store the image of pizza. Error: " + e.getMessage());
        }
    }

    @Override
    public Pizza getPizzaById(long id) throws NotFoundException {
        return pizzaRepository.findById(id).orElseThrow(() -> new NotFoundException("Pizza with given id has not found! (id = " + id + ")"));
    }

    @Override
    public void deletePizzaById(long id) throws NotFoundException {
        if (pizzaRepository.existsById(id)) {
            pizzaRepository.deleteById(id);
        } else {
            throw new NotFoundException("Pizza with given id has not found! (id = " + id + ")");
        }
    }

    @Override
    public void editPizzaById(long id, PizzaSaveRequestDTO pizza) throws BadRequestException, InternalServerErrorException, NotFoundException {
        if (id != pizza.getId()) {
            throw new BadRequestException("Pizza's id is not the same as the path id!");
        }

        Pizza pizzaToEdit = getPizzaById(id);

        if (pizzaRepository.existsByNameAndIdIsNot(pizza.getName(), id)) {
            throw new BadRequestException("Pizza with name " + pizza.getName() + " already exists!");
        }

        try {
            if (pizza.getImage() != null && !Objects.requireNonNull(pizza.getImage().getContentType()).startsWith("image/")) {
                throw new BadRequestException("File " + pizza.getImage().getName() + " is not an image!");
            }
        } catch (NullPointerException e) {
            throw new BadRequestException("File " + pizza.getImage().getName() + " has not got any content type!");
        }

        try {
            Files.deleteIfExists(pizzaImagesRoot.resolve(pizzaToEdit.getImageName()));

            savePizza(pizza, pizzaToEdit);
        } catch (Exception e) {
            throw new InternalServerErrorException("Could not store the image of pizza. Error: " + e.getMessage());
        }
    }

    private String createImageFile(PizzaSaveRequestDTO pizza) throws IOException {
        String extension = StringUtils.getFilenameExtension(pizza.getImage().getOriginalFilename());
        String newFileName = new Date().getTime() + "_" + pizza.getName();

        String localNewFileName = newFileName;
        boolean success = false;
        do {
            try {
                Files.copy(pizza.getImage().getInputStream(), pizzaImagesRoot.resolve(localNewFileName + "." + extension));

                success = true;
                newFileName = localNewFileName;
            } catch (FileAlreadyExistsException e) {
                localNewFileName = newFileName + "_" + Math.round((Math.random() * 999999) % 100000);
            }
        } while (!success);

        return newFileName + "." + extension;
    }

    private void savePizza(PizzaSaveRequestDTO pizza, Pizza pizzaToSave) throws IOException {
        if (!Files.exists(pizzaImagesRoot)) {
            Files.createDirectory(pizzaImagesRoot);
        }

        String newFileName = createImageFile(pizza);

        pizzaToSave.setName(pizza.getName());
        pizzaToSave.setDescription(pizza.getDescription());
        pizzaToSave.setPrice(pizza.getPrice());
        pizzaToSave.setImageName(newFileName);

        pizzaRepository.save(pizzaToSave);
    }
}
