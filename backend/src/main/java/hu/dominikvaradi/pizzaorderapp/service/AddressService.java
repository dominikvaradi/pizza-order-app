package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.Address;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.address.AddressSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;

import java.util.List;

public interface AddressService{
    void deleteAddressById(long addressId) throws NotFoundException;

    void editAddressById(long addressId, AddressSaveRequestDTO address) throws BadRequestException, NotFoundException;

    Address getAddressById(long addressId) throws NotFoundException;

    Address createNewAddress(long userId, AddressSaveRequestDTO address) throws NotFoundException, BadRequestException;

    List<Address> getAllAddressesByUserId(long userId) throws NotFoundException;
}
