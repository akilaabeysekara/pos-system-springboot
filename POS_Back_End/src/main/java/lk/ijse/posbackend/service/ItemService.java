package lk.ijse.posbackend.service;

import lk.ijse.posbackend.dto.ItemDTO;
import java.util.List;

public interface ItemService {

    boolean saveItem(ItemDTO itemDTO);
    boolean updateItem(ItemDTO itemDTO);
    boolean deleteItem(String code);
    List<ItemDTO> getAllItems();
}