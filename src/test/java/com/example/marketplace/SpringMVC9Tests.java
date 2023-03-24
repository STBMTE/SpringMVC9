package com.example.marketplace;

import com.example.marketplace.controlers.RESTController;
import com.example.marketplace.models.Item;
import com.example.marketplace.models.ItemResp;
import com.example.marketplace.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
class SpringMVC9Tests {


    @Autowired
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RESTController restController;

    @Test
    void defaultTest() throws Exception{
        assertThat(restController).isNotNull();
    }

    @Test
    public void testController() throws Exception {
        this.mockMvc.perform(get("/api/item"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }
    @Test
    public void testRemove() throws Exception {
        for (Item item: itemService.getAllItems()) {
            itemService.remove(item.getItemId());
        }
        this.mockMvc.perform(delete("/api/item/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void testAdd() throws Exception{
        ItemResp resp = new ItemResp();
        resp.setName("hello");
        this.mockMvc.perform(post("/api/item")
                .content(objectMapper.writeValueAsString(resp))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/api/item").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[*].name", hasItem("hello")));
    }
    @Test
    public void testGet() throws Exception{

        ItemResp resp = new ItemResp();
        resp.setName("TestGet");
        this.mockMvc.perform(post("/api/item")
                        .content(objectMapper.writeValueAsString(resp))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Integer> res = (Lists.newArrayList(itemService.getAllItems()))
                .stream().map(x->x.getItemId()).max(Integer::compare);

        if(res.isPresent()){
            this.mockMvc.perform(get("/api/item/" + res.get())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect( content().string(containsString("TestGet")));
        }

    }

}
