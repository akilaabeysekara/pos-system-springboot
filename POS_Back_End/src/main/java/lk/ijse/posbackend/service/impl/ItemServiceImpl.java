package lk.ijse.posbackend.service.impl;

import lk.ijse.posbackend.dto.ItemDTO;
import lk.ijse.posbackend.entity.Item;
import lk.ijse.posbackend.exception.CustomException;
import lk.ijse.posbackend.repository.ItemRepository;
import lk.ijse.posbackend.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    public ItemServiceImpl(ItemRepository itemRepository,
                           ModelMapper modelMapper) {
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveItem(ItemDTO dto) {

        if (itemRepository.existsById(dto.getId())) {
            throw new CustomException("Item ID already exists");
        }

        itemRepository.save(modelMapper.map(dto, Item.class));
    }

    @Override
    public void updateItem(ItemDTO dto) {

        if (!itemRepository.existsById(dto.getId())) {
            throw new CustomException("Item not found");
        }

        itemRepository.save(modelMapper.map(dto, Item.class));
    }

    @Override
    public void deleteItem(String code) {

        if (!itemRepository.existsById(code)) {
            throw new CustomException("Item not found");
        }

        itemRepository.deleteById(code);
    }

    @Override
    public List<ItemDTO> getAllItems() {

        return itemRepository.findAll()
                .stream()
                .map(item -> modelMapper.map(item, ItemDTO.class))
                .collect(Collectors.toList());
    }
}