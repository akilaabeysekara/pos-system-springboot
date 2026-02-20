package lk.ijse.posbackend.service;

import lk.ijse.posbackend.dto.ItemDTO;
import java.util.List;

public interface ItemService {

    void saveItem(ItemDTO itemDTO);
    void updateItem(ItemDTO itemDTO);
    void deleteItem(String code);
    List<ItemDTO> getAllItems();

}