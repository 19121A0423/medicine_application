package com.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.bean.AddressBean;
import com.order.constants.CommonConstants;
import com.order.entity.Address;
import com.order.exceptions.AddressNotFoundException;
import com.order.repository.AddressRepository;
import com.order.service.AddressService;

/**
 * Service implementation for managing addresses.
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private ObjectMapper mapper;

    private static Logger log = LoggerFactory.getLogger(AddressServiceImpl.class.getSimpleName());
    
    /**
     * Saves an address.
     *
     * @param addressBean The AddressBean object containing the address information to be saved.
     * @return AddressBean The saved AddressBean object.
     * @throws IllegalArgumentException if any of the address properties (city, street name, state, pin code, user ID) is empty or invalid.
     */
    @Override
    public AddressBean saveAddress(AddressBean addressBean) {
        log.info("AddressServiceImpl::SaveAddress::Started");
        if (addressBean.getCity().isEmpty() || addressBean.getStreetName().isEmpty() || addressBean.getState().isEmpty() ||
                addressBean.getPinCode()<=0 || addressBean.getUserId() <=0) {
            throw new IllegalArgumentException("Address properties cannot be empty");
        }
        else {
            Address address = new Address();
            address = mapper.convertValue(addressBean, Address.class);
            address.setStatus(CommonConstants.STATUS);
            addressRepository.save(address);
            addressBean = mapper.convertValue(address, AddressBean.class);
            log.info("AddressServiceImpl::SaveAddress::Ended");
            return addressBean;
        }
    }

    /**
     * Retrieves an address by its ID.
     * 
     * @param id The ID of the address to retrieve.
     * @return The AddressBean object representing the retrieved address.
     * @throws AddressNotFoundException if the address is not found.
     */
    @Override
    public AddressBean getAddressById(int id) throws AddressNotFoundException {
        log.info("AddressServiceImpl::getAddressById::Started");
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with ID: " + id));

        AddressBean addressBean = new AddressBean();
        addressBean = mapper.convertValue(address, AddressBean.class);
        log.info("AddressServiceImpl::getAddressById::Ended");
        return addressBean;
    }

    /**
     * Updates an existing address.
     * 
     * @param id The ID of the address to update.
     * @param updatedAddress The AddressBean object containing the updated address details.
     * @return The AddressBean object representing the updated address.
     * @throws AddressNotFoundException if the address is not found.
     */
    @Override
    public AddressBean updateAddress(int id, AddressBean updatedAddress) throws AddressNotFoundException {
        log.info("AddressServiceImpl::updateAddress::Started");
        Optional<Address> optionalAddressEntity = addressRepository.findById(id);

        if (optionalAddressEntity.isPresent()) {
            Address address = optionalAddressEntity.get();
            address = mapper.convertValue(updatedAddress, Address.class);
            address.setAddressId(id);
            addressRepository.save(address);
            updatedAddress = mapper.convertValue(address, AddressBean.class);
            log.info("AddressServiceImpl::updateAddress::Ended");
            return updatedAddress;
        } else {
            throw new AddressNotFoundException("Address not found with ID: " + id);
        }
        
    }

    /**
     * Retrieves addresses by user ID.
     * 
     * @param userId The ID of the user.
     * @return A list of AddressBean objects representing addresses associated with the user.
     * @throws AddressNotFoundException if no addresses are found for the user.
     */
    @Override
    public List<AddressBean> getAddressByUserId(int userId) throws AddressNotFoundException {
        log.info("AddressServiceImpl::getAddressByUserId::Started");
        List<Address> addresses = addressRepository.getAddressByUserId(userId);
        if(addresses.isEmpty()) {
            throw new AddressNotFoundException("No addresses found");
        }
        else {
            List<AddressBean> addressBeans = new ArrayList<>();
            addresses.stream().forEach(address -> {
                AddressBean addressBean = new AddressBean();
                addressBean = mapper.convertValue(address, AddressBean.class);
                addressBeans.add(addressBean);
            });
            log.info("AddressServiceImpl::getAddressByUserId::Ended");
            return addressBeans;
        }
    }

}

