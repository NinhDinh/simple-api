package org.example.ninh.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@Tag(name = "Hello")
public class HelloController {

    @Operation(
            summary = "Get hello user",
            description = "Requires realm role read:hello",
            security = @SecurityRequirement(name = "oauth2ClientCredentials")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - missing role read:hello")
    })
    @PreAuthorize("hasRole('read:hello')")
    @GetMapping("{name}")
    public UserDto getUserByName(@PathVariable String name) {
        return new UserDto("Ninh", name);
    }
}
