package lk.ijse.posbackend.service.impl;

import lk.ijse.posbackend.dto.ItemDTO;
import lk.ijse.posbackend.entity.Item;
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
    public boolean saveItem(ItemDTO dto) {

        if (itemRepository.existsById(dto.getId())) {
            return false;
        }

        itemRepository.save(modelMapper.map(dto, Item.class));
        return true;
    }

    @Override
    public boolean updateItem(ItemDTO dto) {

        if (!itemRepository.existsById(dto.getId())) {
            return false;
        }

        itemRepository.save(modelMapper.map(dto, Item.class));
        return true;
    }

    @Override
    public boolean deleteItem(String code) {

        if (!itemRepository.existsById(code)) {
            return false;
        }

        itemRepository.deleteById(code);
        return true;
    }

    @Override
    public List<ItemDTO> getAllItems() {

        return itemRepository.findAll()
                .stream()
                .map(item -> modelMapper.map(item, ItemDTO.class))
                .collect(Collectors.toList());
    }
}