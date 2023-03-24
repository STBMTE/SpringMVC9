package com.example.marketplace;

import com.example.marketplace.models.Item;
import com.example.marketplace.models.ItemResp;
import com.example.marketplace.repository.ItemRepository;
import com.example.marketplace.service.ItemService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ServiceTests {
    @Autowired
    private ItemService itemService;
    @MockBean
    private ItemRepository itemRepository;

    @Test
    public void addItem(){
        ItemResp resp = new ItemResp();
        resp.setName("test");
        Item added = itemService.add(resp);
        assertThat(added).isNotNull();
        assertThat(added.getName().equals("test"));
    }

    @Test
    public void getItem(){
        ItemResp resp = new ItemResp();
        resp.setName("test");

        Item item = itemService.add(resp);

        assertThat(item).isNotNull();
        Optional<Item> result = Optional.of(new Item("test"));

        result.get().setItemId(3);
        Mockito.doReturn(result).when(itemRepository).findById(3);

        assertThat(result.isPresent());
        assertThat(result.get().getName().equals("test"));
    }
}
