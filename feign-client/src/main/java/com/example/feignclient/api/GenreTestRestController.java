package com.example.feignclient.api;

import com.example.feignclient.client.GenreFeignClient;
import com.example.feignclient.dto.genre.GenreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@RestController
@RequestMapping(value = "/genre")
public class GenreTestRestController {

    private final GenreFeignClient genreFeignClient;

   @Autowired
    public GenreTestRestController(GenreFeignClient genreFeignClient) {
        this.genreFeignClient = genreFeignClient;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<GenreDto>> getAll() {
        return ResponseEntity.ok(genreFeignClient.getAll());
    }
}
