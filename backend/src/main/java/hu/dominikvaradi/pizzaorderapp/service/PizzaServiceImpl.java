package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.dto.pizza.PizzaCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.pizza.PizzaEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;
import hu.dominikvaradi.pizzaorderapp.data.repository.PizzaRepository;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.InternalServerErrorException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public Pizza createNewPizza(PizzaCreateRequestDTO pizza, MultipartFile image) throws BadRequestException, InternalServerErrorException {
        checkImage(image);

        if (pizzaRepository.existsByName(pizza.getName())) {
            throw new BadRequestException("Pizza with name " + pizza.getName() + " already exists!");
        }

        try {
            String newFileName = saveImage(image, pizza.getName());
            Pizza newPizza = new Pizza();

            newPizza.setName(pizza.getName());
            newPizza.setDescription(pizza.getDescription());
            newPizza.setPrice(pizza.getPrice());
            newPizza.setImageName(newFileName);

            return pizzaRepository.save(newPizza);
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not store the image of pizza. Error: " + e.getMessage());
        }
    }

    @Override
    public Pizza getPizzaById(long pizzaId) throws NotFoundException {
        return pizzaRepository.findById(pizzaId).orElseThrow(() -> new NotFoundException("Pizza with given id has not found! (id = " + pizzaId + ")"));
    }

    @Override
    public void deletePizzaById(long pizzaId) throws NotFoundException {
        Optional<Pizza> pizzaToDelete = pizzaRepository.findById(pizzaId);
        if (pizzaToDelete.isPresent()) {
            pizzaRepository.delete(pizzaToDelete.get());

            try {
                Files.deleteIfExists(pizzaImagesRoot.resolve(pizzaToDelete.get().getImageName()));
            } catch (IOException ignored) {}
        } else {
            throw new NotFoundException("Pizza with given id has not found! (id = " + pizzaId + ")");
        }
    }

    @Override
    public Pizza editPizzaById(long pizzaId, PizzaEditRequestDTO pizza, MultipartFile image) throws BadRequestException, InternalServerErrorException, NotFoundException {
        if (pizzaId != pizza.getId()) {
            throw new BadRequestException("Pizza's id is not the same as the path id!");
        }

        Pizza pizzaToEdit = getPizzaById(pizzaId);

        if (pizzaRepository.existsByNameAndIdIsNot(pizza.getName(), pizzaId)) {
            throw new BadRequestException("Pizza with name " + pizza.getName() + " already exists!");
        }

        if (image != null) {
            checkImage(image);
        }

        try {
            if (pizza.getName() != null && !pizza.getName().isBlank())
                pizzaToEdit.setName(pizza.getName());

            if (image != null) {
                String newFileName = saveImage(image, pizzaToEdit.getName());
                Files.deleteIfExists(pizzaImagesRoot.resolve(pizzaToEdit.getImageName()));
                pizzaToEdit.setImageName(newFileName);
            }

            if (pizza.getDescription() != null)
                pizzaToEdit.setDescription(pizza.getDescription());

            pizzaToEdit.setPrice(pizza.getPrice());

            return pizzaRepository.saveAndFlush(pizzaToEdit);
        } catch (Exception e) {
            throw new InternalServerErrorException("Could not store the image of pizza. Error: " + e.getMessage());
        }
    }

    private void checkImage(MultipartFile image) throws BadRequestException {
        try {
            if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
                throw new BadRequestException("File " + image.getName() + " is not an image!");
            }
        } catch (NullPointerException e) {
            throw new BadRequestException("Image is required!");
        }
    }

    @Override
    public Resource getPizzaImageById(long pizzaId) throws NotFoundException, MalformedURLException {
        Pizza pizza = getPizzaById(pizzaId);
        Path pizzaImagePath = pizzaImagesRoot.resolve(pizza.getImageName());

        return new UrlResource(pizzaImagePath.toUri());
    }

    private String saveImage(MultipartFile image, String pizzaName) throws IOException {
        if (!Files.exists(pizzaImagesRoot)) {
            Files.createDirectory(pizzaImagesRoot);
        }

        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String newFileName = new Date().getTime() + "_" + pizzaName;

        String localNewFileName = newFileName;
        int tries = 5;
        boolean success = false;

        do {
            try {
                Files.copy(image.getInputStream(), pizzaImagesRoot.resolve(localNewFileName + "." + extension));

                success = true;
                newFileName = localNewFileName;
            } catch (FileAlreadyExistsException e) {
                localNewFileName = newFileName + "_" + Math.round((Math.random() * 999999) % 100000);
                --tries;
            }
        } while (!success && tries != 0);

        return newFileName + "." + extension;
    }
}
