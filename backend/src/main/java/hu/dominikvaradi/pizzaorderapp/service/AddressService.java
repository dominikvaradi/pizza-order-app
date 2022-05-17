package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.dto.address.AddressCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Address;
import hu.dominikvaradi.pizzaorderapp.data.dto.address.AddressEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;

import java.util.List;

public interface AddressService{
    void deleteAddressById(long addressId) throws NotFoundException;

    Address editAddressById(long addressId, AddressEditRequestDTO address) throws BadRequestException, NotFoundException;

    Address getAddressById(long addressId) throws NotFoundException;

    Address createNewAddress(long userId, AddressCreateRequestDTO address) throws NotFoundException, BadRequestException;

    List<Address> getAllAddressesByUserId(long userId) throws NotFoundException;
}
