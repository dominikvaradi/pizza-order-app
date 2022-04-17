package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.Address;
import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.address.AddressSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.repository.AddressRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.UserRepository;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class AddressServiceImpl implements AddressService {
    public static final int MAX_ADDRESS_COUNT_USER_CAN_HAVE = 5;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void deleteAddressById(long addressId) throws NotFoundException {
        if (addressRepository.existsById(addressId)) {
            addressRepository.deleteById(addressId);
        } else {
            throw new NotFoundException("Address with given id has not found! (id = " + addressId + ")");
        }
    }

    @Override
    public void editAddressById(long addressId, AddressSaveRequestDTO address) throws BadRequestException, NotFoundException {
        if (addressId != address.getId()) {
            throw new BadRequestException("Address's id is not the same as the path id!");
        }

        Address addressToEdit = addressRepository.findById(addressId)
            .orElseThrow(() -> new NotFoundException("Address with given id has not found! (id = " + addressId + ")"));

        addressToEdit.setZipCode(addressToEdit.getZipCode());
        addressToEdit.setCity(address.getCity());
        addressToEdit.setHouseNumber(addressToEdit.getHouseNumber());
        addressToEdit.setStreet(addressToEdit.getStreet());

        addressRepository.save(addressToEdit);
    }

    @Override
    public Address getAddressById(long addressId) throws NotFoundException {
        return addressRepository.findById(addressId)
            .orElseThrow(() -> new NotFoundException("Address with given id has not found! (id = " + addressId + ")"));
    }

    @Override
    public Address createNewAddress(long userId, AddressSaveRequestDTO address) throws NotFoundException, BadRequestException {
        if (address.getUserId() != userId) {
            throw new BadRequestException("Address's userId is not the same as the path userId!");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User with given id has not found! (id = " + userId + ")"));

        List<Address> addressList = user.getAddresses();

        if (addressList.size() >= MAX_ADDRESS_COUNT_USER_CAN_HAVE) {
            throw new BadRequestException("User can't have more than " + MAX_ADDRESS_COUNT_USER_CAN_HAVE + " addresses!");
        }

        Address newAddress = new Address(address.getZipCode(), address.getCity(), address.getStreet(), address.getHouseNumber(), user);
        addressList.add(newAddress);

        userRepository.save(user);

        return newAddress;
    }

    @Override
    public List<Address> getAllAddressesByUserId(long userId) throws NotFoundException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User with given id has not found! (id = " + userId + ")"));

        return user.getAddresses();
    }
}
