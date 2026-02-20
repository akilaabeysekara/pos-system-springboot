package lk.ijse.posbackend.service.impl;

import lk.ijse.posbackend.dto.ItemDTO;
import lk.ijse.posbackend.entity.Item;
import lk.ijse.posbackend.repository.ItemRepository;
import lk.ijse.posbackend.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveItem(ItemDTO itemDTO) {
        itemRepository.save(modelMapper.map(itemDTO, Item.class));
    }

    @Override
    public void updateItem(ItemDTO itemDTO) {
        itemRepository.save(modelMapper.map(itemDTO, Item.class));
    }

    @Override
    public void deleteItem(String code) {
        itemRepository.deleteById(code);
    }

    @Override
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDTO> itemDTOS = new ArrayList<>();
        for (Item item : items) {
            itemDTOS.add(modelMapper.map(item, ItemDTO.class));
        }
        return itemDTOS;
    }
}