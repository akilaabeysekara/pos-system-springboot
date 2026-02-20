package lk.ijse.posbackend.service;

import lk.ijse.posbackend.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    public void saveItem(ItemDTO itemDTO);
    public void updateItem(ItemDTO itemDTO);
    public void deleteItem(String code);
    public List<ItemDTO> getAllItems();
}